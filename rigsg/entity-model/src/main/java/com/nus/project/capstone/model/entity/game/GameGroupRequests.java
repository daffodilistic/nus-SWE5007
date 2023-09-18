package com.nus.project.capstone.model.entity.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameGroupRequests {

    private UUID id;
    private UUID gameTeamIdFirst;
    private UUID gameTeamIdSecond;
    private Set<UUID> gameTeamIds;

}
