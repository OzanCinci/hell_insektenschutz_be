package com.ozan.be;

import static com.ozan.be.user.Role.*;

import com.ozan.be.auth.AuthenticationService;
import com.ozan.be.auth.RegisterRequest;
import com.ozan.be.order.*;
import com.ozan.be.product.Product;
import com.ozan.be.product.ProductRepository;
import com.ozan.be.review.ReviewRequest;
import com.ozan.be.review.ReviewService;
import com.ozan.be.user.User;
import com.ozan.be.user.UserRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      AuthenticationService service,
      UserRepository userRepository,
      OrderService orderService,
      ProductRepository productRepository,
      OrderItemRepository orderItemRepository,
      ReviewService reviewService) {
    return args -> {
      var admin =
          RegisterRequest.builder()
              .firstname("Admin")
              .lastname("Admin")
              .email("admin@mail.com")
              .password("password")
              .role(ADMIN)
              .build();
      System.out.println("Admin token: " + service.register(admin).getAccessToken());

      var manager =
          RegisterRequest.builder()
              .firstname("Admin")
              .lastname("Admin")
              .email("manager@mail.com")
              .password("password")
              .role(MANAGER)
              .build();
      System.out.println("Manager token: " + service.register(manager).getAccessToken());

      var regularUser =
          RegisterRequest.builder()
              .firstname("Kadircan")
              .lastname("Bozkurt")
              .email("kadircan.bozkurt@mail.com")
              .password("password")
              .role(USER)
              .build();
      System.out.println("Regular User token: " + service.register(regularUser).getAccessToken());

      var p =
          Product.builder()
              .numberOfRating(0)
              .rating(0.0)
              .stock(true)
              .category("random category")
              .description("description part of the product")
              .imageUrl("https://www.hell-insek.de/image/product.jpg")
              .name("Drehfenster")
              .build();
      productRepository.save(p);

      var tempUserOptional = userRepository.findByEmail("kadircan.bozkurt@mail.com");
      if (tempUserOptional.isPresent()) {
        User tempUser = tempUserOptional.get(); // Get the User from the Optional
        var order =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order);

        var tempProduct = productRepository.findById(1);
        if (tempProduct.isPresent()) {
          Product product = tempProduct.get();
          var orderItem =
              OrderItem.builder()
                  .order(order)
                  .price(12.0)
                  .product(product)
                  .quantity(2)
                  .measurements("some measurements")
                  .build();

          orderItemRepository.save(orderItem);
        }

        var tempProduct2 = productRepository.findById(1);
        if (tempProduct2.isPresent()) {
          Product product = tempProduct2.get();
          var orderItem =
              OrderItem.builder()
                  .order(order)
                  .price(21.0)
                  .product(product)
                  .quantity(1)
                  .measurements("some measurements2")
                  .build();

          orderItemRepository.save(orderItem);
        }
      }

      var tempUserOptional2 = userRepository.findByEmail("manager@mail.com");
      if (tempUserOptional2.isPresent()) {
        User tempUser = tempUserOptional2.get(); // Get the User from the Optional
        var order =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order);

        var order5 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order5);

        var order6 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order6);

        var order7 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order7);

        var order8 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order8);

        var order9 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order9);

        var order10 =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order10);

        var tempProduct2 = productRepository.findById(1);
        if (tempProduct2.isPresent()) {
          Product product = tempProduct2.get();
          var orderItem =
              OrderItem.builder()
                  .order(order)
                  .price(21.0)
                  .product(product)
                  .quantity(1)
                  .measurements("some measurements122")
                  .build();

          orderItemRepository.save(orderItem);
        }
      }

      var tempUserOptional3 = userRepository.findByEmail("admin@mail.com");
      if (tempUserOptional3.isPresent()) {
        User tempUser = tempUserOptional3.get(); // Get the User from the Optional
        var order =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order);

        // duplicate order
        var orderza =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(orderza);

        var orderka =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(orderka);

        var orderda =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address("address 1 2 3 4")
                .city("texas")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(orderda);
        // duplicate order

        var tempProduct2 = productRepository.findById(1);
        if (tempProduct2.isPresent()) {
          Product product = tempProduct2.get();
          var orderItem =
              OrderItem.builder()
                  .order(order)
                  .price(132.0)
                  .product(product)
                  .quantity(4)
                  .measurements("some measurements6")
                  .build();

          var orderItem2 =
              OrderItem.builder()
                  .order(order)
                  .price(245.0)
                  .product(product)
                  .quantity(2)
                  .measurements("some measurements6 etc.")
                  .build();

          orderItemRepository.save(orderItem);
          orderItemRepository.save(orderItem2);
        }
      }

      if (tempUserOptional3.isPresent()) {
        User tempUser = tempUserOptional3.get(); // Get the User from the Optional
        var order =
            Order.builder()
                .postalCode(59850)
                .shippingPrice(50.50)
                .totalPrice(60.50)
                .user(tempUser)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .address(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has ")
                .city("city")
                .country("turkey")
                .orderStatus("non-active")
                .paymentMethod("paypal")
                .userName(tempUser.getFirstname() + " " + tempUser.getLastname())
                .userEmail(tempUser.getEmail())
                .build();

        orderService.saveOrder(order);

        var tempProduct2 = productRepository.findById(1);
        if (tempProduct2.isPresent()) {
          Product product = tempProduct2.get();
          var orderItem =
              OrderItem.builder()
                  .order(order)
                  .price(2.0)
                  .product(product)
                  .quantity(1)
                  .measurements("some measurements6 final")
                  .build();

          orderItemRepository.save(orderItem);
        }

        var ReviewRequest1 =
            ReviewRequest.builder()
                .email("admin@mail.com")
                .productId(1)
                .rating(4)
                .email("admin@mail.com")
                .comment("it is okay!")
                .build();
        var ReviewRequest2 =
            ReviewRequest.builder()
                .email("admin@mail.com")
                .productId(1)
                .rating(5)
                .email("admin@mail.com")
                .comment("it is greate, I bought it twice!!!!")
                .build();
        var ReviewRequest3 =
            ReviewRequest.builder()
                .email("admin@mail.com")
                .productId(1)
                .rating(5)
                .email("admin@mail.com")
                .comment(
                    "it is greate, I bought it twice!!!! danke schön test review to check functionalty. it is greate, I bought it twice!!!! sago kaf kef review test 123 test.")
                .build();
        var ReviewRequest4 =
            ReviewRequest.builder()
                .email("admin@mail.com")
                .productId(1)
                .rating(5)
                .email("admin@mail.com")
                .comment("sago kaf kef review test 123 test")
                .build();
        var ReviewRequest5 =
            ReviewRequest.builder()
                .email("admin@mail.com")
                .productId(1)
                .rating(5)
                .email("admin@mail.com")
                .comment("it is 5/5!")
                .build();

        for (int i = 0; i < 8; i++) {
          var ReviewRequestNew =
              ReviewRequest.builder()
                  .email("admin@mail.com")
                  .productId(1)
                  .rating(i % 5 + 1)
                  .email("admin@mail.com")
                  .comment("it is 5/5!")
                  .build();
          reviewService.createReview(ReviewRequestNew);
        }

        reviewService.createReview(ReviewRequest1);
        reviewService.createReview(ReviewRequest2);
        reviewService.createReview(ReviewRequest3);
        reviewService.createReview(ReviewRequest4);
        reviewService.createReview(ReviewRequest5);
      }
    };
  }
}
