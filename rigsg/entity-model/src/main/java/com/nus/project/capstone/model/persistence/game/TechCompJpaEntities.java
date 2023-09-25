package com.nus.project.capstone.model.persistence.game;

import com.nus.project.capstone.model.entity.game.GamesRequests;
import com.nus.project.capstone.model.entity.game.TechCompRequests;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tech_comp")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TechCompJpaEntities {

    @Id
    @Column(nullable = false, name = "tech_comp_id")
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

    private String gameName;
    private String gameStatus;
    private String gameOutcome;
    private String venue;
    private Instant dateTime;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "game_team_tech_comp",
            joinColumns = @JoinColumn(name = "tech_comp_id"),
            inverseJoinColumns = @JoinColumn(name = "game_team_id")
    )
    private List<GameTeamJpaEntities> gamesTeam = new ArrayList<>();

    public void addToGamesTeam(GameTeamJpaEntities gameTeam) {
        gamesTeam.add(gameTeam);
    }

    public static TechCompJpaEntities toJpaEntity(TechCompRequests t) {
        return TechCompJpaEntities.builder()
                .id(t.getId() == null ? UUID.randomUUID() : t.getId())
                .gameTeamIdHost(UUID.fromString(t.getGameTeamIdHost()))
                .gameTeamIdOppo(UUID.fromString(t.getGameTeamIdOppo()))
                .gameOutcome(t.getGameOutcome())
                .gameName(t.getGameName())
                .gameStatus(t.getGameStatus())
                .venue(t.getGameVenue())
                .dateTime(t.getDateTime())
                .build();
    }

    public TechCompJpaEntities updateJpaEntity(TechCompRequests u) {
        return TechCompJpaEntities.builder()
                .id(u.getId())
                .gameTeamIdHost(u.getGameTeamIdHost() == null ? this.getGameTeamIdHost() : UUID.fromString(u.getGameTeamIdHost()))
                .gameTeamIdOppo(u.getGameTeamIdOppo() == null ? this.getGameTeamIdOppo() : UUID.fromString(u.getGameTeamIdOppo()))
                .gameName(u.getGameName() == null ? this.getGameName() : u.getGameName())
                .gameStatus(u.getGameStatus() == null ? this.getGameStatus() : u.getGameStatus())
                .gameOutcome(u.getGameOutcome() == null ? this.getGameOutcome() : u.getGameOutcome())
                .venue(u.getGameVenue() == null ? this.getVenue() : u.getGameVenue())
                .dateTime(u.getDateTime() == null ? this.getDateTime() : u.getDateTime())
                .build();
    }
}
