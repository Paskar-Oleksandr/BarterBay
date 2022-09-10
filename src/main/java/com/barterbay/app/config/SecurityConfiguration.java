package com.barterbay.app.config;

import com.barterbay.app.service.UserService;
import com.barterbay.app.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final UserService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/ping/**").permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .formLogin()
      .loginPage("/auth/login").permitAll()
      .defaultSuccessUrl("/auth/success")
      .and()
      .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
      .invalidateHttpSession(true)
      .clearAuthentication(true)
      .deleteCookies(Constants.JSESSIONID)
      .logoutSuccessUrl("/auth/login");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  protected DaoAuthenticationProvider daoAuthenticationProvider() {
    final var daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(userService);
    return daoAuthenticationProvider;
  }

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
