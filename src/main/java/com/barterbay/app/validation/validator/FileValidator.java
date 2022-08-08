package com.barterbay.app.validation.validator;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {
  void validateFile(MultipartFile file);
}
