package com.ozan.be.common.service;

import com.ozan.be.common.dtos.appconfig.AppDiscountConfigResponseDTO;
import com.ozan.be.management.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppConfigurationService {
  private final DiscountService discountService;

  public AppDiscountConfigResponseDTO getDiscountConfig() {
    return discountService.getDiscountConfig();
  }
}
