package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ActivityEntityLink {

    @Min(0)
    private Long id;

    @Min(0)
    private Long activityId;

    @NotBlank
    private String entityId;

    @NotBlank
    private String entityName;

}
