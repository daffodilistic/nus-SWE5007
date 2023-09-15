package com.nus.project.capstone.model.persistence.game;

import com.nus.project.capstone.model.entity.game.GameTeamRequests;
import com.nus.project.capstone.model.entity.idc.IdcTeamRequests;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcGroupJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameTeamJpaEntities {

    @Id
    @Column(nullable = false, name = "game_team_id")
    private UUID id;
    private String teamName;
    private String ageGroup;
    private Integer qualificationRoundScore;
    private Integer qualificationRoundPoint;
    private Integer qualificationRoundNumMatchesPlayed;
    private Boolean isQualifiedForElimination;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gameTeam")
    private List<UserJpaEntities> users;

    @ManyToMany(mappedBy = "gamesTeam")
    private List<GamesJpaEntities> games = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_group_id", referencedColumnName = "game_group_id")
    private GameGroupJpaEntities gameGroup;

    public void addToUsers(UserJpaEntities user) {
        user.setGameTeam(this);
        users.add(user);
    }

    public static GameTeamJpaEntities toJpaEntity(GameTeamRequests g) {
        return GameTeamJpaEntities.builder()
                .id(g.getId() == null ? UUID.randomUUID() : g.getId())
                .teamName(g.getTeamName())
                .ageGroup(g.getAgeGroup())
                .qualificationRoundScore(g.getQualificationRoundScore())
                .qualificationRoundPoint(g.getQualificationRoundPoint())
                .qualificationRoundNumMatchesPlayed(g.getQualificationRoundNumMatchesPlayed())
                .isQualifiedForElimination(g.getIsQualifiedForElimination())
                .build();
    }

    public GameTeamJpaEntities updateJpaEntity(GameTeamRequests u) {
        return GameTeamJpaEntities.builder()
                .id(u.getId())
                .teamName(u.getTeamName() == null ? this.getTeamName() : u.getTeamName())
                .ageGroup(u.getAgeGroup() == null ? this.getAgeGroup() : u.getAgeGroup())
                .qualificationRoundScore(u.getQualificationRoundScore() == null ? this.getQualificationRoundScore()
                        : u.getQualificationRoundScore())
                .qualificationRoundPoint(u.getQualificationRoundPoint() == null ? this.getQualificationRoundPoint()
                        : u.getQualificationRoundPoint())
                .qualificationRoundNumMatchesPlayed(u.getQualificationRoundNumMatchesPlayed() == null
                        ? this.getQualificationRoundNumMatchesPlayed()
                        : u.getQualificationRoundNumMatchesPlayed())
                .isQualifiedForElimination(u.getIsQualifiedForElimination() == null
                        ? this.getIsQualifiedForElimination()
                        : u.getIsQualifiedForElimination())
                .build();
    }
}
