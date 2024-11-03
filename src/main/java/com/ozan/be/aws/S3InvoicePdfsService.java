package com.ozan.be.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

@Slf4j
@Service
public class S3InvoicePdfsService {
  private final S3Client s3Client;

  @Value("${aws.s3.invoice-pdfs.region}")
  private String REGION;

  @Value("${aws.s3.invoice-pdfs.bucket-name}")
  private String BUCKET_NAME;

  public S3InvoicePdfsService(
      @Value("${aws.credentials.secret-key}") String AWS_SECRET_KEY,
      @Value("${aws.credentials.access-key}") String AWS_ACCESS_KEY,
      @Value("${aws.s3.invoice-pdfs.region}") String REGION) {

    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
    S3Configuration s3Config = S3Configuration.builder().pathStyleAccessEnabled(true).build();
    this.s3Client =
        S3Client.builder()
            .region(Region.of(REGION))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .serviceConfiguration(s3Config)
            .build();
  }

  public boolean doesObjectExist(String objectKey) {
    try {
      s3Client.headObject(HeadObjectRequest.builder().bucket(BUCKET_NAME).key(objectKey).build());
      return true;
    } catch (Exception e) {
      log.info("Failed to execute get head from s3: {}", e.getMessage());
      return false;
    }
  }
}
