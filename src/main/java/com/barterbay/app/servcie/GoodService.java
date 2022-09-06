package com.barterbay.app.servcie;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.good.GoodDTO;
import com.barterbay.app.mapper.GoodMapper;
import com.barterbay.app.repository.GoodRepository;
import com.barterbay.app.specification.GoodSpecification;
import com.barterbay.app.specification.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GoodService {

  private final GoodRepository goodRepository;
  private final GoodMapper goodMapper;
  @PersistenceContext
  private final EntityManager entityManager;

  @Transactional(readOnly = true)
  public List<GoodDTO> findAllGoods() {
    final var goods = goodRepository.findAll();
    return goodMapper.goodEntityToGoodDTO(goods);
  }

  @Transactional(readOnly = true)
  public GoodDTO findGoodById(long goodId) {
    return goodRepository.findById(goodId)
      .map(goodMapper::goodEntityToGoodDTO)
      .orElseThrow();
  }

  public void updateGoodById(long goodId, GoodDTO goodDTO) {
    goodRepository.findById(goodId)
      .ifPresentOrElse(good -> goodMapper.updateGood(goodDTO, good),
        () -> {
          throw new EntityNotFoundException("Good with " + goodId + " not found");
        });
  }

  public void deleteGoodById(long goodId) {
    goodRepository.deleteById(goodId);
  }

  public Good saveGood(GoodDTO goodDTO) {
    final var good = goodMapper.goodDTOToGoodEntity(goodDTO);
    final var userReference = entityManager.getReference(User.class, goodDTO.getUserOwnerId());
    good.setUser(userReference);
    return goodRepository.save(good);
  }

  public List<GoodDTO> searchGoods(SearchCriteria searchCriteria) {
    final var specification = new GoodSpecification(searchCriteria);
    return goodRepository.findAll(specification).stream()
      .map(goodMapper::goodEntityToGoodDTO)
      .collect(Collectors.toList());
  }
}
