package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamesRequests {

    private UUID id;
    private String gameTeamIdA;
    private String gameTeamIdB;
    private Integer gameScoreA;
    private Integer gameScoreB;
    private String gameStatus;
    private String gameOutcome;
}
