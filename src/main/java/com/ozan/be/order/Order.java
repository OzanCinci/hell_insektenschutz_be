package com.ozan.be.order;

import com.ozan.be.common.Auditable;
import com.ozan.be.order.domain.OrderStatus;
import com.ozan.be.user.User;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fk_order")
public class Order extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(
      name = "user_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "order_user_fk"))
  private User user;

  private String userName;

  private String userEmail;

  private String paymentMethod;

  private Double shippingPrice;

  private Double totalPrice;

  private String address;

  private String city;

  private Integer postalCode;

  private String country;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @OneToMany(
      cascade = CascadeType.ALL,
      mappedBy = "order",
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<OrderItem> orderItems = new ArrayList<>();
}
