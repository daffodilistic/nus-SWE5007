package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamesResponse {

    private UUID id;
    private UUID gameTeamIdHost;
    private UUID gameTeamIdOppo;
    private Integer gameScoreHost;
    private Integer gameScoreOppo;
    private String gameStatus;
    private String gameOutcome;
    private String venue;
    private Instant datetime;
    private String stage;

    public static GamesResponse toGamesResponse(GamesJpaEntities g) {
        return GamesResponse.builder()
                .id(g.getId())
                .gameTeamIdHost(g.getGameTeamIdHost())
                .gameTeamIdOppo(g.getGameTeamIdOppo())
                .gameScoreHost(g.getGameScoreHost())
                .gameScoreOppo(g.getGameScoreOppo())
                .gameStatus(g.getGameStatus())
                .gameOutcome(g.getGameOutcome())
                .venue(g.getVenue())
                .datetime(g.getDatetime())
                .stage(g.getStage())
                .build();
    }
}
