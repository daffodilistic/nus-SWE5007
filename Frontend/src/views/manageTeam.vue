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
import manageTeamIDC from './manageTeamIDC.vue'; // Import your Tab1 component
import manageTeamGA from './manageTeamGA.vue'; // Import your Tab2 component

export default {
  data() {
    return {
      selectedCompetition: '', // Initialize with an empty string or default tab
      options: [
        { text: 'Innovation Design Challenge' },
        { text: 'Grand Arena' },
      ],
    };
  },
  methods: {
    // Return the corresponding component based on the selected tab
    getComponent(tab) {
      switch (tab) {
        case 'Innovation Design Challenge':
          return manageTeamIDC;
        case 'Grand Arena':
          return manageTeamGA;
        default:
          return null;
      }
    },
  },
};
</script>