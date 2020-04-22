package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
