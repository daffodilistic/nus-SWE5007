package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDataRequest {

    private String gameName;
    private String yearMonth;
    private Integer numSamples;

}
