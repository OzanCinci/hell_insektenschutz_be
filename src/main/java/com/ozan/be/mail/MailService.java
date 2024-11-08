package com.ozan.be.mail;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.mail.domain.MailType;
import com.ozan.be.order.domain.entity.Order;
import com.ozan.be.order.domain.enums.OrderStatus;
import com.ozan.be.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Autowired private JavaMailSender mailSender;
  @Autowired private TemplateEngine templateEngine;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private static final String OWNER_MAIL_ADDRESS = "Info@hell-insektenschutz.de";

  public boolean sendPasswordResetEmail(String email, String url) {
    try {
      String subject = "Passwort zurücksetzen";
      Context context = new Context();
      context.setVariable("resetLink", url);
      String htmlContent = templateEngine.process("reset_password", context);
      sendMail(email, subject, htmlContent);
      return true;
    } catch (Exception e) {
      throw new BadRequestException("Failed to send password reset email.");
    }
  }

  public void sendHtmlEmail(User to, MailType mailType, Order order) {
    switch (mailType) {
      case REGISTER -> handleRegisterMail(to);
      case CREATE_ORDER -> handleCreateOrderMail(to, order);
    }
  }

  private void handleCreateOrderMail(User to, Order order) {
    String subject = "Bestellbestätigung";
    Context context = new Context();
    context.setVariable("userName", to.getFirstName() + " " + to.getLastName());

    // Format order number
    String formattedOrderNumber = order.getTraceCode().replaceAll("(.{3})(.{3})(.{3})", "$1-$2-$3");
    context.setVariable("formattedOrderNumber", formattedOrderNumber);

    String cargoCode = nonNull(order.getCargoCode()) ? order.getCargoCode() : "-";
    context.setVariable("cargoCode", cargoCode);

    // Format order date
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMANY)
            .withZone(ZoneId.of("Europe/Berlin"));
    String formattedOrderDate = formatter.format(order.getCreatedAt());
    context.setVariable("formattedOrderDate", formattedOrderDate);

    // Format total price
    DecimalFormat decimalFormat = new DecimalFormat("0.00€");
    String totalPrice =
        decimalFormat.format(
            order.getPrice() != null ? order.getPrice() + order.getShippingPrice() : 0);
    String price = decimalFormat.format(order.getPrice() != null ? order.getPrice() : 0);
    String shippingPrice =
        decimalFormat.format(order.getShippingPrice() != null ? order.getShippingPrice() : 0);
    context.setVariable("totalPrice", totalPrice);
    context.setVariable("price", price);
    context.setVariable("shippingPrice", shippingPrice);

    // Process order items
    context.setVariable(
        "orderItems",
        order.getOrderItems().stream()
            .map(
                orderItem -> {
                  Map<String, Object> itemMap = new HashMap<>();
                  itemMap.put("itemName", orderItem.getItemName());
                  itemMap.put("secondaryName", orderItem.getSecondaryName());
                  itemMap.put("quantity", orderItem.getQuantity());

                  // Format price
                  String itemPrice =
                      decimalFormat.format(orderItem.getPrice() != null ? orderItem.getPrice() : 0);
                  itemMap.put("price", itemPrice);

                  // Parse attributes as JSON array
                  String attributesStr =
                      orderItem.getAttributes() != null ? orderItem.getAttributes() : "[]";
                  try {
                    String[] attributesArray =
                        objectMapper.readValue(attributesStr, String[].class);
                    itemMap.put("attributes", attributesArray);
                  } catch (Exception e) {
                    throw new BadRequestException("Failed to parse attributes JSON");
                  }

                  itemMap.put("cartImage", orderItem.getCartImage());

                  return itemMap;
                })
            .collect(Collectors.toList()));

    String htmlContent;
    if (OrderStatus.PENDING_PAYMENT.equals(order.getOrderStatus())) {
      htmlContent = templateEngine.process("create_order_pending", context);
    } else {
      htmlContent = templateEngine.process("create_order", context);
    }

    sendMail(to.getEmail(), subject, htmlContent);
    informAdmin(order);
  }

  private void handleRegisterMail(User to) {
    String subject = "Willkommen - bitte nicht antworten";
    Context context = new Context();
    context.setVariable("Benutzername", to.getFirstName());
    String htmlContent = templateEngine.process("register", context);
    sendMail(to.getEmail(), subject, htmlContent);
  }

  private void sendMail(String to, String subject, String htmlBody) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(senderEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlBody, true);

      mailSender.send(message);
    } catch (MessagingException | MailException e) {
      throw new BadRequestException("Failed During mail service.");
    }
  }

  public void informAdmin(Order order) {
    try {
      String subject = "Neue Bestellung - Hell Insek";
      DecimalFormat decimalFormat = new DecimalFormat("0.00€");
      String totalPrice =
          decimalFormat.format(
              order.getPrice() != null ? order.getPrice() + order.getShippingPrice() : 0);

      String message =
          String.format(
              "Bitte überprüfen Sie das Admin-Panel, Sie haben eine neue Bestellung, die auf Sie wartet. Der Gesamtbetrag der Bestellung beträgt %s.",
              totalPrice);

      sendTextMail(OWNER_MAIL_ADDRESS, subject, message);
    } catch (Exception e) {
      // TODO: make this async later...
    }
  }

  // Simple mail sending function for admin notification
  private void sendTextMail(String to, String subject, String textBody) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(senderEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(textBody, false); // false means this is plain text

      mailSender.send(message);
    } catch (MessagingException | MailException e) {
      throw new BadRequestException("Failed to send admin notification email.");
    }
  }

  public void sendInvoiceEmail(String orderNumber, String customerEmail) {
    String subject = "Rechnung für Ihre Bestellung: " + orderNumber;
    String invoiceLink =
        "https://hell-insekten-sonnenschutz-invoices-pdf.s3.eu-central-1.amazonaws.com/"
            + orderNumber
            + ".pdf";
    Context context = new Context();
    context.setVariable("orderNumber", orderNumber);
    context.setVariable("invoiceLink", invoiceLink);

    String htmlContent = templateEngine.process("invoice_sender", context);
    sendMail(customerEmail, subject, htmlContent);
  }
}
