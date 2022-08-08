package com.barterbay.app.util;

import lombok.experimental.UtilityClass;

import static com.barterbay.app.util.Constants.SLASH_DELIMITER;

@UtilityClass
public class S3Util {

  public static String buildPathWithImageName(Long userId, Long goodId, String imageName) {
    return new StringBuilder()
      .append("user-id-")
      .append(userId)
      .append(SLASH_DELIMITER)
      .append("good-id-")
      .append(goodId)
      .append(SLASH_DELIMITER)
      .append(imageName)
      .toString();
  }

  public static String buildPath(Long userId, Long goodId) {
    return new StringBuilder()
      .append("user-id-")
      .append(userId)
      .append(SLASH_DELIMITER)
      .append("good-id-")
      .append(goodId)
      .toString();
  }
}
