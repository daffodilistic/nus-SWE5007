<template>
  <div class="match">
    <div class="team" :class="{ winner: match.hostScore > match.oppoScore }">
      <span
        v-if="showTrophy && match.hostScore > match.oppoScore"
        class="trophy"
        >🏆<br
      /></span>
      <span
        v-else-if="showStar && match.hostScore > match.oppoScore"
        class="star"
        >⭐<br
      /></span>
      <span
        v-else="showBlank && match.hostScore < match.oppoScore"
        class="blank"
      >
        <br
      /></span>
      {{ match.hostTeamName }}
    </div>
    <div class="score">
      <template v-if="match.gameStatus !== 'done'">
        <input
          type="number"
          v-model="match.enteredHostScore"
          :min="0"
          :disabled="match.gameStatus !== 'ongoing'"
          class="narrow-input"
        />
        &nbsp;-&nbsp;
        <input
          type="number"
          v-model="match.enteredOppoScore"
          :min="0"
          :disabled="match.gameStatus !== 'ongoing'"
          class="narrow-input"
        />
      </template>
      <template v-else-if="match.gameStatus === 'done'">
        {{ match.hostScore }} - {{ match.oppoScore }}
      </template>
    </div>
    <div class="team" :class="{ winner: match.oppoScore > match.hostScore }">
      <span
        v-if="showTrophy && match.oppoScore > match.hostScore"
        class="trophy"
        >🏆<br
      /></span>
      <span
        v-else-if="showStar && match.oppoScore > match.hostScore"
        class="star"
        >⭐<br
      /></span>
      <span
        v-else="showBlank && match.oppoScore < match.hostScore"
        class="blank"
      >
        <br
      /></span>
      {{ match.oppoTeamName }}
    </div>

    <div>
      <template v-if="gameStatusTextMap[match.gameStatus] === 'Not Started'"
        ><br />
        <b-button
          @click="startGame(match.id, 'na')"
          variant="outline-primary"
          class="delete-button"
          v-b-tooltip.hover="'Click to start game for this matchup'"
        >
          <b-icon icon="play-circle"></b-icon>
        </b-button>
      </template>

      <template v-if="match.gameStatus == 'ongoing'"
        ><br />
        <b-button
          @click="submitScore(match.hostTeamId, match.oppoTeamId, match, 'na')"
          variant="outline-primary"
          class="delete-button"
          v-b-tooltip.hover="'Click to submit score for this matchup'"
        >
          <b-icon icon="save"></b-icon>
        </b-button>
      </template>
    </div>
  </div>
</template>

<script>
import Round from "./Round.vue";
import ScoreGA from "./ScoreGA.vue";
import eventBus from "../utils/eventBus.js";
import { delay } from "../utils/utils.js";

export default {
  props: {
    match: Object, // Contains match data (homeTeam, awayTeam, hostScore, oppoScore)
    showStar: Boolean, // Indicates whether to show a star
    showTrophy: Boolean, // Indicates whether to show a trophy
    showBlank: Boolean,
  },
  components: {
    Round,
    ScoreGA,
  },
  data() {
    return {};
  },
  computed: {
    gameStatusTextMap() {
      // Define a mapping of age group values to their corresponding text
      const gameStatusMap = {
        pending: "Not Started",
        ongoing: "In-Progress",
        done: "Completed",
        // Add more entries as needed for other age groups
      };
      return gameStatusMap;
    },
  },
  methods: {
    async submitScore(hostTeamId, oppoTeamId, matchObj, index) {
      eventBus.$emit("submit-score", hostTeamId, oppoTeamId, matchObj, index);
    },

    async startGame(gameId, index) {
      eventBus.$emit("start-game", gameId, index);
      await delay(1000);
      eventBus.$emit("load-elimination-data");
    },

    async checkAdvanceRound(gameId, index) {
      eventBus.$emit("check-advance");
    },
  },
};
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
  width: 120px;
}

/* Style for the winning team */
.winner {
  background-color: #ddedfc; /* Change background color for winning team */
}

/* Style for the trophy and star icons */
.star,
.blank {
  font-size: 20px;
  margin-left: 4px;
}

.trophy {
  font-size: 30px;
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
  font-size: 30px;

  /* Use Google Fonts for the Sports World font */
  font-family: "Tourney", sans-serif;
  display: flex;
  align-items: center;
}

.narrow-input {
  width: 50px;
}

.host-score-td {
  font-size: 20px;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
}
</style>
