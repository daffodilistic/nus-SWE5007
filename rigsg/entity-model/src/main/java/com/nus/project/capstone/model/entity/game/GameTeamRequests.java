package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameTeamRequests {

    private UUID id;
    private String teamName;
    private String competitionChoice;
    private String ageGroup;
    private Integer qualificationRoundScore;
    private Integer qualificationRoundPoint;
    private Integer qualificationRoundNumMatchesPlayed;
    private Boolean isQualifiedForElimination;
    private Set<UUID> userIds;
}
