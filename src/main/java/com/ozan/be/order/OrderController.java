package com.ozan.be.order;

import com.ozan.be.common.BaseController;
import com.ozan.be.order.dto.CreateOrderRequestDTO;
import com.ozan.be.order.dto.CreateOrderResponseDTO;
import com.ozan.be.order.dto.CreateOrderVisitorRequestDTO;
import com.ozan.be.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@AllArgsConstructor
@RestController
public class OrderController extends BaseController {
  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<CreateOrderResponseDTO> createOrdersAuthUser(
      @Valid @RequestBody CreateOrderRequestDTO requestDTO) {
    UUID userId = getCurrentUser().getId();
    CreateOrderResponseDTO responseDTO = orderService.createOrdersAuthUser(userId, requestDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PostMapping("/anonymous")
  public ResponseEntity<CreateOrderResponseDTO> createOrdersVisitor(
      @Valid @RequestBody CreateOrderVisitorRequestDTO requestDTO) {
    CreateOrderResponseDTO responseDTO = orderService.createOrdersVisitor(requestDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @GetMapping("/{traceCode}")
  public ResponseEntity<CreateOrderResponseDTO> getOrderByTraceCode(
      @PathVariable("traceCode") String traceCode) {
    CreateOrderResponseDTO responseDTO = orderService.getOrderByTraceCode(traceCode);
    return ResponseEntity.ok(responseDTO);
  }

  @GetMapping("/me")
  public ResponseEntity<Page<CreateOrderResponseDTO>> getOrdersByUserId(
      @PageableDefault(size = 5) Pageable pageable) {
    UUID userId = getCurrentUser().getId();
    Page<CreateOrderResponseDTO> responseDTOS = orderService.getOrdersByUserId(pageable, userId);
    return ResponseEntity.ok(responseDTOS);
  }
}
