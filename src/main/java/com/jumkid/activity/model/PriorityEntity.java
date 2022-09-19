package com.jumkid.activity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "priority")
@Getter @Setter
@NoArgsConstructor
public class PriorityEntity {

    @Id
    @Column(name = "priority_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int priorityId;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "label")
    private String label;

}
