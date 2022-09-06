package com.barterbay.app.mapper;

import com.barterbay.app.domain.GoodPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URL;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GoodPhotoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "urlPath", expression = "java(url.toString())")
  GoodPhoto urlToEntity(URL url);

  Set<GoodPhoto> urlToEntity(Set<URL> url);

  @Mapping(target = "urlPath", source = "url")
  GoodPhoto stringUrlPathToEntity(String url);

  Set<GoodPhoto> stringUrlPathToEntity(Set<String> url);
}
