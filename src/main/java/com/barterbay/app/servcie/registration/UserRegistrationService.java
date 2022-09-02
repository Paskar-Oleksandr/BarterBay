package com.barterbay.app.servcie.registration;

import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.user.UserRegistrationDTO;
import com.barterbay.app.exception.registration.OverdueTokenException;
import com.barterbay.app.repository.UserRepository;
import com.barterbay.app.servcie.email.GmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserRegistrationService {

  private final GmailService gmailService;
  private final ConfirmationTokenService confirmationTokenService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;

  public String registerUser(UserRegistrationDTO userRegistrationDTO) {
    final var user = createUser(userRegistrationDTO);
    final var userToken = createToken(user);
    final var link = "http://localhost:3111/v1/bb/registration/confirm?token=" + userToken;
    gmailService.sendEmail(
      new String[]{userRegistrationDTO.getEmail()},
      "Confirm your email",
      link);
    return userToken;
  }

  @Transactional(noRollbackFor = OverdueTokenException.class)
  public void confirmToken(String token) {
    var confirmationToken = confirmationTokenService.findToken(token)
      .orElseThrow(() -> new IllegalStateException("Token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("Email already confirmed");
    }

    if (!confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      confirmationToken.setConfirmedAt(LocalDateTime.now());
    } else {
      confirmationTokenService.deleteConfirmationToken(confirmationToken);
      throw new OverdueTokenException("Dear user, verification link has expired, please register again. " +
        "You are able to register with the same email and receive new verification link");
    }
  }

  private User createUser(UserRegistrationDTO userRegistrationDTO) {
    final var user = new User(
      userRegistrationDTO.getEmail(),
      bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword())
    );
    return userRepository.save(user);
  }

  private String createToken(User user) {
    var token = UUID.randomUUID().toString();
    confirmationTokenService.saveConfirmationToken(token, user);
    return token;
  }
}
