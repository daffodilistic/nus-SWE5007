package com.nus.project.capstone.game.analytics;

import com.nus.project.capstone.model.persistence.game.GameDataJpaEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.nus.project.capstone.game.web.Tools.*;

public class RandomDataGenerator {

    private GameDataJpaEntities gameData = new GameDataJpaEntities();
    private List<String> teamPool = new ArrayList<>();
    private List<String> school = new ArrayList<>();
    private List<String> country = new ArrayList<>();
    private final String[] outcome = {WIN, DRAW, LOSE};
    private final Random random = new Random();
    private final String[] UpperCaseAlphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private final String[] countryState = {
            "United States, California",
            "Canada, Ontario",
            "Australia, New South Wales",
            "Thailand, Bangkok",
            "India, Maharashtra",
            "Germany, Bavaria",
            "Malaysia, Penang",
            "Japan, Tokyo",
            "China, Jiangsu",
            "Mexico, Mexico City"
    };

    public RandomDataGenerator() {
        for(int i=0; i<30; i++){
            teamPool.add("Team" + i);
            school.add("School" + UpperCaseAlphabet[i % 15]);
            country.add(countryState[i % 10]);
        }

    }

    public GameDataJpaEntities getRandomGameData(String gameName, String yearMonth){
        this.gameData.setId(UUID.randomUUID());
        this.gameData.setGameName(gameName);
        this.gameData.setHostTeam(teamPool.get(random.nextInt(teamPool.size())));
        this.gameData.setHostSchool(school.get(random.nextInt(school.size())));
        this.gameData.setHostLocation(country.get(random.nextInt(country.size())));
        this.gameData.setHostAvgExp(random.nextFloat() * 5.0f);
        this.gameData.setOppoTeam(teamPool.get(random.nextInt(teamPool.size())));
        this.gameData.setOppoSchool(school.get(random.nextInt(school.size())));
        this.gameData.setOppoLocation(country.get(random.nextInt(country.size())));
        this.gameData.setOppoAvgExp(random.nextFloat() * 5.0f);
        if (gameName.equals("game_arena")){
            int hostScore = random.nextInt(281);
            int oppoScore = random.nextInt(281);
            this.gameData.setHostScore(hostScore);
            this.gameData.setOppoScore(oppoScore);
            this.gameData.setGameOutcome(calculateOutcome(hostScore, oppoScore));
        } else {
            this.gameData.setHostScore(-1);
            this.gameData.setOppoScore(-1);
            this.gameData.setGameOutcome(outcome[random.nextInt(outcome.length)]);
        }
        this.gameData.setYearMonth(yearMonth);
        return gameData;
    }
}
