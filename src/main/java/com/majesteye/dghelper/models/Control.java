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

@Table(name = "controls")
@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Control {
    @Id
    @Column(name = "control_id")
    private String id;

    @Column(name = "control")
    private String control;

    @Column(name = "dimension")
    private String dimension;

    @Column(name = "dependency")
    private String dependency;

    @Column(name = "compliance_percentage")
    private Integer compliancePercentage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @Column(name = "domain_id", insertable = false, updatable = false)
    private String domainId;

    @OneToMany(mappedBy = "control", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specification> specifications = new ArrayList<>();
}
