package com.barterbay.app.repository;

import com.barterbay.app.domain.Good;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface GoodRepository extends JpaRepository<Good, Long> {

  @EntityGraph(attributePaths = {
    "address", "goodPhotos"
  },
    type = EntityGraph.EntityGraphType.LOAD)
  @Query("select good from Good good")
  List<Good> findAllWithAddressAndGoodPhotosEagerlyLoaded();

  @EntityGraph(attributePaths = {
    "address", "goodPhotos"
  },
    type = EntityGraph.EntityGraphType.LOAD)
  @Query("select good from Good good where good.id =:goodId")
  Optional<Good> findByIdWithAddressAndGoodPhotosEagerlyLoaded(@Param("goodId") long goodId);
}
