package com.ozan.be.aws;

import com.ozan.be.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Slf4j
public class SqsMessagePublisher {

  private final SqsClient sqsClient;

  public SqsMessagePublisher(
      @Value("${aws.credentials.secret-key}") String AWS_SECRET_KEY,
      @Value("${aws.credentials.access-key}") String AWS_ACCESS_KEY,
      @Value("${aws.sqs.invoice-generator-sqs.region}") String REGION,
      @Value("${aws.sqs.invoice-generator-sqs.queue-url}") String QUEUE_URL) {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
    this.sqsClient =
        SqsClient.builder()
            .region(Region.of(REGION))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    this.QUEUE_URL = QUEUE_URL;
  }

  @Value("${aws.sqs.invoice-generator-sqs.queue-url}")
  private String QUEUE_URL;

  public void sendMessageToSqs(Object object, String messageGroupId, String deduplicationID) {
    String messageBody = ModelMapperUtils.convertToJsonString(object);
    sendMessageToSqs(messageBody, messageGroupId, deduplicationID);
  }

  private boolean sendMessageToSqs(
      String messageBody, String messageGroupId, String deduplicationID) {
    boolean isSuccess = false;
    SendMessageRequest sendMsgRequest =
        SendMessageRequest.builder()
            .queueUrl(this.QUEUE_URL)
            .messageBody(messageBody)
            .messageGroupId(messageGroupId)
            .messageDeduplicationId(deduplicationID)
            .build();

    try {
      SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMsgRequest);
      isSuccess = sendMessageResponse.sdkHttpResponse().isSuccessful();
    } catch (Exception e) {
      log.error("Failed to send SQS message: {}", e.getMessage());
    }

    return isSuccess;
  }
}
