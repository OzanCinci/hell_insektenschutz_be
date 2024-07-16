package com.ozan.be.mail;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.mail.domain.MailType;
import com.ozan.be.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Autowired private JavaMailSender mailSender;

  public String loadHtmlTemplate(String filePath) {
    try {
      Path path = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());
      return new String(Files.readAllBytes(path));
    } catch (IOException | URISyntaxException e) {
      throw new BadRequestException("Failed to read template.");
    }
  }

  public void sendHtmlEmail(User to, MailType mailType) {
    switch (mailType) {
      case REGISTER -> handleRegisterMail(to);
    }
  }

  private void handleRegisterMail(User to) {
    String subject = "Willkommen - bitte nicht antworten";
    String htmlContent = loadHtmlTemplate("templates/register.html");
    htmlContent = htmlContent.replace("[Benutzername]", to.getFirstName());
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
}
