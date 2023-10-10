package com.nus.project.capstone.game.service;

import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameDataJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameDataRepository;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import com.nus.project.capstone.model.persistence.game.GamesRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class GameService {

    private final GamesRepository gamesRepository;
    private final GameDataRepository gameDataRepository;

    @Autowired
    public GameService(GamesRepository gamesRepository, GameDataRepository gameDataRepository) {
        this.gamesRepository = gamesRepository;
        this.gameDataRepository = gameDataRepository;
    }

    public void setUpAndSaveGame() {

        List<GamesJpaEntities> gamesJpaEntitiesList = gamesRepository.findAll();

        for (GamesJpaEntities game : gamesJpaEntitiesList) {
            GameDataJpaEntities gameData = new GameDataJpaEntities();
            gameData.setId(UUID.randomUUID());
            /** game name **/
            gameData.setGameName("game_arena");

            /** host team **/
            val host = game.getGamesTeam().stream()
                    .filter(gameTeam -> gameTeam.getId().equals(game.getGameTeamIdHost())).findFirst();
            if (host.isEmpty()) throw new RuntimeException("No host");
            val h = host.get();
            gameData.setHostTeam(h.getTeamName());

            /** host school **/
            val users = h.getUsers();
            if (users.isEmpty()) {
                gameData.setHostSchool(null);
                gameData.setHostLocation(null);
                gameData.setHostAvgExp((float) 0);
            } else {
                val user = users.stream().findFirst().get();
                val hostSchool = user.getSchoolName();
                gameData.setHostSchool(hostSchool);

                /** host location **/
                val hostLocation = user.getCountry() + "," + user.getState();
                gameData.setHostLocation(hostLocation);

                /** host avg exp **/
                val hostAvgExp = users.stream().mapToDouble(UserJpaEntities::getYearsOfExp)
                        .average()
                        .orElse(0.0);
                gameData.setHostAvgExp((float) hostAvgExp);
            }

            /** host score **/
            gameData.setHostScore(game.getGameScoreHost());

            /** oppo team **/
            val oppo = game.getGamesTeam().stream()
                    .filter(gameTeam -> gameTeam.getId().equals(game.getGameTeamIdOppo())).findFirst();
            if (oppo.isEmpty()) throw new RuntimeException("No oppo");
            val o = oppo.get();
            gameData.setOppoTeam(o.getTeamName());

            /** oppo school **/
            val usersOppo = o.getUsers();
            if (usersOppo.isEmpty()) {
                gameData.setOppoSchool(null);
                gameData.setOppoLocation(null);
                gameData.setOppoAvgExp((float) 0);
            } else {
                val userOppo = usersOppo.stream().findFirst().get();
                val oppoSchool = userOppo.getSchoolName();
                gameData.setOppoSchool(oppoSchool);

                /** oppo location **/
                val oppoLocation = userOppo.getCountry() + "," + userOppo.getState();
                gameData.setOppoLocation(oppoLocation);

                /** oppo avg exp **/
                val oppoAvgExp = usersOppo.stream().mapToDouble(UserJpaEntities::getYearsOfExp)
                        .average()
                        .orElse(0.0);
                gameData.setOppoAvgExp((float) oppoAvgExp);
            }

            /** oppo score **/
            gameData.setOppoScore(game.getGameScoreOppo());

            gameData.setGameOutcome(game.getGameOutcome());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMyyyy", Locale.ENGLISH)
                    .withZone(ZoneId.systemDefault());
            gameData.setYearMonth(formatter.format(game.getDatetime()));
            gameDataRepository.save(gameData);
        }
    }
}
