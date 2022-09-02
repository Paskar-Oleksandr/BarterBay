package com.barterbay.app.repository;

import com.barterbay.app.domain.ConfirmationToken;
import com.barterbay.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
  @Transactional(readOnly = true)
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  void deleteUserByToken(ConfirmationToken token);
}
