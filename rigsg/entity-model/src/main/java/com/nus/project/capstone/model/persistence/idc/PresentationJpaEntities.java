package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.entity.idc.PresentationRequests;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "presentation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PresentationJpaEntities {

    @Id
    @Column(nullable = false, name = "presentation_id")
    private UUID id;
    private String stage;
    private Float score;
    private String venue;
    private Instant datetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idc_team_id", referencedColumnName = "idc_team_id")
    private IdcTeamJpaEntities idcTeam;

    public static PresentationJpaEntities toJpaEntity(PresentationRequests p) {
        return PresentationJpaEntities.builder()
                .id(p.getId() == null ? UUID.randomUUID() : p.getId())
                .stage(p.getStage())
                .score(p.getScore())
                .venue(p.getVenue())
                .datetime(Instant.now())
                .build();
    }
}
