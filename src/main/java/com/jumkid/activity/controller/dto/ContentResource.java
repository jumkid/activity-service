package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ContentResource {

    @Min(0)
    private Long id;

    @Min(0)
    private Long activityId;

    @NotBlank
    private String contentResourceId;

}
