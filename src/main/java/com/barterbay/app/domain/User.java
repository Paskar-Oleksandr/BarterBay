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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "bb_user",
  indexes = @Index(name = "ue_index", columnList = "email", unique = true))
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = -2548006027287809458L;

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

  @NotBlank
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @NotBlank
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @NotBlank
  @Column(nullable = false)
  private String password;

  @Version
  private Long version;

  @OneToMany(mappedBy = "user")
  private Set<Good> goods;
  @OneToOne(mappedBy = "user")
  private ConfirmationToken token;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
