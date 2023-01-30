package com.jumkid.activity.controller.dto;

import com.jumkid.share.service.dto.GenericDTO;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Data
@Builder
public class Priority extends GenericDTO {

    @Min(0)
    @NotNull
    private int id;

    @NotBlank
    @Size(max = 255)
    private String identifier;

    @NotBlank
    @Size(max = 255)
    private String label;

}
