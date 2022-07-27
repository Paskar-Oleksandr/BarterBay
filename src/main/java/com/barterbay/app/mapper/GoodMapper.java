package com.barterbay.app.mapper;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.dto.GoodDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoodMapper {

  Good goodDTOToGoodEntity(GoodDTO goodDTO);

  GoodDTO goodEntityToGoodDTO(Good good);
}

