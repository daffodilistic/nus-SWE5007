<template>
  <div class="round">
    <div v-for="match in roundMatches" :key="match.id">
      <Match
        :match="match"
        :showStar="isSecondRound"
        :showTrophy="isLastRound"
      />
    </div>
  </div>
</template>

<script>
import Match from "./Match.vue";

export default {
  components: {
    Match,
  },
  props: {
    roundMatches: Array, // Matches for the round
    isFirstRound: Boolean, // Indicates if it's the first round
    isSecondRound: Boolean, // Indicates if it's the second round
    isLastRound: Boolean, // Indicates if it's the last round
  },
};
</script>

<style scoped>
/* Add styling for the round */
.round {
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Additional styling as needed for the round */
</style>
