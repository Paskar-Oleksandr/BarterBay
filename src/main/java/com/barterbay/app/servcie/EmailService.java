package com.barterbay.app.servcie;

import com.barterbay.app.exception.SendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

  private static final String UTF_8 = "UTF-8";

  private final JavaMailSender javaMailSender;

  @Async
  public void sendEmail(String[] to, String subject, String body) {
    javaMailSender.send(mimeMessage -> createMimeMessageHelper(mimeMessage, to, subject, body));
  }

  @Async
  public void sendEmail(String[] to, String subject, String body, MultipartFile attachment) {
    javaMailSender.send(mimeMessage -> createMimeMessageAttachment(mimeMessage, to, subject, body, attachment));
  }

  private void createMimeMessageHelper(MimeMessage mimeMessage, String[] to, String subject, String text) {
    MimeMessageHelper mimeMessageHelper;
    try {
      mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(text, true);
    } catch (MessagingException e) {
      log.error("Error while sending email");
      throw new SendEmailException(e.getMessage());
    }
  }

  private void createMimeMessageAttachment(MimeMessage mimeMessage, String[] to, String subject, String body, MultipartFile attachment) throws SendEmailException {
    MimeMessageHelper mimeMessageHelper;
    try {
      mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(body);

      if (attachment != null) {
        mimeMessageHelper.addAttachment(attachment.getOriginalFilename(), attachment);
      }
    } catch (MessagingException e) {
      log.error("Error while sending email with attachments");
      throw new SendEmailException(e.getMessage());
    }
  }
}
