package com.barterbay.app.specification;

import com.barterbay.app.enumeration.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;

@AllArgsConstructor
@Getter
public class SearchCriteria {

  @Nonnull
  private String key;

  @Nonnull
  private SearchOperation operation;

  @Nonnull
  private Object value;
}
