package com.nus.project.capstone.game.analytics;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GameDataRequest;
import com.nus.project.capstone.model.persistence.game.GameDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/analytics")
public class GameDataController {

    private final GameDataRepository gameDataRepository;
    private final RandomDataGenerator generator;

    @Autowired
    public GameDataController(GameDataRepository gameDataRepository) {
        this.gameDataRepository = gameDataRepository;
        this.generator = new RandomDataGenerator();
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneralMessageEntity> generateRandomData(@RequestBody GameDataRequest g) {
        for(int i=0; i<g.getNumSamples(); i++){
            gameDataRepository.save(generator.getRandomGameData(g.getGameName(), g.getYearMonth()));
        }
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Generated %s data", g.getNumSamples())).build());
    }

}
