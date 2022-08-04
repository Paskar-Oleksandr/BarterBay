package com.barterbay.app.domain.dto.good;

import com.barterbay.app.validation.annotation.GoodImageValidators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateGoodDTO extends BaseGoodDto {

  @NotBlank(message = "Address cannot be null")
  private String address;

  @GoodImageValidators
  private MultipartFile[] photos;
}
