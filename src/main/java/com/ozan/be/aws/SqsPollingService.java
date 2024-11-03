package com.ozan.be.aws;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@Service
public class SqsPollingService {

  private final SqsClient sqsClient;

  @Value("${aws.sqs.hell-insek-event-broker-sqs.queue-url}")
  private String QUEUE_URL;

  public SqsPollingService(
      @Value("${aws.credentials.secret-key}") String AWS_SECRET_KEY,
      @Value("${aws.credentials.access-key}") String AWS_ACCESS_KEY,
      @Value("${aws.sqs.hell-insek-event-broker-sqs.region}") String REGION) {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
    this.sqsClient =
        SqsClient.builder()
            .region(Region.of(REGION))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
  }

  public List<Message> pollSqsMessages() {
    ReceiveMessageRequest receiveRequest =
        ReceiveMessageRequest.builder()
            .queueUrl(QUEUE_URL)
            .maxNumberOfMessages(10)
            .attributeNamesWithStrings(
                "All") // Request all system attributes, including MessageGroupId
            .build();

    List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
    return messages;
  }

  public void deleteMessage(Message message) {
    try {
      DeleteMessageRequest deleteMessageRequest =
          DeleteMessageRequest.builder()
              .queueUrl(QUEUE_URL)
              .receiptHandle(message.receiptHandle())
              .build();
      sqsClient.deleteMessage(deleteMessageRequest);
      log.info("Message with ID {} deleted successfully", message.messageId());
    } catch (Exception e) {
      log.error("Failed to delete message with ID {}: {}", message.messageId(), e.getMessage());
    }
  }
}
