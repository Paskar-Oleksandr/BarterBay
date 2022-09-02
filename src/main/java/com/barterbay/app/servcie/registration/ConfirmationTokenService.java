package com.barterbay.app.servcie.registration;

import com.barterbay.app.domain.ConfirmationToken;
import com.barterbay.app.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public void saveConfirmationToken(ConfirmationToken token) {
    confirmationTokenRepository.save(token);
  }

  public Optional<ConfirmationToken> getToken(String token) {
    return confirmationTokenRepository.findByToken(token);
  }

  public void setConfirmedAt(String token) {
    confirmationTokenRepository.updateConfirmedAt(
      token, LocalDateTime.now());
  }

  public void deleteToken(ConfirmationToken confirmationToken) {
    confirmationTokenRepository.deleteConfirmationTokenByUser(confirmationToken.getUser());
  }
}
