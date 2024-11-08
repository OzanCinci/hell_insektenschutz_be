package com.ozan.be.management.controller;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.basic.BasicReponseDTO;
import com.ozan.be.management.domain.DiscountSearchFilter;
import com.ozan.be.management.dto.DiscountManagementResponseDTO;
import com.ozan.be.management.dto.DiscountUpdateRequestDTO;
import com.ozan.be.management.service.DiscountService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/management/discounts")
@AllArgsConstructor
@RestController
public class ManagementDiscountController extends BaseController {
  private final DiscountService discountService;

  @GetMapping
  public ResponseEntity<Page<DiscountManagementResponseDTO>> getAllDiscounts(
      @PageableDefault(size = 20) Pageable pageable,
      @ParameterObject DiscountSearchFilter discountSearchFilter) {
    return ResponseEntity.ok(
        discountService.getAllDiscounts(pageable, discountSearchFilter.getPredicate()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BasicReponseDTO> updateDiscount(
      @PathVariable("id") UUID id, @Valid @RequestBody DiscountUpdateRequestDTO requestDTO) {
    discountService.updateDiscount(id, requestDTO);
    return ResponseEntity.ok(new BasicReponseDTO(true));
  }
}
