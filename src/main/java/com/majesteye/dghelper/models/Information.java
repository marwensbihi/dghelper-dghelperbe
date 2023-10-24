package com.majesteye.dghelper.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marwen Sbihi
 */

@Table(name = "information")
@Entity
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Information  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "information_id")
    private Long informationId;

    @Column(name = "information")
    private String information;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specification_id")
    private Specification specification;

    @OneToMany(mappedBy = "information", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @Column(name = "specification_id", insertable = false, updatable = false)
    private String specificationId;

}
