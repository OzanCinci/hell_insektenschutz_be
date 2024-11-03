package com.ozan.be.auth;

import com.ozan.be.auth.dtos.AuthenticationRequestDTO;
import com.ozan.be.auth.dtos.AuthenticationResponseDTO;
import com.ozan.be.auth.dtos.ChangePasswordEmailRedirectionDTO;
import com.ozan.be.auth.dtos.PasswordResetEmailRequest;
import com.ozan.be.auth.dtos.RefreshTokenRequestDTO;
import com.ozan.be.auth.dtos.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponseDTO> register(
      @Valid @RequestBody RegisterRequestDTO request) {
    AuthenticationResponseDTO responseDTO = service.register(request);
    return ResponseEntity.ok(responseDTO);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponseDTO> authenticate(
      @Valid @RequestBody AuthenticationRequestDTO request) {
    AuthenticationResponseDTO responseDTO = service.authenticate(request);
    return ResponseEntity.ok(responseDTO);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AuthenticationResponseDTO> refreshToken(
      @Valid @RequestBody RefreshTokenRequestDTO requestDTO) {
    AuthenticationResponseDTO responseDTO = service.refreshToken(requestDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PostMapping("/reset-password-email")
  public ResponseEntity<Boolean> sendPasswordResetEmail(
      @Valid @RequestBody PasswordResetEmailRequest requestDTO) {
    boolean isSuccess = service.sendPasswordResetEmail(requestDTO.getEmail());
    return ResponseEntity.ok(isSuccess);
  }

  @PutMapping("/change-password-email-redirection")
  public ResponseEntity<Boolean> changePasswordViaEmailRedirection(
      @Valid @RequestBody ChangePasswordEmailRedirectionDTO changePasswordEmailRedirectionDTO) {
    boolean isSuccess =
        service.changePasswordViaEmailRedirection(changePasswordEmailRedirectionDTO);
    return ResponseEntity.ok(isSuccess);
  }
}
