package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jumkid.activity.enums.ActivityStatus;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.jumkid.activity.util.Constants.FORMAT_DDMMYYYY_HHMM;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(of = {"activityId"}, callSuper = false)
public class Activity extends GenericDTO{

    @Min(0L)
    private Long activityId;

    @NotBlank(message = "name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 5000)
    private String description;

    private ActivityStatus status;

    private Priority priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT_DDMMYYYY_HHMM)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT_DDMMYYYY_HHMM)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;


    /**
     * This constructor is for lombok builder only since it is subclass of generic DTO
     *
     */
    @Builder
    public Activity(Long activityId, String name, String description, ActivityStatus status,
                    Priority priority, LocalDateTime startDate, LocalDateTime endDate,
                    String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modificationDate) {
        super(createdBy, creationDate, modifiedBy, modificationDate);
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
