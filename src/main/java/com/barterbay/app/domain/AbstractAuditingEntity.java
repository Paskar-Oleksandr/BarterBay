package com.barterbay.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified by and created, last modified by date.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = -3302438363141373749L;

// TODO implement after spring security
//  @CreatedBy
//  @Column(name = "created_by", nullable = false, length = 50, updatable = false)
//  @JsonIgnore
//  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  @JsonIgnore
  private LocalDateTime createdDate = LocalDateTime.now();

// TODO implement after spring security
//  @LastModifiedBy
//  @Column(name = "last_modified_by", length = 50)
//  @JsonIgnore
//  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_date")
  @JsonIgnore
  private LocalDateTime lastModifiedDate = LocalDateTime.now();

}
