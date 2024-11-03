package com.ozan.be.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class ChangePasswordEmailRedirectionDTO implements Serializable {
  @NotNull @NotBlank private String newPassword;

  @NotNull @NotBlank private String email;

  @NotNull private UUID token;
}
