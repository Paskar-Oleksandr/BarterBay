package com.barterbay.app.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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
  @NotBlank(message = "ZIP code cannot be empty")
  private int zipCode;
  private GoodDTO goodDTO;
}
