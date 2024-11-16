package com.ozan.be.review;

import com.ozan.be.common.Auditable;
import com.ozan.be.product.domain.Product;
import com.ozan.be.user.User;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "product_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "review_product_fk"))
  private Product product;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "user_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "review_user_fk"))
  private User user;

  private Integer rating;

  @Column(length = 1024)
  private String comment;

  private Boolean approved;

  private String reviewImage;

  private String itemName;

  private String secondaryName;
}
