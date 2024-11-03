package com.ozan.be.externalProducts.dtos.extendedPrice;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class BaseExtendedPriceDTO implements Serializable {
  private String category;
  private Integer subcategory;
  private String color;
  private Integer blendcolor;
}
