package com.barterbay.app.validation.validator.goods;

import com.barterbay.app.validation.validator.FileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentSizeValidator implements FileValidator {
  @Override
  public void validateFile(MultipartFile file) {
    final long tenMB = 10_000_000;
    if (file.getSize() > tenMB) {
      throw new IllegalStateException("Document size should be less than 10 mb");
    }
  }
}
