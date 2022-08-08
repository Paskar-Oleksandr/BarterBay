package com.barterbay.app.domain;

import com.barterbay.app.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "good")
public class Good extends AbstractAuditingEntity {

  private static final long serialVersionUID = -2551337806445640103L;

  @Id
  @Column(name = "good_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "good_sequenceGenerator")
  @SequenceGenerator(
    name = "good_sequenceGenerator",
    sequenceName = "good_sequence",
    allocationSize = 1)
  private Long id;

  @NotBlank
  @Column(name = "good_name", nullable = false, length = 100)
  private String goodName;

  @NotBlank
  @Column(nullable = false, length = 1000)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Category category;

  @Version
  private Long version;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "good", cascade = CascadeType.ALL)
  private Set<GoodPhoto> goodPhotos;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Good good = (Good) o;
    return id != null && Objects.equals(id, good.id);
  }

  @Override
  public int hashCode() {
    // should be fixed since we use strategy = GenerationType.SEQUENCE
    // and there`s no unique fields
    return 31;
  }
}
