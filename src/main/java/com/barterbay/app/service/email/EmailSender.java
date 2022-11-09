package com.barterbay.app.service.email;

import org.springframework.web.multipart.MultipartFile;

public interface EmailSender {
  void sendEmail(String[] to, String subject, String body);

  void sendEmailWithAttachment(String[] to, String subject, String body, MultipartFile attachment);
}
