package com.barterbay.app.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

  @Value("${aws.s3.key.access}")
  private String accessKeyId;

  @Value("${aws.s3.key.secret}")
  private String accessKeySecret;

  @Value("${aws.s3.region.virginia}")
  private String s3VirginiaRegionName;

  @Value("${aws.s3.region.paris}")
  private String s3ParisRegionName;

  @Bean(name = "parisS3Client")
  public AmazonS3 getAmazonParisS3Client() {
    final var basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
      .withRegion(s3ParisRegionName)
      .build();
  }

  @Bean(name = "virginiaS3Client")
  public AmazonS3 getAmazonVirginiaS3Client() {
    final var basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
      .withRegion(s3VirginiaRegionName)
      .build();
  }
}
