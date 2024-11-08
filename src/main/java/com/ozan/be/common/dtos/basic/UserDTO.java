package com.ozan.be.common.dtos.basic;

import com.ozan.be.user.domain.Role;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class UserDTO {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Role role;
  private Instant createdAt;
}
