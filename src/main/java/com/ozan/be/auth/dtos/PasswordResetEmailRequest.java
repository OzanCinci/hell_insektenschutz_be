package com.ozan.be.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Data
public class PasswordResetEmailRequest implements Serializable {
  @NotNull @NotBlank private String email;
}
