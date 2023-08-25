package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadRequests {

    private String localFilePath;
    private UUID idcTeamId;

}
