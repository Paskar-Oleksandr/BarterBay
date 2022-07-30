package com.barterbay.app.domain;

import com.barterbay.app.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "good")
public class Good extends AbstractAuditingEntity {

  @Id
  @Column(name = "good_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "good_sequenceGenerator")
  @SequenceGenerator(
    name = "good_sequenceGenerator",
    sequenceName = "good_sequence",
    allocationSize = 1)
  private Long id;

  @NotBlank
  @Column(name = "name", nullable = false, length = 100)
  private String goodName;

  @NotBlank
  @Column(name = "description", nullable = false, unique = true, length = 200)
  private String description;

  @NotBlank
  @Column(name = "category", nullable = false)
  @Enumerated(EnumType.STRING)
  private Category category;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
