package com.ozan.be.product.domain.dto;

import com.ozan.be.externalProducts.enums.KasondaExtendedPriceBuildType;
import com.ozan.be.product.domain.projections.PredefinedWindowDimensionsDTO;
import java.io.Serializable;
import lombok.Data;

@Data
public class PredefinedWindowDimensionResponseDTO implements Serializable {
  private Integer width;
  private Integer width2;
  private Integer height;
  private Integer height2;
  private Integer depth;
  private KasondaExtendedPriceBuildType kasondaExtendedPriceBuildType;

  public PredefinedWindowDimensionResponseDTO(PredefinedWindowDimensionsDTO dto) {
    this.width = dto.getWindowWidth();
    this.width2 = dto.getWindowWidth();
    this.height = dto.getWindowHeight();
    this.height2 = dto.getWindowHeight();
    this.depth = 50;
    this.kasondaExtendedPriceBuildType = KasondaExtendedPriceBuildType.DACHFENSTER_PRICE_OBJ;
  }
}
