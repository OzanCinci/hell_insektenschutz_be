package com.ozan.be.cronJobs.jobs;

import com.ozan.be.aws.SqsPollingService;
import com.ozan.be.cronJobs.base.BaseJobBean;
import com.ozan.be.cronJobs.services.ISqsMessageHandler;
import com.ozan.be.customException.types.DataNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsPollingJob extends BaseJobBean {
  @Autowired private SqsPollingService sqsPollingService;
  @Autowired private List<? extends ISqsMessageHandler> sqsMessageHandlers;
  private final ExecutorService executorService = Executors.newFixedThreadPool(3);

  @Override
  protected void executeJob() {
    log.info("SQS polling job has been triggered. Polling messages from the que");
    List<Message> messages = sqsPollingService.pollSqsMessages();
    log.info("Successfully fetched {} messages from the queue", messages.size());

    if (messages.isEmpty()) {
      log.info("NO MESSAGE from the queue, early termination.");
      return;
    }

    Map<String, List<Message>> groupedMessages =
        messages.stream()
            .collect(
                Collectors.groupingBy(
                    msg -> msg.attributes().get(MessageSystemAttributeName.MESSAGE_GROUP_ID)));

    log.info("Starting to handle messages.");
    groupedMessages.forEach(
        (messageGroupId, groupMessages) -> {
          executorService.submit(
              () -> {
                ISqsMessageHandler iSqsMessageHandler = provideSqsMessageHandler(messageGroupId);
                iSqsMessageHandler.handle(
                    groupMessages, (message) -> sqsPollingService.deleteMessage(message));
              });
        });
  }

  private ISqsMessageHandler provideSqsMessageHandler(String messageGroupId) {
    return sqsMessageHandlers.stream()
        .filter(handler -> messageGroupId.equals(handler.getMessageGroupId().toString()))
        .findFirst()
        .orElseThrow(
            () ->
                new DataNotFoundException(
                    "Failed to find sqs message handler belonging to messageGroupId: "
                        + messageGroupId));
  }
}
