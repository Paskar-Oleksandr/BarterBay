package com.barterbay.app.domain.dto.good;

import com.barterbay.app.domain.dto.address.AddressDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class GoodDTO extends BaseGoodDto {

  private Long id;

  @NotNull(message = "Address cannot be null")
  private AddressDTO address;

  private Set<String> goodPhotos;
}
