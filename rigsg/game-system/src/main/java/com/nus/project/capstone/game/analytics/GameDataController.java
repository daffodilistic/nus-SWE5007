package com.nus.project.capstone.game.analytics;

import com.nus.project.capstone.game.service.GameService;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GameDataRequest;
import com.nus.project.capstone.model.persistence.game.GameDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/analytics")
public class GameDataController {

    private final GameDataRepository gameDataRepository;
    private final RandomDataGenerator generator;
    private final GameService gameService;

    @Autowired
    public GameDataController(GameDataRepository gameDataRepository, GameService gameService) {
        this.gameDataRepository = gameDataRepository;
        this.gameService = gameService;
        this.generator = new RandomDataGenerator();
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneralMessageEntity> generateRandomData(@RequestBody GameDataRequest g) {
        generator.setNumOfTeams(g.getNumSamples());
        generator.getRandomGameData(g.getGameName(), g.getYearMonth(), gameDataRepository);
        int numSamples = ((g.getNumSamples() * (g.getNumSamples() - 1)) / 2);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Generated %s data", numSamples)).build());
    }

    @GetMapping("/initiate")
    public ResponseEntity<GeneralMessageEntity> initiate() {

        gameService.setUpAndSaveGame();
        gameService.setUpAndSaveTech();

        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data("ok").build());
    }
}
