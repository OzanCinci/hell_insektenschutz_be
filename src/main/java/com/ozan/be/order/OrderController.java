package com.ozan.be.order;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.BasicCreateResponseDTO;
import com.ozan.be.common.dtos.BasicReponseDTO;
import com.ozan.be.order.dto.CreateOrderRequestDTO;
import com.ozan.be.order.dtos.OrderCreateRequestDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@AllArgsConstructor
@RestController
public class OrderController extends BaseController {
  private final OrderService orderService;

  @PostMapping("/")
  public ResponseEntity<BasicReponseDTO> createOrder(
      @Valid @RequestBody OrderCreateRequestDTO requestDTO) {
    UUID userId = getCurrentUser().getId();
    orderService.createOrder(userId, requestDTO);
    return ResponseEntity.ok(new BasicReponseDTO(true));
  }

  @PostMapping
  public ResponseEntity<BasicCreateResponseDTO> createOrdersAuthUser(
      @Valid @RequestBody CreateOrderRequestDTO requestDTO) {
    UUID userId = getCurrentUser().getId();
    return ResponseEntity.ok(new BasicCreateResponseDTO(userId));
  }

  @PostMapping("/anonymous")
  public ResponseEntity<String> createOrdersVisitor(
      @Valid @RequestBody CreateOrderRequestDTO requestDTO) {
    return ResponseEntity.ok("Ok");
  }
}
