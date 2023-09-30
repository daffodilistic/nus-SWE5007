<template>
  <div class="match">
    <div class="team" :class="{ 'winner': match.gameScoreHost > match.gameScoreOppo }">
      {{ match.hostTeamName }}
      <span v-if="showTrophy && match.gameScoreHost > match.gameScoreOppo" class="trophy">🏆</span>
      <span v-else-if="showStar && match.gameScoreHost > match.gameScoreOppo" class="star">⭐</span>
    </div>
    <div class="score">&nbsp;&nbsp;&nbsp;&nbsp;{{ match.gameScoreHost }} - {{ match.gameScoreOppo }}&nbsp;&nbsp;&nbsp;&nbsp;</div>
    <div class="team" :class="{ 'winner': match.gameScoreOppo > match.gameScoreHost }">
      {{ match.oppoTeamName }}
      <span v-if="showTrophy && match.gameScoreOppo > match.gameScoreHost" class="trophy">🏆</span>
      <span v-else-if="showStar && match.gameScoreOppo > match.gameScoreHost" class="star">⭐</span>
    </div>
  </div>
</template>

<script>
import Round from './Round.vue';
import { GET_ALL_GAMES_BASE_URL,GET_ALL_GAME_TEAM_BASE_URL} from '@/api';
import Vue from 'vue'
import axios from "axios";

export default {
   props: {
    match: Object, // Contains match data (homeTeam, awayTeam, gameScoreHost, gameScoreOppo)
    showStar: Boolean, // Indicates whether to show a star
    showTrophy: Boolean, // Indicates whether to show a trophy
  },
  components: {
    Round,
  },
  data() {
    return {
      round5: [
        { id: 3, homeTeam: 'Winner 1', awayTeam: 'Winner 2', homeScore: 1, awayScore: 2 },
        { id: 2, homeTeam: 'Team C', awayTeam: 'Team D', homeScore: 3, awayScore: 1 },
      ],
      round4: [
        { id: 4, homeTeam: 'Champion', awayTeam: 'Second Place', homeScore: 1, awayScore: 2 },
      ],
      round1:[],
      teamList:[],
    };
  },
  async mounted() {

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      try {
          const response = await axios.get(`${GET_ALL_GAMES_BASE_URL}`, { headers });
          this.allGameData = response.data.data
          this.fetchTeams();
          console.log('wewewewew')
        } catch (error) {
          console.error("Error fetching data:", error);
        }
  },

 methods: {

  async fetchTeams() {

      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      let response = '';
      try {
        response = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, { headers });
        this.teamList = response.data.data

        }catch (error) {
          console.error("Error fetching data:", error);
        }
        const tempArray = [];

      for (const item of this.allGameData) {
          // Create an object with "id" and associated teamArray

          if(item.stage.indexOf('quali') || item.stage.indexOf('Quali')){
            const gameTeamIdHost = this.teamList.find(team => team.id === item.gameTeamIdHost);

            const gameTeamIdOppo = this.teamList.find(team => team.id === item.gameTeamIdOppo);

            const objectWithIdAndOppoArray = {
              id: item.id,
              oppoTeamId: item.gameTeamIdOppo,
              oppoScore: item.gameScoreOppo,
              hostTeamId: item.gameTeamIdHost,
              hostScore: item.gameScoreHost,
              gameStatus : item.gameStatus,
              gameOutcome : item.gameOutcome,
              hostTeamName : gameTeamIdHost.teamName,
              oppoTeamName : gameTeamIdOppo.teamName
            };
            this.round1.push(objectWithIdAndOppoArray);
          }

      }
       console.log(' this.round1', this.round1)
    }
  }
}
</script>

<style scoped>
/* Add styling for the match */
.match {
  display: flex;
  justify-content: space-between;
  margin: 4px 0;
  padding: 10px; /* Add padding to create spacing and border */
  border: 1px solid #ccc; /* Border style */
  border-radius: 4px; /* Optional: Add rounded corners */
  margin-bottom: 40px;
}

.team {
  flex: 1;
  text-align: center;
  font-weight: bold;

}

/* Style for the winning team */
.winner {
  background-color: #ddedfc; /* Change background color for winning team */
}

/* Style for the trophy and star icons */
.trophy, .star {
  font-size: 20px;
  margin-left: 4px;
}

.trophy {
  color: gold; /* Change color of the trophy icon */
}

.star {
  color: yellow; /* Change color of the star icon */
}

.score {
  background-color: #0d0b0bd8; /* Background color for the score */
  color: white; /* Text color for the score */
  padding: 4px 8px; /* Adjust padding for spacing */
  border-radius: 4px; /* Add rounded corners */
  font-size: 20px;

  /* Use Google Fonts for the Sports World font */
  font-family: 'Sports World', sans-serif;
}
</style>
