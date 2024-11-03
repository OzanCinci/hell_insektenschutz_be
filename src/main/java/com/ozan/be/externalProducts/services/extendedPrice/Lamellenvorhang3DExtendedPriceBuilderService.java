package com.ozan.be.externalProducts.services.extendedPrice;

import com.ozan.be.externalProducts.dtos.extendedPrice.BaseExtendedPriceDTO;
import com.ozan.be.externalProducts.dtos.extendedPrice.ExtendedPriceDTOS;
import com.ozan.be.externalProducts.dtos.extendedPrice.KasondaExtendedPriceRequestDTO;
import com.ozan.be.externalProducts.enums.KasondaExtendedPriceBuildType;
import com.ozan.be.utils.ModelMapperUtils;
import org.springframework.stereotype.Service;

@Service
public class Lamellenvorhang3DExtendedPriceBuilderService implements IExtendedPriceBuilder {
  @Override
  public KasondaExtendedPriceBuildType getBuildType() {
    return KasondaExtendedPriceBuildType.LAMELLENVORHANG_SLOPE_3D_PRICE_OBJ;
  }

  @Override
  public BaseExtendedPriceDTO build(KasondaExtendedPriceRequestDTO dto) {
    return ModelMapperUtils.map(dto, ExtendedPriceDTOS.Lamellenvorhang3DExtendedPriceDTO.class);
  }
}