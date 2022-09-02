package com.barterbay.app.servcie.registration;

import com.barterbay.app.domain.ConfirmationToken;
import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.user.UserRegistrationDTO;
import com.barterbay.app.exception.registration.OverdueTokenException;
import com.barterbay.app.repository.ConfirmationTokenRepository;
import com.barterbay.app.repository.UserRepository;
import com.barterbay.app.servcie.UserService;
import com.barterbay.app.servcie.email.GmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserRegistrationService {

  private final UserService userService;
  private final GmailService gmailService;
  private final ConfirmationTokenService confirmationTokenService;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final UserRepository userRepository;

  public String register(UserRegistrationDTO userRegistrationDTO) {

    var userToken = userService.registerUser(new User(
      userRegistrationDTO.getEmail(),
      userRegistrationDTO.getPassword()
    ));

    String link = "http://localhost:3111/v1/bb/registration/confirm?token=" + userToken;
    gmailService.sendEmail(
      new String[]{userRegistrationDTO.getEmail()},
      "Confirm your email",
      link);

    return userToken;
  }

  @Transactional
  public void confirmToken(String token) {
    var confirmationToken = confirmationTokenService
      .getToken(token)
      .orElseThrow(() ->
        new IllegalStateException("Token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("Email already confirmed");
    }

    if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      deleteUserAndToken(confirmationToken);
    } else {
      confirmationTokenService.setConfirmedAt(token);
    }
  }

  private void deleteUserAndToken(ConfirmationToken confirmationToken){
    confirmationTokenService.deleteToken(confirmationToken);
    userRepository.deleteUserByToken(confirmationToken);
    confirmationTokenRepository.flush();
    userRepository.flush();
    throw new OverdueTokenException("Dear user, verification link has expired, please register again. " +
      "You are able to register with the same email and receive new verification link");
  }
}
