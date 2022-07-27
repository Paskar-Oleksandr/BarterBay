package com.barterbay.app.servcie;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.dto.good.GoodCreatedDTO;
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
  public GoodCreatedDTO findGoodById(long goodId) {
    return goodMapper.goodEntityToGoodDTO(goodRepository.findById(goodId).orElseThrow());
  }

  public void updateGoodById(long goodId, GoodCreatedDTO goodCreatedDTO) {
    final Good goodById = goodRepository.findById(goodId).orElseThrow();
    if (goodCreatedDTO.getGoodName() != null) {
      goodById.setGoodName(goodCreatedDTO.getGoodName());
    }
    if (goodCreatedDTO.getDescription() != null) {
      goodById.setDescription(goodCreatedDTO.getDescription());
    }
    if (goodCreatedDTO.getAddress() != null) {
      goodById.setAddress(addressMapper.addressDTOToAddressEntity(goodCreatedDTO.getAddress()));
    }
    if (goodCreatedDTO.getCategory() != null) {
      goodById.setCategory(goodCreatedDTO.getCategory());
    }
  }

  public void deleteGoodById(long goodId) {
    goodRepository.deleteById(goodId);
  }

  public void saveGood(GoodCreatedDTO goodCreatedDTO) {
    final var good = goodMapper.goodDTOToGoodEntity(goodCreatedDTO);
    goodRepository.save(good);
  }
}
