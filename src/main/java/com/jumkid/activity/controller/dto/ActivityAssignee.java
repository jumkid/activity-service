package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor @NoArgsConstructor
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
