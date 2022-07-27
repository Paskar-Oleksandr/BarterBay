package com.barterbay.app.domain.dto;

import com.barterbay.app.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoodDTO {
  private Long id;
  @NotBlank(message = "Good`s name cannot be empty")
  private String goodName;
  @NotBlank(message = "Description cannot be empty")
  private String description;
  @NotBlank(message = "Category cannot be empty")
  private Category category;
  private AddressDTO address;
  private UserDTO userOwner;
}
