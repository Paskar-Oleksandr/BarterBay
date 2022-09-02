package com.barterbay.app.servcie;

import com.barterbay.app.domain.ConfirmationToken;
import com.barterbay.app.domain.User;
import com.barterbay.app.repository.UserRepository;
import com.barterbay.app.servcie.registration.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ConfirmationTokenService confirmationTokenService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public String registerUser(User user) {

    var encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

    user.setPassword(encodedPassword);

    userRepository.save(user);

    var token = UUID.randomUUID().toString();

    var confirmationToken = new ConfirmationToken(
      token,
      LocalDateTime.now(),
      LocalDateTime.now().plusMinutes(2),
      user
    );

    confirmationTokenService.saveConfirmationToken(confirmationToken);

    return token;
  }
}

