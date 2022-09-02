package com.barterbay.app.controller.registration;

import com.barterbay.app.domain.dto.user.UserRegistrationDTO;
import com.barterbay.app.servcie.registration.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class UserRegistrationController {
  private final UserRegistrationService userRegistrationService;

  @PostMapping
  public String registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
    return userRegistrationService.register(userRegistrationDTO);
  }

  @GetMapping("/confirm")
  public ModelAndView confirm(@RequestParam("token") String token) {
    userRegistrationService.confirmToken(token);
    return new ModelAndView("create-user-data");
  }
}
