package com.ozan.be.user.service;

import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.user.User;
import com.ozan.be.user.domain.PasswordResetAuthToken;
import com.ozan.be.user.repository.PasswordResetAuthTokenRepository;
import com.ozan.be.utils.AESUtils;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordResetAuthTokenService {
  @Autowired private PasswordResetAuthTokenRepository passwordResetAuthTokenRepository;
  @Autowired private AESUtils aesUtils;
  private static final String DELIMITER = "|::|";

  @Transactional
  public PasswordResetAuthToken findPasswordResetAuthTokenByToken(UUID token) {
    return passwordResetAuthTokenRepository
        .findPasswordResetAuthTokenByToken(token)
        .orElseThrow(
            () -> new DataNotFoundException("Password reset token: " + token + " not found."));
  }

  private PasswordResetAuthToken saveAndFlush(PasswordResetAuthToken passwordResetAuthToken) {
    return passwordResetAuthTokenRepository.saveAndFlush(passwordResetAuthToken);
  }

  @Transactional
  public String generateOneTimeAuthEntry(User user) {
    PasswordResetAuthToken resetAuthToken = new PasswordResetAuthToken();
    resetAuthToken.setUser(user);
    resetAuthToken.setValidUntil(Instant.now().plus(Duration.ofMinutes(15)));
    resetAuthToken.setUsed(false);
    PasswordResetAuthToken saved = saveAndFlush(resetAuthToken);
    String valueToEncrypt = saved.getToken() + DELIMITER + user.getEmail();
    return aesUtils.encrypt(valueToEncrypt);
  }
}
