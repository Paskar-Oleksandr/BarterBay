package com.barterbay.app.domain.dto.address;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class AddressDTO {

  private Long id;

  @NotBlank(message = "Country cannot be empty")
  private String country;

  @NotBlank(message = "City cannot be empty")
  private String city;

  @NotBlank(message = "Street cannot be empty")
  private String street;

  @Positive(message = "Zip code should be positive")
  private int zipCode;
}
