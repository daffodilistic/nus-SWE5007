package com.nus.project.capstone.base.adapters.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.base.adapters.persistence.IdcTeamJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdcTeamRequests {

    private UUID id;
    private String teamName;
    private String competitionChoice;
    private String ageGroup;
    private UUID idcGroupId;
    private Integer teamScoreFirstStage;
    private Integer teamScoreSecondStage;
    private Boolean isQualifiedSecondStage;
    private Set<UUID> userIds;
    public static IdcTeamRequests toIdcTeamRequests(IdcTeamJpaEntities i) {
        return IdcTeamRequests.builder()
                .id(i.getId())
                .teamName(i.getTeamName())
                .competitionChoice(i.getCompetitionChoice())
                .ageGroup(i.getAgeGroup())
                .teamScoreFirstStage(i.getTeamScoreFirstStage())
                .teamScoreSecondStage(i.getTeamScoreSecondStage())
                .isQualifiedSecondStage(i.getIsQualifiedSecondStage())
                .build();
    }
}
