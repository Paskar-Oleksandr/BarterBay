package com.barterbay.app.validation.validator.goods;

import com.barterbay.app.validation.validator.FileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.apache.http.entity.ContentType.IMAGE_BMP;
import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

@Component
public class ImageMimeTypeValidator implements FileValidator {

  private final List<String> mimeTypes = List.of(
    IMAGE_PNG.getMimeType(), IMAGE_BMP.getMimeType(),
    IMAGE_GIF.getMimeType(), IMAGE_JPEG.getMimeType()
  );

  @Override
  public void validateFile(MultipartFile image) {
    if (!mimeTypes.contains(image.getContentType())) {
      throw new IllegalStateException("FIle uploaded is not an image");
    }
  }
}
