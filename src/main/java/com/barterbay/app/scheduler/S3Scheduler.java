package com.barterbay.app.scheduler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.barterbay.app.exception.aws.S3GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.barterbay.app.servcie.filestorage.S3FileStorageService.AMAZON_SERVICE_EXCEPTION_MESSAGE;
import static com.barterbay.app.servcie.filestorage.S3FileStorageService.SDK_CLIENT_EXCEPTION_MESSAGE;

@Service
@Slf4j
public class S3Scheduler {

  @Value("${aws.s3.bucket-name.packages}")
  private String bucketName;

  private final AmazonS3 s3client;

  public S3Scheduler(@Qualifier("virginiaS3Client") AmazonS3 s3client) {
    this.s3client = s3client;
  }

  @Scheduled(cron = "0 0 10 * * *") // every day 10 AM
  public void clearS3PackagesBucketEveryDay() {
    log.info("Cron job has been invoked for {} bucket at {}", bucketName, LocalDateTime.now());
    try {
      final var fileNames = s3client.listObjectsV2(
          new ListObjectsV2Request().withBucketName(bucketName)
        )
        .getObjectSummaries()
        .stream()
        .map(S3ObjectSummary::getKey)
        .toArray(String[]::new);
      if (fileNames.length > 0) {
        s3client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNames));
      }
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    }
  }
}
