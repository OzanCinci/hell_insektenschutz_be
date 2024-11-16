package com.ozan.be.product.domain.projections;

import java.io.Serializable;

public interface PredefinedWindowDimensionsDTO extends Serializable {
  Integer getWindowWidth();

  Integer getWindowHeight();
}
