package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class Tools {

    public static String WIN = "win";
    public static String LOSE = "lose";
    public static String DRAW = "draw";
    public static String UNKNOWN = "unknown";
    public static String PENDING = "pending";
    public static String ONGOING = "ongoing";
    public static String DONE = "done";
    public static Map<String, Integer> rulesMap = Map.of(WIN, 3, DRAW, 1, LOSE, 0);
    public static int DEFAULT_TEAM_SIZE = 4;
    public static int DEFAULT_TOP = 2;

    public static ResponseEntity<GeneralMessageEntity> genericFailureMessage(){
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data("Update failed. Request contains other illegal params. Pls check.").build());
    }

    public static String calculateOutcome(Integer hostScore, Integer oppoScore){
        return hostScore > oppoScore ? WIN : (hostScore < oppoScore ? LOSE : DRAW);
    }

    public static String flipOutcome(String outcome){
        return outcome.equals(WIN) ? LOSE : (outcome.equals(LOSE) ? WIN : DRAW);
    }

    public static List<GameTeamJpaEntities> getTopNTeams(int n, List<GameTeamJpaEntities> allGameTeams){

        Collections.sort(allGameTeams, new Comparator<GameTeamJpaEntities>() {
            @Override
            public int compare(GameTeamJpaEntities team1, GameTeamJpaEntities team2){
                int pointComparison = Integer.compare(team2.getQualificationRoundPoint(), team1.getQualificationRoundPoint());
                if (pointComparison == 0) {
                    return Integer.compare(team2.getQualificationRoundScore(), team1.getQualificationRoundScore());
                }
                return pointComparison;
            }
        });

        List<GameTeamJpaEntities> topTeamsN = new ArrayList<>();

        for(int i=0; i<allGameTeams.size(); i++){
            if(i < n){
                topTeamsN.add(allGameTeams.get(i));
            } else if (allGameTeams.get(i).getQualificationRoundPoint() == allGameTeams.get(i-1).getQualificationRoundPoint()
            && (allGameTeams.get(i).getQualificationRoundScore() == allGameTeams.get(i-1).getQualificationRoundScore())){
                topTeamsN.add(allGameTeams.get(i)); // same score at cut-off point
            } else {
                break; // exit once the score is not same
            }
        }

        return topTeamsN;
    }

}
