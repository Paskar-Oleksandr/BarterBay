package com.barterbay.app.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "good_photo")
public class GoodPhoto extends AbstractAuditingEntity {

  private static final long serialVersionUID = 7352571336650260070L;

  @Id
  @Column(name = "good_photo_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "good_photo_sequenceGenerator")
  @SequenceGenerator(
    name = "good_photo_sequenceGenerator",
    sequenceName = "good_photo_sequence",
    allocationSize = 1)
  private Long id;

  @Column(name = "url_path", nullable = false)
  private String urlPath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "good_id", nullable = false)
  private Good good;

}
