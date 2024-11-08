package com.ozan.be.common.controller;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.appconfig.AppDiscountConfigResponseDTO;
import com.ozan.be.common.service.AppConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/common")
@AllArgsConstructor
@RestController
public class AppConfigurationController extends BaseController {
  private final AppConfigurationService appConfigurationService;

  @GetMapping("/discount-config")
  public ResponseEntity<AppDiscountConfigResponseDTO> getDiscountConfig() {
    return ResponseEntity.ok(appConfigurationService.getDiscountConfig());
  }
}
