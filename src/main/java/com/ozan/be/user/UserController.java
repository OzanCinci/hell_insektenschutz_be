package com.ozan.be.user;

import com.ozan.be.common.dtos.BasicReponseDTO;
import com.ozan.be.user.dtos.ChangePasswordRequestDTO;
import com.ozan.be.user.dtos.UserResponseDTO;
import com.ozan.be.user.service.UserService;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @PutMapping("/change-password")
  public ResponseEntity<BasicReponseDTO> changePassword(
      @RequestBody ChangePasswordRequestDTO request, Principal connectedUser) {
    service.changePassword(request, connectedUser);
    return ResponseEntity.ok(new BasicReponseDTO(true));
  }

  @GetMapping("/{id}")
  public void getUserDetails(@PathVariable("id") UUID id) {
    UserResponseDTO response = service.getUserDetails(id);
    ResponseEntity.ok(response);
  }
}
