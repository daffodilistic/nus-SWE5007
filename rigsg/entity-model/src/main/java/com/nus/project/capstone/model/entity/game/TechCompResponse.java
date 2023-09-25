package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import com.nus.project.capstone.model.persistence.game.TechCompJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TechCompResponse {

    private UUID id;
    private UUID gameTeamIdHost;
    private UUID gameTeamIdOppo;
    private String gameName;
    private String gameStatus;
    private String gameOutcome;
    private String gameVenue;
    private Instant dateTime;

    public static TechCompResponse toTechCompResponse(TechCompJpaEntities t) {
        return TechCompResponse.builder()
                .id(t.getId())
                .gameTeamIdHost(t.getGameTeamIdHost())
                .gameTeamIdOppo(t.getGameTeamIdOppo())
                .gameName(t.getGameName())
                .gameStatus(t.getGameStatus())
                .gameOutcome(t.getGameOutcome())
                .gameVenue(t.getVenue())
                .dateTime(t.getDateTime())
                .build();
    }

}
