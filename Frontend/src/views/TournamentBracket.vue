<template>
  <div class="tournament-bracket">
    <Round :roundMatches="round1" :isSecondRound="true" />
    <Round :roundMatches="round2" :isSecondRound="true" />
    <Round :roundMatches="round3" :isLastRound="true" />
  </div>
</template>

<script>
import Round from './Round.vue';

export default {
  components: {
    Round,
  },
  data() {
    return {
      round1: [
        { id: 1, homeTeam: 'Team A', awayTeam: 'Team B', homeScore: 1, awayScore: 2 },
        { id: 2, homeTeam: 'Team C', awayTeam: 'Team D', homeScore: 3, awayScore: 4 },
        { id: 2, homeTeam: 'Team C', awayTeam: 'Team D', homeScore: 1, awayScore: 2 },
        { id: 2, homeTeam: 'Team C', awayTeam: 'Team D', homeScore: 1, awayScore: 4 },


      ],
      round2: [
        { id: 3, homeTeam: 'Winner 1', awayTeam: 'Winner 2', homeScore: 1, awayScore: 2 },
        { id: 2, homeTeam: 'Team C', awayTeam: 'Team D', homeScore: 3, awayScore: 1 },
      ],
      round3: [
        { id: 4, homeTeam: 'Champion', awayTeam: 'Second Place', homeScore: 1, awayScore: 2 },
      ],
    };
  },
};
</script>

<style scoped>
/* Add styling for the tournament bracket */
.tournament-bracket {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 20px; /* Add margin for spacing */
  justify-content: space-between; /* Evenly distribute space */
  width: 60%; /* Adjust as per your layout requirement */
  margin: 0 auto; /* Center the entire bracket */
  position: relative;
}

/* Styling for arrow lines between rounds*/
.arrow {
  width: 0;
  height: 0;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-left: 15px solid #333; /* Arrow color */
  right: 655px;
  position: absolute; /* Position the arrow absolutely within the container */
  top: 59%; /* Vertically center the arrow within the container */
  transform: translateY(-50%); /* Adjust for vertical centering */
}

/* Create connecting lines using pseudo-elements */


</style>
