package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamesRequests {

    private UUID id;
    private String gameTeamIdHost;
    private String gameTeamIdOppo;
    private Integer gameScoreHost;
    private Integer gameScoreOppo;
    private String gameStatus;
    private String gameOutcome;
}
