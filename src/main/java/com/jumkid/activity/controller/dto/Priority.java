package com.jumkid.activity.controller.dto;

import com.jumkid.share.controller.dto.GenericDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(of = {"priorityId"}, callSuper = true)
@Data
@Builder
public class Priority extends GenericDTO {

    @Min(0)
    @NotNull
    private int priorityId;

    @NotBlank
    @Size(max = 255)
    private String identifier;

    @NotBlank
    @Size(max = 255)
    private String label;

}
