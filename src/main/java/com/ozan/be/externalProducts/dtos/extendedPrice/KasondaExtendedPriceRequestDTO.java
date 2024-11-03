package com.ozan.be.externalProducts.dtos.extendedPrice;

import com.ozan.be.externalProducts.dtos.KasondaPriceRequestDTO;
import com.ozan.be.externalProducts.enums.KasondaExtendedPriceBuildType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KasondaExtendedPriceRequestDTO extends KasondaPriceRequestDTO {
  @NotNull private KasondaExtendedPriceBuildType kasondaExtendedPriceBuildType;

  private Integer width2;
  private Integer height2;
  private Integer depth;
  private Integer heightleft;
  private Integer heightright;
  private Integer widthtop;
  private Integer widthbottom;
}
