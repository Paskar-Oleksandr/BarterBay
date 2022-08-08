package com.barterbay.app.validation.validator.goods;

import com.barterbay.app.validation.validator.FileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageSizeValidator implements FileValidator {

  @Override
  public void validateFile(MultipartFile image) {
    final long fiveMB = 5_000_000;
    if (image.getSize() > fiveMB) {
      throw new IllegalStateException("Image size should be less than 5 mb");
    }
  }
}
