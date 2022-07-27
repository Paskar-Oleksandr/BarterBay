package com.barterbay.app.domain.dto.good;

import com.barterbay.app.domain.dto.address.AddressDTO;
import com.barterbay.app.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoodCreatedDTO {
  private Long id;
  @NotBlank(message = "Good`s name cannot be empty")
  private String goodName;
  @NotBlank(message = "Description cannot be empty")
  private String description;
  @NotBlank(message = "Category cannot be empty")
  private Category category;
  private AddressDTO address;
  private Long userOwnerId;
}
