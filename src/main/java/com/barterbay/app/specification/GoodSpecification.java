package com.barterbay.app.specification;

import com.barterbay.app.domain.Good;
import com.barterbay.app.enumeration.SearchOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class GoodSpecification implements Specification<Good> {

  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Good> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    if (criteria.getOperation().equals(SearchOperation.CONTAINS)) {
      return builder.like(
        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    } else if (criteria.getOperation().equals(SearchOperation.STARTS_WITH)) {
      return builder.like(
        root.get(criteria.getKey()), "%" + criteria.getValue());
    } else if (criteria.getOperation().equals(SearchOperation.EQUALS)) {
      return builder.equal(
        root.get(criteria.getKey()), criteria.getValue());
    }
    return null;
  }
}

