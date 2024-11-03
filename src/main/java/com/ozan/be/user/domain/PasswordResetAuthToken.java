package com.ozan.be.user.domain;

import com.ozan.be.common.Auditable;
import com.ozan.be.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "password_reset_auth_token")
public class PasswordResetAuthToken extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private UUID token;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(
      name = "user_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "password_reset_auth_user_fk"))
  private User user;

  private Instant validUntil;

  private boolean isUsed = false;

  @PrePersist
  public void generateToken() {
    this.token = UUID.randomUUID();
  }
}
