package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TechCompRequests {

    private UUID id;
    private String gameTeamIdHost;
    private String gameTeamIdOppo;
    private String gameName;
    private String gameStatus;
    private String gameOutcome;
    private String venue;
    private Instant datetime;
}
