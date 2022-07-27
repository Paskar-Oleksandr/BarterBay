package com.barterbay.app.repository;

import com.barterbay.app.domain.Good;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good, Long> {
  @Override
  @EntityGraph(attributePaths = {"bb_user"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Good> findById(Long aLong);
}
