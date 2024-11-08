package com.ozan.be.cronJobs.services;

import com.ozan.be.mail.MailService;
import com.ozan.be.order.service.OrderService;
import com.ozan.be.utils.ModelMapperUtils;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsInvoiceSenderMessageHandlerService extends BaseHandlerClass {
  private record InvoiceEmailMessage(String orderNumber, String customerEmail) {}
  ;

  private final MailService mailService;
  private final OrderService orderService;

  @Override
  public messageGroupId getMessageGroupId() {
    return ISqsMessageHandler.messageGroupId.INVOICE_SENDER;
  }

  @Override
  public void run(List<Message> messages, Consumer<Message> messageConsumer) {
    log.info(
        "Started to SqsInvoiceSenderMessageHandlerService with {} message(s).", messages.size());

    for (Message msg : messages) {
      try {
        InvoiceEmailMessage invoiceEmailMessage =
            ModelMapperUtils.readFromString(msg.body(), InvoiceEmailMessage.class);

        if (!orderService.isInvoiceAlreadyExists(invoiceEmailMessage.orderNumber)) {
          orderService.updateInvoiceStatusOfOrderByTraceCode(invoiceEmailMessage.orderNumber);
          mailService.sendInvoiceEmail(
              invoiceEmailMessage.orderNumber, invoiceEmailMessage.customerEmail);
        }

        messageConsumer.accept(msg);
      } catch (Exception e) {
        log.info("FAILED TO PROCESS SQS MESSAGE FOR ORDER INVOICE MAIL: {}", e.getMessage());
      }
    }
  }
}
