package com.barterbay.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "bb_user",
  indexes = @Index(name = "ue_index", columnList = "email", unique = true))
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractAuditingEntity implements Serializable {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequenceGenerator")
  @SequenceGenerator(
    name = "user_sequenceGenerator",
    sequenceName = "user_sequence",
    allocationSize = 1)
  private Long id;

  @NotBlank
  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "first_name", length = 100)
  private String firstName;

  @Column(name = "last_name", length = 100)
  private String lastName;

  @NotBlank
  @Column(nullable = false)
  private String password;

  @Version
  private Long version;

  @OneToOne(mappedBy = "user")
  private ConfirmationToken token;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
