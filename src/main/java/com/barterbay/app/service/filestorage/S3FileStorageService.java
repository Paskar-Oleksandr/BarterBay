package com.barterbay.app.servcie.filestorage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.EncryptedGetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.barterbay.app.exception.aws.S3GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class S3FileStorageService {

  public static final String AMAZON_SERVICE_EXCEPTION_MESSAGE = "The call was transmitted successfully," +
    " but Amazon S3 couldn't process it, so it returned an error response.";
  public static final String SDK_CLIENT_EXCEPTION_MESSAGE = "Amazon S3 couldn't be contacted for a response," +
    " or the client couldn't parse the response from Amazon S3.";

  public Set<URL> multipleUpload(String bucketName, AmazonS3 s3client,
                                 List<Triplet<String, Map<String, String>, MultipartFile>> triplets) {
    log.info("Multiple upload has been triggered for {} bucket", bucketName);
    //todo replace
    ExecutorService executor = Executors.newFixedThreadPool(10);
    final var uploadCallableListTasks = new ArrayList<Callable<URL>>();
    triplets.forEach(triple -> {
        final Callable<URL> urlCallable =
          () -> upload(bucketName, triple.getValue0(), triple.getValue1(), triple.getValue2(), s3client);
        uploadCallableListTasks.add(urlCallable);
      }
    );
    try {
      final var urls = new HashSet<URL>();
      final var futures = executor.invokeAll(uploadCallableListTasks);
      for (Future<URL> future : futures) {
        urls.add(future.get());
      }
      return urls;
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw new S3GeneralException(e);
    }
  }

  public URL upload(String bucketName, String pathToFile,
                    Map<String, String> metadata, MultipartFile file, AmazonS3 s3client) {
    log.info("Upload for single object into {}" +
      " bucket with {} path, and file name {}", bucketName, pathToFile, file.getOriginalFilename());
    final var objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    if (!metadata.isEmpty()) {
      metadata.forEach(objectMetadata::addUserMetadata);
    }
    try {
      s3client.putObject(new PutObjectRequest(bucketName, pathToFile, file.getInputStream(), objectMetadata));
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    } catch (IOException e) {
      throw new S3GeneralException("IO error while uploading object", e);
    }
    s3client.setObjectAcl(bucketName, pathToFile, CannedAccessControlList.PublicRead);
    return s3client.getUrl(bucketName, pathToFile);
  }

  public Set<URL> listingFiles(String bucketName, String prefix, AmazonS3 s3client) {
    log.info("Listing files has been invoked for {} bucket with {} prefix", bucketName, prefix);
    final var urls = new HashSet<URL>();
    final var listObjectsRequest = new ListObjectsV2Request()
      .withBucketName(bucketName)
      .withPrefix(prefix);
    try {
      s3client.listObjectsV2(listObjectsRequest)
        .getObjectSummaries()
        .forEach(s3ObjectSummary -> urls.add(s3client.getUrl(bucketName, s3ObjectSummary.getKey())));
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    }
    return urls;
  }

  public byte[] download(String bucketName, String pathToFile, AmazonS3 s3client) {
    log.info("Download has been invoked for {} bucket with {} path", bucketName, pathToFile);
    try {
      final S3Object object = s3client.getObject(new EncryptedGetObjectRequest(bucketName, pathToFile));
      return IOUtils.toByteArray(object.getObjectContent());
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    } catch (IOException e) {
      throw new S3GeneralException("IO error while downloading object", e);
    }
  }

  public void delete(String bucketName, String pathToFile, AmazonS3 s3client) {
    log.info("Deletion has been invoked for {} bucket with {} path", bucketName, pathToFile);
    try {
      s3client.deleteObject(new DeleteObjectRequest(bucketName, pathToFile));
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    }
  }

  public void bulkDelete(String bucketName, Set<String> pathToFiles, AmazonS3 s3client) {
    log.info("Bulk deletion has been invoked for {} bucket", bucketName);
    try {
      final var deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
        .withKeys(pathToFiles.toArray(new String[0]));
      s3client.deleteObjects(deleteObjectsRequest);
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    }
  }

  // direct renaming of S3 objects is not possible
  public void renameFile(String bucketName, String oldPathToFile,
                         String newPathToFile, AmazonS3 s3client) {
    log.info("Rename file has been invoked for {} bucket. Old path - {}, new path - {}",
      bucketName, oldPathToFile, newPathToFile);
    try {
      s3client.copyObject(new CopyObjectRequest(bucketName, oldPathToFile, bucketName, newPathToFile));
      delete(bucketName, oldPathToFile, s3client);
    } catch (AmazonServiceException e) {
      throw new S3GeneralException(AMAZON_SERVICE_EXCEPTION_MESSAGE, e);
    } catch (SdkClientException e) {
      throw new S3GeneralException(SDK_CLIENT_EXCEPTION_MESSAGE, e);
    }
  }
}
