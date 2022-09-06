package com.barterbay.app.domain.dto.good;

import com.barterbay.app.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class BaseGoodDto {

  @NotBlank(message = "Good`s name cannot be empty")
  private String goodName;

  @NotBlank(message = "Description cannot be empty")
  private String description;

  @NotNull(message = "Category cannot be null")
  private Category category;

  @NotNull(message = "User id cannot be empty")
  private Long userOwnerId;
}
