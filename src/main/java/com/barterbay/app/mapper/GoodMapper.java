package com.barterbay.app.mapper;

import com.barterbay.app.domain.Good;
import com.barterbay.app.domain.dto.good.GoodCreatedDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoodMapper {

  Good goodDTOToGoodEntity(GoodCreatedDTO goodCreatedDTO);

  GoodCreatedDTO goodEntityToGoodDTO(Good good);
}

