package com.barterbay.app.mapper;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.dto.good.GoodDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodMapper {

  @Mapping(target = "id", ignore = true)
  Good goodDTOToGoodEntity(GoodDTO goodDTO);

  GoodDTO goodEntityToGoodDTO(Good good);

  List<GoodDTO> goodEntityToGoodDTO(List<Good> good);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void updateGood(GoodDTO goodDTO, @MappingTarget Good good);

}
