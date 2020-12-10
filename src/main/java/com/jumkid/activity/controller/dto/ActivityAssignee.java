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
@EqualsAndHashCode(of = {"activityAssigneeId"}, callSuper = false)
public class ActivityAssignee {

    @Min(0)
    private Long activityAssigneeId;

    @Min(0)
    private Long activityId;

    @NotBlank
    private String assigneeId;

    @JsonIgnoreProperties
    private String assigneeName;

    @JsonIgnoreProperties
    private String assigneeEmail;

}
