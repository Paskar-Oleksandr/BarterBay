package com.barterbay.app.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "address")
public class Address extends AbstractAuditingEntity {

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

  @OneToOne(mappedBy = "address")
  private Good good;
}
