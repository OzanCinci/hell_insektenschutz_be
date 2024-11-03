package com.ozan.be.cronJobs.services;

import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@Service
public class SqsOrderInvoiceStatusUpdateMessageHandlerService extends BaseHandlerClass {
  @Override
  public messageGroupId getMessageGroupId() {
    return ISqsMessageHandler.messageGroupId.ORDER_INVOICE_STATUS_UPDATE;
  }

  @Override
  public void run(List<Message> messages, Consumer<Message> messageConsumer) {
    log.info(
        "Started to SqsOrderInvoiceStatusUpdateMessageHandlerService with {} message(s).",
        messages.size());
    // TODO: update order invoice generated field
    // TODO: save and flush
    // TODO: delete message
  }
}
