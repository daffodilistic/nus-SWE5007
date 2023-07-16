package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.entity.base.UserRequests;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdcTeamResponse {

    private UUID id;
    private String teamName;
    private String competitionChoice;
    private String ageGroup;
    private UUID idcGroupId;
    private Integer teamScoreFirstStage;
    private Integer teamScoreSecondStage;
    private Boolean isQualifiedSecondStage;
    private Set<UUID> userIds;
    private List<UserResponse> userResponses;

    public static IdcTeamResponse toIdcTeamResponse(IdcTeamJpaEntities i) {
        return IdcTeamResponse.builder()
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
