package com.jumkid.activity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "priority")
@Getter @Setter
@NoArgsConstructor
public class PriorityEntity {

    @Id
    @Column(name = "priority_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "label")
    private String label;

}
