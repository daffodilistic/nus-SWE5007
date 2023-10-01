<template>
  <div class="match">
    <div class="team" :class="{ 'winner': match.hostScore > match.oppoScore }">
      {{ match.hostTeamName }}
      <span v-if="showTrophy && match.hostScore > match.oppoScore" class="trophy">🏆</span>
      <span v-else-if="showStar && match.hostScore > match.oppoScore" class="star">⭐</span>
    </div>
    <div class="score">
      <template v-if="match.gameStatus !== 'done'">
        <input type="number" v-model="match.enteredHostScore" :min="0" :disabled="match.gameStatus !== 'ongoing'" class="narrow-input">
        <img src="../assets/Versus_icon.png" alt="Versus">
        <input type="number" v-model="match.enteredOppoScore" :min="0" :disabled="match.gameStatus !== 'ongoing'"
        class="narrow-input">
      </template>
      <template v-else-if="match.gameStatus === 'done'">
        {{ match.hostScore }} - {{ match.oppoScore }}
      </template>
    </div>
    <div class="team" :class="{ 'winner': match.oppoScore > match.hostScore }">
      {{ match.oppoTeamName }}
      <span v-if="showTrophy && match.oppoScore > match.hostScore" class="trophy">🏆</span>
      <span v-else-if="showStar && match.oppoScore > match.hostScore" class="star">⭐</span>
    </div>

    <div>
     <template v-if="gameStatusTextMap[match.gameStatus]==='Not Yet Started'">
        <b-button @click="startGame(match.id)" variant="outline-primary" class="delete-button" v-b-tooltip.hover="'Click to start game for this matchup'">
        <b-icon icon="play-circle"></b-icon>&nbsp;Start
        </b-button>
     </template>
    </div>
  </div>
</template>

<script>
import Round from './Round.vue';
import axios from "axios";
import { UPDATE_GAME_ONGOING_STATUS_BASE_URL,UPDATE_GAME_SCORE_BASE_URL,QUALIFY_GAME_TEAM_BASE_URL} from '@/api';
import Swal from 'sweetalert2';
import Vue from 'vue'
import ScoreGA from './ScoreGA.vue';
import eventBus from './eventBus.js';

export default {
   props: {
    match: Object, // Contains match data (homeTeam, awayTeam, hostScore, oppoScore)
    showStar: Boolean, // Indicates whether to show a star
    showTrophy: Boolean, // Indicates whether to show a trophy
  },
  components: {
    Round,
    ScoreGA
  },
  data() {
    return {
    };
  },
   computed: {
    gameStatusTextMap() {
    // Define a mapping of age group values to their corresponding text
    const gameStatusMap = {
      'pending': 'Not Yet Started',
      'ongoing': 'In-Progress',
      'done': 'Completed',
      // Add more entries as needed for other age groups
    };
    return gameStatusMap;
  }
  },
  methods:{

async startGame(gameId) {

      //eventBus.$emit('start-game', gameId);
       const requestBody = {
        id:gameId
      };

      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
       const response = await axios.put(`${UPDATE_GAME_ONGOING_STATUS_BASE_URL}`,requestBody, { headers });
       console.log(requestBody)
      }

      catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
      eventBus.$emit('load-elimination-data');
  },

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

.narrow-input {
  width: 50px;
}
</style>
