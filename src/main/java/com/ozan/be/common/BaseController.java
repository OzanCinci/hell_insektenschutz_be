package com.ozan.be.common;

import com.ozan.be.common.dtos.basic.UserDTO;
import com.ozan.be.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public abstract class BaseController {
  public UserDTO getCurrentUser() {
    return ModelMapperUtils.map(
        SecurityContextHolder.getContext().getAuthentication().getPrincipal(), UserDTO.class);
  }
}
