package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jumkid.activity.controller.validation.ValidDateComparison;
import com.jumkid.activity.enums.ActivityStatus;
import com.jumkid.share.controller.dto.GenericDTO;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ValidDateComparison(message = "end date is earlier then start date",
        first = "endDate",
        second = "startDate",
        greaterThenOrEquals = true)
@Data
@EqualsAndHashCode(of = {"activityId"}, callSuper = false)
public class Activity extends GenericDTO {

    @Min(0L)
    private Long activityId;

    @NotBlank(message = "name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 5000)
    private String description;

    private ActivityStatus status;

    private Priority priority;

    @Future
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT_DDMMYYYY_HHMM)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @Future
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT_DDMMYYYY_HHMM)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean autoNotify;

    @Valid
    private ActivityNotification activityNotification;

    @Valid
    private List<ActivityAssignee> activityAssignees;

    @Valid
    private List<ContentResource> contentResources;

    /**
     * This constructor is for lombok builder only since it is subclass of generic DTO
     *
     */
    @Builder
    public Activity(Long activityId, String name, String description, ActivityStatus status,
                    Priority priority, LocalDateTime startDate, LocalDateTime endDate, ActivityNotification activityNotification,
                    String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modificationDate) {
        super(createdBy, creationDate, modifiedBy, modificationDate);
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityNotification = activityNotification;
    }

}
