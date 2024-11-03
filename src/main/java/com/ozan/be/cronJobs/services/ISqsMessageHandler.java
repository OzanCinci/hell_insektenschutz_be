package com.ozan.be.cronJobs.services;

import java.util.List;
import java.util.function.Consumer;
import software.amazon.awssdk.services.sqs.model.Message;

public interface ISqsMessageHandler {
  enum messageGroupId {
    INVOICE_SENDER,
    ORDER_INVOICE_STATUS_UPDATE
  }

  void handle(List<Message> messages, Consumer<Message> messageConsumer);

  messageGroupId getMessageGroupId();
}
