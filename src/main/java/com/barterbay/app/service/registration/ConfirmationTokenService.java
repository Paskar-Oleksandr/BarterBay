package com.barterbay.app.service.registration;

import com.barterbay.app.domain.ConfirmationToken;
import com.barterbay.app.domain.User;
import com.barterbay.app.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ConfirmationTokenService {
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public void saveConfirmationToken(String token, User user) {
    var confirmationToken = new ConfirmationToken(
      token, LocalDateTime.now(),
      LocalDateTime.now().plusMinutes(2), user
    );
    confirmationTokenRepository.save(confirmationToken);
  }

  @Transactional(readOnly = true)
  public Optional<ConfirmationToken> findToken(String token) {
    return confirmationTokenRepository.findByToken(token);
  }

  public void deleteConfirmationToken(ConfirmationToken confirmationToken) {
    confirmationTokenRepository.deleteById(confirmationToken.getId());
  }
}
