package com.nus.project.capstone.model.persistence.game;

import com.nus.project.capstone.model.entity.game.GamesRequests;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
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
    @Column(name = "game_team_id_host")
    private UUID gameTeamIdHost;

    /**
     * opponent
     */
    @Column(name = "game_team_id_oppo")
    private UUID gameTeamIdOppo;
    @Column(name = "game_score_host")
    private Integer gameScoreHost;
    @Column(name = "game_score_oppo")
    private Integer gameScoreOppo;
    private String gameStatus;
    private String gameOutcome;
    private String venue;
    private Instant datetime;
    private String stage;

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
                .gameTeamIdHost(UUID.fromString(g.getGameTeamIdHost()))
                .gameTeamIdOppo(UUID.fromString(g.getGameTeamIdOppo()))
                .gameScoreHost(g.getGameScoreHost())
                .gameScoreOppo(g.getGameScoreOppo())
                .gameStatus(g.getGameStatus())
                .gameOutcome(g.getGameOutcome())
                .venue(g.getVenue())
                .datetime(g.getDatetime())
                .stage(g.getStage())
                .build();
    }

    public GamesJpaEntities updateJpaEntity(GamesRequests u) {
        return GamesJpaEntities.builder()
                .id(u.getId())
                .gameTeamIdHost(u.getGameTeamIdHost() == null ? this.getGameTeamIdHost() : UUID.fromString(u.getGameTeamIdHost()))
                .gameTeamIdOppo(u.getGameTeamIdOppo() == null ? this.getGameTeamIdOppo() : UUID.fromString(u.getGameTeamIdOppo()))
                .gameScoreHost(u.getGameScoreHost() == null ? this.getGameScoreHost() : u.getGameScoreHost())
                .gameScoreOppo(u.getGameScoreOppo() == null ? this.getGameScoreOppo() : u.getGameScoreOppo())
                .gameStatus(u.getGameStatus() == null ? this.getGameStatus() : u.getGameStatus())
                .gameOutcome(u.getGameOutcome() == null ? this.getGameOutcome() : u.getGameOutcome())
                .venue(u.getVenue() == null ? this.getVenue() : u.getVenue())
                .datetime(u.getDatetime() == null ? this.getDatetime() : u.getDatetime())
                .stage(u.getStage() == null ? this.getStage() : u.getStage())
                .build();
    }
}
