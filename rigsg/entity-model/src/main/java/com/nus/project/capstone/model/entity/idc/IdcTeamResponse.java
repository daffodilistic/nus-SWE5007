package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdcTeamResponse {

    private UUID id;
    private String teamName;
    private String ageGroup;
    private UUID idcGroupId;
    private Integer rankFirstStage;
    private Boolean isQualifiedPromo;
    private Boolean isQualifiedFinal;
    private Boolean isQualifiedFinalSecondStage;
    private UUID teacherId;
    private Set<UUID> userIds;
    private List<UserResponse> userResponses;
    private List<PresentationResponse> presentationResponses;

    public static IdcTeamResponse toIdcTeamResponse(IdcTeamJpaEntities i) {
        return IdcTeamResponse.builder()
                .id(i.getId())
                .teamName(i.getTeamName())
                .ageGroup(i.getAgeGroup())
                .rankFirstStage(i.getRankFirstStage())
                .isQualifiedPromo(i.getIsQualifiedPromo())
                .isQualifiedFinal(i.getIsQualifiedFinal())
                .isQualifiedFinalSecondStage(i.getIsQualifiedFinalSecondStage())
                .teacherId(i.getTeacherId())
                .presentationResponses(i.getPresentations().stream()
                        .map(PresentationResponse::toPresentationResponse).collect(Collectors.toList()))
                .build();
    }
}
