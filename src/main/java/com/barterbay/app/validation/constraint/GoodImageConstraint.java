package com.barterbay.app.validation.constraint;

import com.barterbay.app.annotation.InjectList;
import com.barterbay.app.validation.annotation.GoodImageValidators;
import com.barterbay.app.validation.validator.FileValidator;
import com.barterbay.app.validation.validator.goods.ImageMimeTypeValidator;
import com.barterbay.app.validation.validator.goods.ImageSizeValidator;
import com.barterbay.app.validation.validator.goods.IsFileEmptyValidator;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class GoodImageConstraint implements ConstraintValidator<GoodImageValidators, MultipartFile[]> {

  @InjectList(value = {
    ImageSizeValidator.class, ImageMimeTypeValidator.class, IsFileEmptyValidator.class
  })
  private List<FileValidator> imageValidators;

  @Override
  public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
    if (multipartFiles != null) {
      Arrays.stream(multipartFiles)
        .forEach(image -> imageValidators.forEach(validator -> validator.validateFile(image)));
    }
    return true;
  }
}
