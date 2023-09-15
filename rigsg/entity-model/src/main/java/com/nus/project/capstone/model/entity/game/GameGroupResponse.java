package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.game.GameGroupJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameGroupResponse {

    private UUID id;
    private UUID gameTeamIdFirst;
    private UUID gameTeamIdSecond;
    private List<GameTeamResponse> gameTeamResponses;

    public static GameGroupResponse toGameGroupResponse(GameGroupJpaEntities g){
        return GameGroupResponse.builder()
                .id(g.getId())
                .gameTeamIdFirst(g.getGameTeamIdFirst())
                .gameTeamIdSecond(g.getGameTeamIdSecond())
                .build();
    }
}
