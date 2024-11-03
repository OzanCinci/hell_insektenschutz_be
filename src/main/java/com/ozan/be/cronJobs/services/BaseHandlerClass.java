package com.ozan.be.cronJobs.services;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sqs.model.Message;

public abstract class BaseHandlerClass implements ISqsMessageHandler {

  protected void generateAndSetSqsHandlerId() {
    String uniqueSqsHandlerID = UUID.randomUUID().toString();
    MDC.put("sqsHandlerID", uniqueSqsHandlerID);
  }

  protected abstract void run(List<Message> messages, Consumer<Message> messageConsumer);

  public void handle(List<Message> messages, Consumer<Message> messageConsumer) {
    try {
      generateAndSetSqsHandlerId();
      run(messages, messageConsumer);
    } finally {
      MDC.clear();
    }
  }
}
