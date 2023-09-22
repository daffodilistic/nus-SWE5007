package com.nus.project.capstone.model.persistence.game;

import com.nus.project.capstone.model.entity.game.GameGroupRequests;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameGroupJpaEntities {

    @Id
    @Column(nullable = false, name = "game_group_id")
    private UUID id;
    @Column(name = "game_team_id_first")
    private UUID gameTeamIdFirst;
    @Column(name = "game_team_id_second")
    private UUID gameTeamIdSecond;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gameGroup")
    private List<GameTeamJpaEntities> gameTeams;

    public void addToGameTeams(GameTeamJpaEntities team) {
        team.setGameGroup(this);
        gameTeams.add(team);
    }

    public void removeGameTeams(GameTeamJpaEntities team) {
        team.setGameGroup(null);
        gameTeams.remove(team);
    }

    public static GameGroupJpaEntities toJpaEntity(GameGroupRequests g) {
        return GameGroupJpaEntities.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public String toString() {
        return "GameGroupJpaEntities{" +
                "id=" + id +
                ", gameTeamIdFirst=" + gameTeamIdFirst +
                ", gameTeamIdSecond=" + gameTeamIdSecond +
                ", gameTeams=" + gameTeams +
                '}';
    }
}
