package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.entity.idc.PresentationRequests;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private Integer score;
    private String venue;
    private Instant datetime;

    public static PresentationJpaEntities toJpaEntity(PresentationRequests p) {
        return PresentationJpaEntities.builder()
                .id(p.getId() == null ? UUID.randomUUID() : p.getId())
                .score(p.getScore())
                .venue(p.getVenue())
                .datetime(p.getDateTime())
                .build();
    }
}
