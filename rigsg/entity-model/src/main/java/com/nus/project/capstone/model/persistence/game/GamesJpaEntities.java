package com.nus.project.capstone.model.persistence.game;

import com.nus.project.capstone.model.entity.game.GameTeamRequests;
import com.nus.project.capstone.model.entity.game.GamesRequests;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GamesJpaEntities {

    @Id
    @Column(nullable = false, name = "game_id")
    private UUID id;

    /**
     * host
     */
    @Column(name = "game_team_id_a")
    private UUID gameTeamIdA;

    /**
     * opponent
     */
    @Column(name = "game_team_id_b")
    private UUID gameTeamIdB;
    @Column(name = "game_score_a")
    private Integer gameScoreA;
    @Column(name = "game_score_b")
    private Integer gameScoreB;
    private String gameStatus;
    private String gameOutcome;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "game_team_games",
            joinColumns = @JoinColumn(name = "games_id"),
            inverseJoinColumns = @JoinColumn(name = "game_team_id")
    )
    private List<GameTeamJpaEntities> gamesTeam = new ArrayList<>();

    public void addToGamesTeam(GameTeamJpaEntities gameTeam) {
        gamesTeam.add(gameTeam);
    }

    public static GamesJpaEntities toJpaEntity(GamesRequests g) {
        return GamesJpaEntities.builder()
                .id(g.getId() == null ? UUID.randomUUID() : g.getId())
                .gameTeamIdA(UUID.fromString(g.getGameTeamIdA()))
                .gameTeamIdB(UUID.fromString(g.getGameTeamIdB()))
                .gameScoreA(g.getGameScoreA())
                .gameScoreB(g.getGameScoreB())
                .gameStatus(g.getGameStatus())
                .gameOutcome(g.getGameOutcome())
                .build();
    }
}
