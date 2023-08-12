package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamesResponse {

    private UUID id;
    private UUID gameTeamIdA;
    private UUID gameTeamIdB;
    private Integer gameScoreA;
    private Integer gameScoreB;
    private String gameStatus;
    private String gameOutcome;

    public static GamesResponse toGamesResponse(GamesJpaEntities g) {
        return GamesResponse.builder()
                .id(g.getId())
                .gameTeamIdA(g.getGameTeamIdA())
                .gameTeamIdB(g.getGameTeamIdB())
                .gameScoreA(g.getGameScoreA())
                .gameScoreB(g.getGameScoreB())
                .gameStatus(g.getGameStatus())
                .gameOutcome(g.getGameOutcome())
                .build();
    }
}
