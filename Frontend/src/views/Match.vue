<template>
  <div class="match">
    <div class="team" :class="{ 'winner': match.homeScore > match.awayScore }">
      {{ match.homeTeam }}
      <span v-if="showTrophy && match.homeScore > match.awayScore" class="trophy">🏆</span>
      <span v-else-if="showStar && match.homeScore > match.awayScore" class="star">⭐</span>
    </div>
    <div class="score">&nbsp;&nbsp;&nbsp;&nbsp;{{ match.homeScore }} - {{ match.awayScore }}&nbsp;&nbsp;&nbsp;&nbsp;</div>
    <div class="team" :class="{ 'winner': match.awayScore > match.homeScore }">
      {{ match.awayTeam }}
      <span v-if="showTrophy && match.awayScore > match.homeScore" class="trophy">🏆</span>
      <span v-else-if="showStar && match.awayScore > match.homeScore" class="star">⭐</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    match: Object, // Contains match data (homeTeam, awayTeam, homeScore, awayScore)
    showStar: Boolean, // Indicates whether to show a star
    showTrophy: Boolean, // Indicates whether to show a trophy
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
