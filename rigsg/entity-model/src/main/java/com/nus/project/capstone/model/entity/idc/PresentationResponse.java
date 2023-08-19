package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import com.nus.project.capstone.model.persistence.idc.PresentationJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PresentationResponse {

    private UUID id;
    private String stage;
    private Integer score;
    private String venue;
    private Instant dateTime;

    public static PresentationResponse toPresentationResponse(PresentationJpaEntities p) {
        return PresentationResponse.builder()
                .id(p.getId())
                .stage(p.getStage())
                .score(p.getScore())
                .venue(p.getVenue())
                .dateTime(p.getDatetime())
                .build();
    }
}
