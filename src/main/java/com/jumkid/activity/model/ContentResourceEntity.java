package com.jumkid.activity.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "activity_content_resource")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContentResourceEntity {

    @Id
    @Column(name = "activity_content_resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityContentResourceId;

    @NotBlank
    @Column(name = "content_resource_id")
    private String contentResourceId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

}
