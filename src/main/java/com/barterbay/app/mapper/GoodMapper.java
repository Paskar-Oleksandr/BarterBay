package com.barterbay.app.mapper;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.GoodPhoto;
import com.barterbay.app.domain.dto.good.CreateGoodDTO;
import com.barterbay.app.domain.dto.good.GoodDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
  uses = {
    GoodPhotoMapper.class
  }, imports = {
  Collectors.class, GoodPhoto.class
})
public interface GoodMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "address", ignore = true)
  Good goodDTOToGoodEntity(CreateGoodDTO goodDTO);

  @Mapping(target = "goodPhotos", expression = "java(good.getGoodPhotos().stream()" +
    ".map(GoodPhoto::getUrlPath)" +
    ".collect(Collectors.toSet()))")
  GoodDTO goodEntityToGoodDTO(Good good);

  List<GoodDTO> goodEntityToGoodDTO(List<Good> good);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "address", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void updateGood(CreateGoodDTO goodDTO, @MappingTarget Good good);
}

