package com.barterbay.app.repository;

import com.barterbay.app.domain.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodRepository extends JpaRepository<Good, Long>, JpaSpecificationExecutor<Good> {
}
