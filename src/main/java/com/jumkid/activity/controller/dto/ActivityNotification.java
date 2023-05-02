package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jumkid.activity.enums.NotifyTimeUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ActivityNotification {

    @Min(0)
    private Long id;

    @Min(0)
    private Long activityId;

    @Min(1)
    private Integer notifyBefore;

    @NotNull
    private NotifyTimeUnit notifyBeforeUnit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean expired;

}
