package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jumkid.activity.enums.NotifyTimeUnit;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@EqualsAndHashCode(of = {"activityNotificationId"}, callSuper = false)
public class ActivityNotification {

    @Min(0)
    private Long activityNotificationId;

    @Min(1)
    private Integer notifyBefore;

    @NotNull
    private NotifyTimeUnit notifyBeforeUnit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean expired;

}
