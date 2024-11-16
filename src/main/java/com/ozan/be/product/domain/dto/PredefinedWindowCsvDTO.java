package com.ozan.be.product.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class PredefinedWindowCsvDTO implements Serializable {
  @JsonProperty("WindowName")
  private String windowName;

  @JsonProperty("WindowProducer")
  private String windowProducer;

  @JsonProperty("WindowType")
  private String windowType;

  @JsonProperty("WindowNumber")
  private String windowNumber;

  @JsonProperty("WindowWidth")
  private String windowWidth;

  @JsonProperty("WindowHeight")
  private String windowHeight;
}
