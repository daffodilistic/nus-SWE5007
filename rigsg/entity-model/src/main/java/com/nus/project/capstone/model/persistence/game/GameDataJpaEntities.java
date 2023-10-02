package com.nus.project.capstone.model.persistence.game;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "game_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameDataJpaEntities {

    @Id
    @Column(nullable = false, name = "data_id")
    private UUID id;
    private String gameName;
    private String hostTeam;
    private String hostSchool;
    private String hostLocation;
    private Float hostAvgExp;
    private Integer hostScore;
    private String oppoTeam;
    private String oppoSchool;
    private String oppoLocation;
    private Float oppoAvgExp;
    private Integer oppoScore;
    private String gameOutcome;
    private String yearMonth;

}
