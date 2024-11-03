package com.ozan.be.externalProducts.dtos.extendedPrice;

import lombok.Data;

@Data
public class ExtendedPriceDTOS {
  @Data
  public static class Lamellenvorhang3DExtendedPriceDTO extends BaseExtendedPriceDTO {
    private Integer width;
    private Integer heightleft;
    private Integer heightright;
  }

  @Data
  public static class Sonderform2DExtendedPriceDTO extends BaseExtendedPriceDTO {
    private Integer width;
    private Integer height;
  }

  @Data
  public static class Sonderform3DExtendedPriceDTO extends BaseExtendedPriceDTO {
    private Integer width;
    private Integer heightleft;
    private Integer heightright;
  }

  @Data
  public static class Sonderform4DExtendedPriceDTO extends BaseExtendedPriceDTO {
    private Integer widthtop;
    private Integer widthbottom;
    private Integer heightleft;
    private Integer heightright;
  }

  @Data
  public static class DachfensterExtendedPriceDTO extends BaseExtendedPriceDTO {
    private Integer width;
    private Integer height;
    private Integer width2;
    private Integer height2;
    private Integer depth;
  }
}
