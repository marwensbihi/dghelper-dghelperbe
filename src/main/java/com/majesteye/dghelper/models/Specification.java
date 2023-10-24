package com.majesteye.dghelper.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Majd Selmi
 */

@Table(name = "specifications")
@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Specification {
    @Id
    @Column(name = "specification_id")
    private String id;

    @Column(name = "specification")
    private String specification;

    @Column(name = "compliance_percentage")
    private Integer compliancePercentage;

    @Column(name = "priority")
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "control_id")
    private Control control;

    @Column(name = "control_id", insertable = false, updatable = false)
    private String controlId;


  @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Information> informations = new ArrayList<>();
}
