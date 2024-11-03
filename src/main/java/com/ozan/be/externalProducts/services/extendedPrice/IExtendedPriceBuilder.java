package com.ozan.be.externalProducts.services.extendedPrice;

import com.ozan.be.externalProducts.dtos.extendedPrice.BaseExtendedPriceDTO;
import com.ozan.be.externalProducts.dtos.extendedPrice.KasondaExtendedPriceRequestDTO;
import com.ozan.be.externalProducts.enums.KasondaExtendedPriceBuildType;

public interface IExtendedPriceBuilder {
  KasondaExtendedPriceBuildType getBuildType();

  BaseExtendedPriceDTO build(KasondaExtendedPriceRequestDTO dto);
}
