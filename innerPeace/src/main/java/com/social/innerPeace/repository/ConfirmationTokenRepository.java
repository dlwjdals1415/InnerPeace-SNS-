package com.social.innerPeace.repository;

import com.social.innerPeace.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,String> {
    Optional<ConfirmationToken> findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId, LocalDateTime now, boolean expired);

    List<ConfirmationToken> findAllByExpirationDateBeforeAndExpiredIsFalse(LocalDateTime now);
}