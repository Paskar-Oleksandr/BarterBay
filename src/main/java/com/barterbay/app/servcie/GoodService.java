package com.barterbay.app.servcie;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.dto.GoodDTO;
import com.barterbay.app.mapper.AddressMapper;
import com.barterbay.app.mapper.GoodMapper;
import com.barterbay.app.repository.GoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class GoodService {

  private final GoodRepository goodRepository;
  private final GoodMapper goodMapper;
  private final AddressMapper addressMapper;

  @Transactional(readOnly = true)
  public GoodDTO findGoodById(long goodId) {
    return goodMapper.goodEntityToGoodDTO(goodRepository.findById(goodId).orElseThrow());
  }

  public void updateGoodById(long goodId, GoodDTO goodDTO) {
    final Good goodById = goodRepository.findById(goodId).orElseThrow();
    if (goodDTO.getGoodName() != null) {
      goodById.setGoodName(goodDTO.getGoodName());
    }
    if (goodDTO.getDescription() != null) {
      goodById.setDescription(goodDTO.getDescription());
    }
    if (goodDTO.getAddress() != null) {
      goodById.setAddress(addressMapper.addressDTOToAddressEntity(goodDTO.getAddress()));
    }
    if (goodDTO.getCategory() != null) {
      goodById.setCategory(goodDTO.getCategory());
    }
  }

  public void deleteGoodById(long goodId) {
    goodRepository.deleteById(goodId);
  }

  public void saveGood(GoodDTO goodDTO) {
    final var good = goodMapper.goodDTOToGoodEntity(goodDTO);
    goodRepository.save(good);
  }
}
