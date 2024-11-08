package com.ozan.be.common.dtos.appconfig;

import com.ozan.be.utils.ModelMapperUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class AppDiscountConfigResponseDTO implements Serializable {
  Map<String, DiscountOption> discountOptionMap;

  @Data
  public static class DiscountOption implements Serializable {
    private String key;
    private Double percentage;
    private String text;
  }

  public AppDiscountConfigResponseDTO(List<DiscountOption> discountOptionList) {
    this.discountOptionMap =
        ModelMapperUtils.convertListToArbitraryMap(discountOptionList, DiscountOption::getKey);
  }
}
