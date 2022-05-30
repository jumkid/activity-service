package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor @NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"activityContentResourceId"}, callSuper = false)
public class ContentResource {

    @Min(0)
    private Long activityContentResourceId;

    @Min(0)
    private Long activityId;

    @NotBlank
    private String contentResourceId;

}
