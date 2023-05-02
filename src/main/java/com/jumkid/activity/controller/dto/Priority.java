package com.jumkid.activity.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jumkid.share.service.dto.GenericDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Data
@Builder
@JsonIgnoreProperties({"createdBy", "creationDate", "modifiedBy", "modificationDate"})
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
