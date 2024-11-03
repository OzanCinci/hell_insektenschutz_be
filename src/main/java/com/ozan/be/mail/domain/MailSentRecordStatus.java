package com.ozan.be.mail.domain;

public enum MailSentRecordStatus {
  PENDING,
  SUCCESS,
  FAILED,
  RETRIED_AND_FAILED,
  RETRIED_AND_SUCCEEDED
}
