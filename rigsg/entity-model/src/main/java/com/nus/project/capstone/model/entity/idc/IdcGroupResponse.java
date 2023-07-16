package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.idc.IdcGroupJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdcGroupResponse {

    private UUID id;
    private String groupName;
    private List<IdcTeamResponse> idcTeamResponses;

    public static IdcGroupResponse toIdcGroupResponse(IdcGroupJpaEntities g) {
        return IdcGroupResponse.builder()
                .id(g.getId())
                .groupName(g.getGroupName())
                .build();
    }
}