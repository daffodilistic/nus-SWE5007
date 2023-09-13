package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Tools {

    public static String WIN = "win";
    public static String LOSE = "lose";
    public static String DRAW = "draw";
    public static String UNKNOWN = "unknown";
    public static String PENDING = "pending";
    public static String ONGOING = "ongoing";
    public static String DONE = "done";
    public static Map<String, Integer> rulesMap = Map.of(WIN, 3, DRAW, 1, LOSE, 0);

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
}
