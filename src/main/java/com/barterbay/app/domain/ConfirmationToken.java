package com.barterbay.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken implements Serializable {

  @SequenceGenerator(
    name = "confirmation_token_sequenceGenerator",
    sequenceName = "confirmation_token_sequence",
    allocationSize = 1)
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "confirmation_token_sequenceGenerator"
  )
  @Column(name = "token_id")
  private Long id;
  @Column(name = "token", nullable = false, unique = true)
  private String token;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  private LocalDateTime confirmedAt;
  @OneToOne(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(
    nullable = false,
    name = "user_id"
  )
  private User user;

  public ConfirmationToken(String token,
                           LocalDateTime createdAt,
                           LocalDateTime expiresAt,
                           User user) {
    this.token = token;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.user = user;
  }
}
