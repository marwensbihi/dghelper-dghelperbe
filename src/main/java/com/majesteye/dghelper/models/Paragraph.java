package com.majesteye.dghelper.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "paragraph")
@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paragraph {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "paragraph_id")
  private Long id;

  @Column(name = "paragraph", length = 4096)
  private String paragraph;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "information_id")
  private Information information;

  @Column(name = "information_id", insertable = false, updatable = false)
  private Long informationId;

}
