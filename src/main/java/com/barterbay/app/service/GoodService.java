package com.barterbay.app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.barterbay.app.domain.Address;
import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.good.CreateGoodDTO;
import com.barterbay.app.domain.dto.good.GoodDTO;
import com.barterbay.app.mapper.GoodMapper;
import com.barterbay.app.mapper.GoodPhotoMapper;
import com.barterbay.app.repository.GoodRepository;
import com.barterbay.app.service.filestorage.StorageProxyService;
import com.barterbay.app.specification.GoodSpecification;
import com.barterbay.app.specification.SearchCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoodService {

  @Value("${aws.s3.bucket-name.photos}")
  private String bucketName;

  @PersistenceContext
  private final EntityManager entityManager;
  private final GoodRepository goodRepository;
  private final GoodMapper goodMapper;
  private final StorageProxyService storageProxyService;
  private final AmazonS3 amazonS3;
  private final GoodPhotoMapper goodPhotoMapper;
  private final ObjectMapper objectMapper;

  public GoodService(EntityManager entityManager, GoodRepository goodRepository,
                     GoodMapper goodMapper, StorageProxyService storageProxyService,
                     @Qualifier("parisS3Client") AmazonS3 amazonS3, GoodPhotoMapper goodPhotoMapper,
                     ObjectMapper objectMapper) {
    this.entityManager = entityManager;
    this.goodRepository = goodRepository;
    this.goodMapper = goodMapper;
    this.storageProxyService = storageProxyService;
    this.amazonS3 = amazonS3;
    this.goodPhotoMapper = goodPhotoMapper;
    this.objectMapper = objectMapper;
  }

  @Transactional(readOnly = true)
  public List<GoodDTO> findAllGoods() {
    final var goods = goodRepository.findAllWithAddressAndGoodPhotosEagerlyLoaded();
    return goodMapper.goodEntityToGoodDTO(goods);
  }

  @Transactional(readOnly = true)
  public GoodDTO findGoodById(long goodId) {
    return goodRepository.findByIdWithAddressAndGoodPhotosEagerlyLoaded(goodId)
      .map(goodMapper::goodEntityToGoodDTO)
      .orElseThrow(() -> {
        throw new EntityNotFoundException("Good with " + goodId + " not found");
      });
  }

  @SneakyThrows
  public void updateGoodById(long goodId, CreateGoodDTO goodDTO) {
    final var address = objectMapper.readValue(goodDTO.getAddress(), Address.class);
    goodRepository.findById(goodId)
      .ifPresentOrElse(good -> {
          goodMapper.updateGood(goodDTO, good);
          good.setAddress(address);
          storageProxyService.bulkImagesDelete(goodDTO.getUserOwnerId(), goodId, amazonS3, bucketName);
          uploadPhotosToS3(goodDTO, good);
        },
        () -> {
          throw new EntityNotFoundException("Good with " + goodId + " not found");
        });
  }

  public void deleteGoodById(long goodId) {
    goodRepository.deleteById(goodId);
  }

  @SneakyThrows
  public void saveGood(CreateGoodDTO goodDTO) {
    final var good = goodMapper.goodDTOToGoodEntity(goodDTO);
    final var address = objectMapper.readValue(goodDTO.getAddress(), Address.class);
    final var userReference = entityManager.getReference(User.class, goodDTO.getUserOwnerId());
    good.setUser(userReference);
    good.setAddress(address);
    final var savedGood = goodRepository.save(good);
    uploadPhotosToS3(goodDTO, savedGood);
  }

  private void uploadPhotosToS3(CreateGoodDTO goodDTO, Good good) {
    if (goodDTO.getPhotos() != null) {
      final var photoUrls = storageProxyService.uploadMultipleFiles(
          goodDTO.getUserOwnerId(), good.getId(),
          goodDTO.getPhotos(), amazonS3, bucketName
        ).stream()
        .map(url -> {
          final var goodPhoto = goodPhotoMapper.urlToEntity(url);
          goodPhoto.setGood(good);
          return goodPhoto;
        })
        .collect(Collectors.toSet());
      good.setGoodPhotos(photoUrls);
    }
  }

  public List<GoodDTO> searchGoods(SearchCriteria searchCriteria) {
    final var specification = new GoodSpecification(searchCriteria);
    return goodRepository.findAll(specification).stream()
      .map(goodMapper::goodEntityToGoodDTO)
      .collect(Collectors.toList());
  }
}
