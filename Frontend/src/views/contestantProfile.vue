<template>
  <div>
    <b-tabs v-model="selectedCompetition">
      <b-tab v-for="option in options" :key="option.text" :title="option.text">
        <!-- Render the corresponding component based on the selected tab -->
        <component :is="getComponent(option.text)"></component>
      </b-tab>
    </b-tabs>
  </div>
</template>

<script>
import contestantProfileGA from './contestantProfileGA.vue'; // Import your Tab1 component
import contestantProfileIDC from './contestantProfileIDC.vue'; // Import your Tab2 component
import contestantProfileTA from './contestantProfileTA.vue'; // Import your Tab1 component

export default {
  data() {
    return {
      selectedCompetition: '', // Initialize with an empty string or default tab
      options: [
        { text: 'Innovation Design Challenge' },
        { text: 'Grand Arena' },
        { text: 'Technical Competition' },
      ],
    };
  },
  methods: {
    // Return the corresponding component based on the selected tab
    getComponent(tab) {
      switch (tab) {
        case 'Innovation Design Challenge':
          return contestantProfileIDC;
        case 'Grand Arena':
          return contestantProfileGA;
        case 'Technical Competition':
          return contestantProfileTA;
        default:
          return null;
      }
    },
  },
};
</script>