package com.ozan.be.product.controller;

import com.ozan.be.common.dtos.basic.BasicListResponseDTO;
import com.ozan.be.product.domain.dto.PredefinedWindowDimensionResponseDTO;
import com.ozan.be.product.service.PredefinedWindowsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predefined-windows")
@AllArgsConstructor
public class PredefinedWindowController {
  private final PredefinedWindowsService predefinedWindowsService;

  @GetMapping("/{producer}")
  public ResponseEntity<BasicListResponseDTO<String>> getLevel1Options(
      @PathVariable("producer") String producer) {
    return ResponseEntity.ok(predefinedWindowsService.getLevel1Options(producer));
  }

  @GetMapping("/{producer}/{type}")
  public ResponseEntity<BasicListResponseDTO<String>> getLevel2Options(
      @PathVariable("producer") String producer, @PathVariable("type") String type) {
    return ResponseEntity.ok(predefinedWindowsService.getLevel2Options(producer, type));
  }

  @GetMapping("/{producer}/{type}/{number}")
  public ResponseEntity<PredefinedWindowDimensionResponseDTO> getDimensions(
      @PathVariable("producer") String producer,
      @PathVariable("type") String type,
      @PathVariable("number") String number) {
    return ResponseEntity.ok(predefinedWindowsService.getDimensions(producer, type, number));
  }
}
