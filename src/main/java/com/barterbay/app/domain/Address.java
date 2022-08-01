package com.barterbay.app.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "address")
public class Address implements Serializable {

  private static final long serialVersionUID = -7890060770610734074L;

  @Id
  @Column(name = "address_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequenceGenerator")
  @SequenceGenerator(
    name = "address_sequenceGenerator",
    sequenceName = "address_sequence",
    allocationSize = 1)
  private Long id;

  @NotBlank
  @Column(name = "country", nullable = false, length = 50)
  private String country;

  @NotBlank
  @Column(name = "city", nullable = false, length = 50)
  private String city;

  @NotBlank
  @Column(name = "street", nullable = false, length = 50)
  private String street;

  @NotBlank
  @Column(name = "zip_code", nullable = false)
  private int zipCode;

  @Version
  private Long version;

  @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
  private Good good;
}
