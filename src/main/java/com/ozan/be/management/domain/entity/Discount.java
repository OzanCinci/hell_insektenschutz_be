package com.ozan.be.management.domain.entity;

import static java.util.Objects.nonNull;

import com.ozan.be.common.Auditable;
import com.ozan.be.management.domain.enums.DiscountProductType;
import com.ozan.be.management.domain.enums.DiscountType;
import com.ozan.be.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "discount")
public class Discount extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = true,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "dealer_user_fk"))
  private User user;

  private Double percentage;

  @Enumerated(EnumType.STRING)
  private DiscountType discountType;

  @Enumerated(EnumType.STRING)
  private DiscountProductType discountProductType;

  private Instant validUntil;

  private boolean isActive;

  private String text;

  public String getKey() {
    return nonNull(user) ? user.getEmail() : discountType.toString();
  }
}
