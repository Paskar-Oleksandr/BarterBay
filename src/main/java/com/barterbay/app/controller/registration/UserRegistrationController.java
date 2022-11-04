package com.barterbay.app.controller.registration;

import com.barterbay.app.domain.dto.user.UserRegistrationDTO;
import com.barterbay.app.servcie.registration.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/registrations")
@AllArgsConstructor
public class UserRegistrationController {
  private final UserRegistrationService userRegistrationService;

  @PostMapping
  public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
    return ResponseEntity.ok(userRegistrationService.registerUser(userRegistrationDTO));
  }

  @PostMapping("/confirm")
  public ModelAndView confirm(@RequestParam String token) {
    userRegistrationService.confirmToken(token);
    return new ModelAndView("create-user-data");
  }
}
