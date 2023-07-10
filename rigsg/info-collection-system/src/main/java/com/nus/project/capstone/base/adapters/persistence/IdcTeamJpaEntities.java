package com.nus.project.capstone.base.adapters.persistence;

import com.nus.project.capstone.base.adapters.entity.IdcTeamRequests;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "idc_team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Data
@Getter
@Setter
public class IdcTeamJpaEntities {

    @Id
    @Column(nullable = false, name = "idc_team_id")
    private UUID id;
    private String teamName;
    private String competitionChoice;
    private String ageGroup;
    private UUID idcGroupId;
    private Integer teamScoreFirstStage;
    private Integer teamScoreSecondStage;
    private Boolean isQualifiedSecondStage;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "team"
//            , cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<UserJpaEntities> users;

    public void addToUsers(UserJpaEntities user) {
        user.setTeam(this);
        users.add(user);
    }

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

    public IdcTeamJpaEntities updateJpaEntity(IdcTeamRequests u) {
        return IdcTeamJpaEntities.builder()
                .id(u.getId())
                .teamName(u.getTeamName() == null ? this.getTeamName() : u.getTeamName())
                .competitionChoice(u.getCompetitionChoice() == null ? this.getCompetitionChoice()
                        : u.getCompetitionChoice())
                .ageGroup(u.getAgeGroup() == null ? this.getAgeGroup() : u.getAgeGroup())
                .teamScoreFirstStage(u.getTeamScoreFirstStage() == null ? this.getTeamScoreFirstStage()
                        : u.getTeamScoreFirstStage())
                .teamScoreSecondStage(u.getTeamScoreSecondStage() == null ? this.getTeamScoreSecondStage()
                        : u.getTeamScoreSecondStage())
                .isQualifiedSecondStage(u.getIsQualifiedSecondStage() == null ? this.getIsQualifiedSecondStage()
                        : u.getIsQualifiedSecondStage())
                .build();
    }
}
