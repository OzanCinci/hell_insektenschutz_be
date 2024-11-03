package com.ozan.be.mail.domain;

import java.time.Instant;
import java.util.UUID;

/*
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mail_sent_record")
 */
// public class MailSentRecord extends Auditable<UUID> implements Serializable {
public class MailSentRecord {
  // @Id
  // @GeneratedValue(strategy = GenerationType.UUID)
  /** * IT WILL BE COMPLETED LATER, FOR NOW DO NOT CREATE ANY TABLE */
  private UUID id;

  private String to;

  private MailSentRecordType type;

  private UUID relatedEntityId;

  private MailSentRecordStatus status = MailSentRecordStatus.PENDING;

  private String err;

  private String params;

  private Integer retryCount = 0;

  private Instant lastTryDate;

  private String subject;

  private String body;
}
