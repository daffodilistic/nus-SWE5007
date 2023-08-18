package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.entity.idc.IdcTeamResponse;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameTeamResponse {

    private UUID id;
    private List<UserResponse> userResponses;
    private String name;
    public static GameTeamResponse toGameTeamResponse(GameTeamJpaEntities g) {
        return GameTeamResponse.builder()
                .id(g.getId())
                .name(g.getTeamName())
                .build();
    }
}
