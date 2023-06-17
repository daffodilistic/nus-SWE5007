package com.nus.project.capstone.rigsg.adapters.persistence;

import com.nus.project.capstone.rigsg.adapters.entity.IdcTeamRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "idc_team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdcTeamJpaEntities {

    @Id
    @Column(nullable = false)
    private UUID id;
    private String teamName;
    private String competitionChoice;
    private String ageGroup;
    private UUID idcGroupId;
    private Integer teamScoreFirstStage;
    private Integer teamScoreSecondStage;
    private Boolean isQualifiedSecondStage;

    public static IdcTeamJpaEntities toJpaEntity(IdcTeamRequests i) {
        return IdcTeamJpaEntities.builder()
                .id(i.getId() == null ? UUID.randomUUID() : i.getId())
                .teamName(i.getTeamName())
                .competitionChoice(i.getCompetitionChoice())
                .ageGroup(i.getAgeGroup())
                .teamScoreFirstStage(i.getTeamScoreFirstStage())
                .teamScoreSecondStage(i.getTeamScoreSecondStage())
                .isQualifiedSecondStage(i.getIsQualifiedSecondStage())
                .build();
    }
}
