package com.nus.project.capstone.game;

import com.nus.project.capstone.game.web.Tools;
import com.nus.project.capstone.model.persistence.game.GameGroupJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Entity;
import java.util.*;

import static com.nus.project.capstone.game.web.Tools.*;

public class GameSimulator {

    private static final Logger logger = LogManager.getLogger(GameSimulator.class);
    public static String ageGroup = "test";

    public Map<UUID, GameTeamJpaEntitiesWithStatus> teamList = new HashMap<>();
    public List<GamesJpaEntities> gameList = new ArrayList<>();
    public GameGroupJpaEntities gameGroup;

    public void createGameGroup(){
        gameGroup = new GameGroupJpaEntities();
        UUID uuid = UUID.randomUUID();
        gameGroup.setId(uuid);
        gameGroup.setGameTeams(new ArrayList<>());
        logger.info("Created group {}", uuid);
    }

    public void createTeams(int num){
        for(int i=0; i < num; i++){
            GameTeamJpaEntitiesWithStatus team = new GameTeamJpaEntitiesWithStatus();
            UUID uuid = UUID.randomUUID();
            team.setId(uuid);
            team.setTeamName("Team" + i);
            team.setAgeGroup(ageGroup);
            team.setQualificationRoundPoint(0);
            team.setQualificationRoundScore(0);
            team.setQualificationRoundNumMatchesPlayed(0);
            team.setIsQualifiedForElimination(false);
            teamList.put(uuid, team);
            logger.info("Created and added team: {}", team.toString());
        }
    }

    public void createGamesForTeams(Map<UUID, GameTeamJpaEntitiesWithStatus> teams){
        List<UUID> teamArr = teams.keySet().stream().toList();
        for(int i=0; i<teamArr.size() -1; i++){
            for(int j=i+1; j<teamArr.size(); j++){
                GamesJpaEntities game = new GamesJpaEntities();
                game.setGameTeamIdHost(teamArr.get(i));
                game.setGameTeamIdOppo(teamArr.get(j));
                game.setGameScoreHost(0);
                game.setGameScoreOppo(0);
                game.setGameStatus(PENDING);
                game.setGameOutcome(UNKNOWN);
                gameList.add(game);
                logger.info("Created game {} Vs {}", teams.get(teamArr.get(i)).getTeamName(), teams.get(teamArr.get(j)).getTeamName());
            }
        }
    }

    public void assignTeamToGroup(Map<UUID, GameTeamJpaEntitiesWithStatus>teams, GameGroupJpaEntities group){
        teams.values().forEach(t -> group.addToGameTeams(t));
        logger.info("Added: {}", group.toString());
    }

    public void startMatch(List<GamesJpaEntities> gameList, Map<UUID, GameTeamJpaEntitiesWithStatus>teams){
        Random r = new Random();
        int low = 10, high = 280;
        gameList.forEach(g -> {
            var hostScore = r.nextInt(high-low) + low;
            var oppoScore = r.nextInt(high-low) + low;
            String outcome = Tools.calculateOutcome(hostScore, oppoScore);
            var teamHost = teams.get(g.getGameTeamIdHost());
            var teamOppo = teams.get(g.getGameTeamIdOppo());

            logger.info("After {} Vs {}:", teamHost.getTeamName(), teamOppo.getTeamName());
            teamHost.setQualificationRoundScore(teamHost.getQualificationRoundScore() + hostScore);
            teamOppo.setQualificationRoundScore(teamOppo.getQualificationRoundScore() + oppoScore);

            teamHost.setQualificationRoundPoint(teamHost.getQualificationRoundPoint() + rulesMap.get(outcome));
            teamOppo.setQualificationRoundPoint(teamOppo.getQualificationRoundPoint() + rulesMap.get(flipOutcome(outcome)));

            teamHost.setQualificationRoundNumMatchesPlayed(teamHost.getQualificationRoundNumMatchesPlayed() + 1);
            teamOppo.setQualificationRoundNumMatchesPlayed(teamOppo.getQualificationRoundNumMatchesPlayed() + 1);

            if (outcome.equals(WIN)) {
                teamHost.setTotalWin(teamHost.getTotalWin() + 1);
                teamOppo.setTotalLoss(teamOppo.getTotalLoss() + 1);
            } else {
                if (outcome.equals(LOSE)) {
                    teamHost.setTotalLoss(teamHost.getTotalLoss() + 1);
                    teamOppo.setTotalWin(teamOppo.getTotalWin() + 1);
                } else {
                    teamHost.setTotalDraw(teamHost.getTotalDraw() + 1);
                    teamOppo.setTotalDraw(teamOppo.getTotalDraw() + 1);
                }
            }
            printOutcome(teams);
        });
    }

    public static void printOutcome(Map<UUID, GameTeamJpaEntitiesWithStatus>teams){
        String div = "|";
        String div2 = "   |   ";
        logger.info("Team" + div + "Matches Played" + div + "Win" + div + "Draw" + div + "Loss" + div + "Total Score" + div + "Points");
        teams.values().forEach(t -> logger.info(
                t.getTeamName() + div2 + t.getQualificationRoundNumMatchesPlayed() + div2 + t.getTotalWin() + div2 + t.getTotalDraw() + div2 + t.getTotalLoss() + div2 + t.getQualificationRoundScore() + div2 + t.getQualificationRoundPoint()
        ));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of teams:");
        int num = scanner.nextInt();
        System.out.println("Enter qualify top X?:");
        int top = scanner.nextInt();

        if(top > num){
            System.out.println("top must <= numer of teams!");
            System.exit(1);
        } else if (num <= 0){
            System.out.println("num must be positive!");
            System.exit(1);
        } else if (top <= 0){
            System.out.println("top must be positive!");
            System.exit(1);
        }

        GameSimulator simulator = new GameSimulator();
        logger.info("Creating group....");
        simulator.createGameGroup();
        logger.info("Creating team....");
        simulator.createTeams(num);
        logger.info("Creating games for teams....");
        simulator.createGamesForTeams(simulator.teamList);
        logger.info("Adding team to group....");
        simulator.assignTeamToGroup(simulator.teamList, simulator.gameGroup);
        logger.info("START MATCH!!!");
        simulator.startMatch(simulator.gameList, simulator.teamList);
        logger.info("END MATCH!!!");
        List<GameTeamJpaEntities> topNTeams = getTopNTeams(top, new ArrayList<>(simulator.teamList.values().stream().map(t -> (GameTeamJpaEntities) t).toList()));
        logger.info("Top {} teams are:", top);
        topNTeams.forEach(t -> {
            t.setIsQualifiedForElimination(true);
            logger.info(t.toString());
        });
    }

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    class GameTeamJpaEntitiesWithStatus extends GameTeamJpaEntities{
        private Integer totalWin = 0;
        private Integer totalLoss = 0;
        private Integer totalDraw = 0;
    }
}
