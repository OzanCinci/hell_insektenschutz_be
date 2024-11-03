package com.ozan.be.user.repository;

import com.ozan.be.user.domain.PasswordResetAuthToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetAuthTokenRepository
    extends JpaRepository<PasswordResetAuthToken, UUID> {

  Optional<PasswordResetAuthToken> findPasswordResetAuthTokenByToken(UUID token);
}
