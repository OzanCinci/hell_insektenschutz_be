package com.ozan.be.auth;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.ozan.be.auth.dtos.AuthenticationRequestDTO;
import com.ozan.be.auth.dtos.AuthenticationResponseDTO;
import com.ozan.be.auth.dtos.ChangePasswordEmailRedirectionDTO;
import com.ozan.be.auth.dtos.RefreshTokenRequestDTO;
import com.ozan.be.auth.dtos.RegisterRequestDTO;
import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.mail.MailService;
import com.ozan.be.mail.domain.MailType;
import com.ozan.be.token.Token;
import com.ozan.be.token.TokenRepository;
import com.ozan.be.token.TokenType;
import com.ozan.be.user.User;
import com.ozan.be.user.domain.PasswordResetAuthToken;
import com.ozan.be.user.repository.PasswordResetAuthTokenRepository;
import com.ozan.be.user.repository.UserRepository;
import com.ozan.be.user.service.PasswordResetAuthTokenService;
import com.ozan.be.user.service.UserService;
import com.ozan.be.utils.ModelMapperUtils;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final MailService mailService;
  private final UserService userService;
  private final PasswordResetAuthTokenService passwordResetAuthTokenService;
  private final UserRepository userRepository;
  private final PasswordResetAuthTokenRepository passwordResetAuthTokenRepository;

  private User findUserByEmail(String email) {
    return repository.findByEmail(email).orElse(null);
  }

  private User findUserByEmailThrowsException(String email) {
    return repository
        .findByEmail(email)
        .orElseThrow(() -> new DataNotFoundException("User with email: " + email + " not found."));
  }

  @Transactional
  public AuthenticationResponseDTO register(RegisterRequestDTO request) {
    User user = ModelMapperUtils.map(request, User.class);
    validateRegisterRequestDTO(user);

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setCreatedAt(Instant.now());

    User savedUser = repository.save(user);
    mailService.sendHtmlEmail(savedUser, MailType.REGISTER, null);

    return buildAuthenticationResponseDTO(savedUser, true);
  }

  private void validateRegisterRequestDTO(User user) {
    if (isNull(user)) {
      throw new BadRequestException("Please check user register data.");
    }
    if (nonNull(findUserByEmail(user.getEmail()))) {
      throw new BadRequestException("User with this email already exists.");
    }
  }

  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = findUserByEmail(request.getEmail());
    return buildAuthenticationResponseDTO(user, false);
  }

  private void saveUserToken(User user, String jwtToken) {
    Token token = new Token();
    token.setUser(user);
    token.setToken(jwtToken);
    token.setTokenType(TokenType.BEARER);
    token.setExpired(false);
    token.setRevoked(false);

    tokenRepository.saveAndFlush(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) return;
    validUserTokens.forEach(
        token -> {
          token.setExpired(true);
          token.setRevoked(true);
        });
    tokenRepository.saveAll(validUserTokens);
  }

  public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO) {
    String refreshToken = requestDTO.getRefreshToken();
    String email = jwtService.extractUsername(refreshToken);

    User user = validateRefreshTokenAndFindUser(email, refreshToken);

    return buildAuthenticationResponseDTO(user, false);
  }

  private User validateRefreshTokenAndFindUser(String email, String refreshToken) {
    if (isNull(email)) {
      throw new BadRequestException("Failed to find user with given refresh token.");
    }

    User user = findUserByEmailThrowsException(email);

    if (!jwtService.isTokenValid(refreshToken, user)) {
      throw new BadRequestException("Invalid refresh token.");
    }
    return user;
  }

  private AuthenticationResponseDTO buildAuthenticationResponseDTO(
      User user, boolean isRegisterRequest) {
    String accessToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    if (!isRegisterRequest) {
      // revokeAllUserTokens(user);
    }

    saveUserToken(user, accessToken);

    AuthenticationResponseDTO responseDTO =
        ModelMapperUtils.map(user, AuthenticationResponseDTO.class);
    responseDTO.setAccessToken(accessToken);
    responseDTO.setRefreshToken(refreshToken);
    return responseDTO;
  }

  public boolean sendPasswordResetEmail(String email) {
    User user = userService.getUserByMail(email);
    String token = passwordResetAuthTokenService.generateOneTimeAuthEntry(user);
    String baseURL = "https://www.hell-insekten-sonnenschutz.com/passwort-reset";
    String url = baseURL + "?auth=" + token;
    return mailService.sendPasswordResetEmail(email, url);
  }

  @Transactional
  public boolean changePasswordViaEmailRedirection(
      ChangePasswordEmailRedirectionDTO changePasswordEmailRedirectionDTO) {
    PasswordResetAuthToken passwordResetAuthTokenByToken =
        passwordResetAuthTokenService.findPasswordResetAuthTokenByToken(
            changePasswordEmailRedirectionDTO.getToken());
    validateChangePasswordViaEmailRedirection(
        passwordResetAuthTokenByToken, changePasswordEmailRedirectionDTO);

    User currentUser = passwordResetAuthTokenByToken.getUser();
    currentUser.setPassword(
        passwordEncoder.encode(changePasswordEmailRedirectionDTO.getNewPassword()));
    passwordResetAuthTokenByToken.setUsed(true);

    passwordResetAuthTokenRepository.save(passwordResetAuthTokenByToken);
    userRepository.save(currentUser);
    return true;
  }

  private void validateChangePasswordViaEmailRedirection(
      PasswordResetAuthToken passwordResetAuthTokenByToken,
      ChangePasswordEmailRedirectionDTO changePasswordEmailRedirectionDTO) {
    if (!passwordResetAuthTokenByToken
        .getUser()
        .getEmail()
        .equals(changePasswordEmailRedirectionDTO.getEmail())) {
      // Token and selected e-mail address do not match.
      throw new BadRequestException("Token und ausgewählte E-Mail-Adresse stimmen nicht überein.");
    }
    if (Boolean.TRUE.equals(passwordResetAuthTokenByToken.isUsed())) {
      // Token is already used once before.
      throw new BadRequestException("Token wurde bereits einmal verwendet.");
    }
    if (Instant.now().isAfter(passwordResetAuthTokenByToken.getValidUntil())) {
      // Token is expired.
      throw new BadRequestException(
          "Token ist abgelaufen. Der an Ihre E-Mail-Adresse gesendete Link ist nach 15 Minuten ungültig.");
    }
  }
}
