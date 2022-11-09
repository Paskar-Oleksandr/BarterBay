package com.barterbay.app.service.filestorage;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.barterbay.app.util.Constants.SLASH_DELIMITER;
import static com.barterbay.app.util.S3Util.buildPath;
import static com.barterbay.app.util.S3Util.buildPathWithImageName;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
@Slf4j
//@PreAuthorize("isAuthenticated()")
public class StorageProxyService {

  private final S3FileStorageService s3FileStorageService;

  public StorageProxyService(S3FileStorageService s3FileStorageService) {
    this.s3FileStorageService = s3FileStorageService;
  }

  public URL uploadFileToS3(Long userId, Long goodId, MultipartFile file,
                            AmazonS3 s3client, String bucketName) {
    final var imagePath = buildPathWithImageName(userId, goodId, file.getOriginalFilename());
    final var metadata = new HashMap<String, String>();
    metadata.put(CONTENT_TYPE, file.getContentType());
    return s3FileStorageService.upload(bucketName, imagePath, metadata, file, s3client);
  }

  public Set<URL> getAllFilesURL(Long userId, Long goodId, AmazonS3 s3client, String bucketName) {
    final var prefix = buildPath(userId, goodId);
    return s3FileStorageService.listingFiles(bucketName, prefix, s3client);
  }

  public byte[] downloadFile(Long userId, Long goodId, String imageName,
                             AmazonS3 s3client, String bucketName) {
    final var filePath = buildPathWithImageName(userId, goodId, imageName);
    return s3FileStorageService.download(bucketName, filePath, s3client);
  }

  public Set<URL> uploadMultipleFiles(Long userId, Long goodId, MultipartFile[] files,
                                      AmazonS3 s3client, String bucketName) {
    final var triplets = new ArrayList<Triplet<String, Map<String, String>, MultipartFile>>();
    Arrays.stream(files)
      .forEach(image -> {
        final var imagePath = buildPathWithImageName(userId, goodId, image.getOriginalFilename());
        final var metadata = new HashMap<String, String>();
        metadata.put(CONTENT_TYPE, image.getContentType());
        triplets.add(new Triplet<>(imagePath, metadata, image));
      });
    return s3FileStorageService.multipleUpload(bucketName, s3client, triplets);
  }

  public void deleteImage(Long userId, Long goodId, String imageName,
                          AmazonS3 s3client, String bucketName) {
    final var filePath = buildPathWithImageName(userId, goodId, imageName);
    s3FileStorageService.delete(bucketName, filePath, s3client);
  }

  public void bulkImagesDelete(Long userId, Long goodId, Set<String> imageName,
                               AmazonS3 s3client, String bucketName) {
    final var filesPath = new HashSet<String>();
    imageName.forEach(name -> filesPath.add(buildPathWithImageName(userId, goodId, name)));
    s3FileStorageService.bulkDelete(bucketName, filesPath, s3client);
  }

  public void bulkImagesDelete(Long userId, Long goodId, AmazonS3 s3client, String bucketName) {
    final var urls = getAllFilesURL(userId, goodId, s3client, bucketName).stream()
      .map(url -> {
        final var splitUrl = url.toString().split(SLASH_DELIMITER);
        return splitUrl[splitUrl.length - 1];
      })
      .collect(Collectors.toSet());
    bulkImagesDelete(userId, goodId, urls, s3client, bucketName);
  }
}
