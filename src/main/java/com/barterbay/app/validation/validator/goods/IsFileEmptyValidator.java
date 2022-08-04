package com.barterbay.app.validation.validator.goods;

import com.barterbay.app.validation.validator.FileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class IsFileEmptyValidator implements FileValidator {

  @Override
  public void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalStateException("Cannot upload empty file");
    }
  }
}
