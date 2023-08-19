<template>
  <b-tabs >
    <b-tab v-for="option in filteredCompetitionChoices" :key="option.id" :title="option.text" @click="selectedCompetition = option.text; loadTeam()">
  <div>
    <br><br>
    <div class="search-container">
      <table>
        <tr>
          <td><p class="h3 mb-2"><b-icon icon="filter" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
          <td>
            <b-form-select v-model="selectedQualification" class="filter-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Stage Name</option>
              </template>
              <option v-for="stageName in distinctStageNames" :key="stageName" :value="stageName">
                {{ stageName }}
              </option>
            </b-form-select>
          </td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td><p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
          <td><input type="text" v-model="searchQuery" placeholder="Search Team Name" class="search-box"></td>
        </tr>
      </table>
    </div>
    <div v-if="teams && teams.length > 0">
    <p>Showing {{ startIndex }} to {{ endIndex }} of {{ totalRecords }} records</p>
    <div class="pagination">
      <button @click="gotoPage(currentPage - 1)" :disabled="currentPage === 1" class="page-button">
        <i class="fas fa-chevron-left icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Previous" icon -->
      </button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button @click="gotoPage(currentPage + 1)" :disabled="currentPage === totalPages" class="page-button">
        <i class="fas fa-chevron-right icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Next" icon -->
      </button>
    </div>
    <table class="main-table">
      <thead>
        <tr>
          <th></th>
          <th>Team Name</th>
          <th>Age Group</th>
          <th>Teacher Name</th>
          <th>Qualification Status</th>
        </tr>
      </thead>
      <tbody v-for="(team, index) in paginatedTeamsByQualification" :key="index">
        <tr :class="{'parent-row': true, 'active-row': activeRow === index}" @click="toggleRow(index)">
          <td>
            <i :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'" class="expand-icon" @click="toggleRow(index)"></i>
          </td>
          <td>{{ team.teamName }}</td>
          <td> {{ ageGroupTextMap[team.ageGroup] }} </td>
          <td>{{ team.teacherName }}</td>
          <td v-if="!team.editing">
            <p v-if="team.isQualifiedFinalSecondStage">Final 2nd Stage</p>
            <p v-else-if="team.isQualifiedFinal">Final 1st Stage</p>
            <p v-else-if="team.isQualifiedPrelim">Preliminary Round</p>
            <p v-else-if="team.isQualifiedPromo">Promotional Round</p>
            <p v-else>Not qualified</p>
          </td>
          <td v-else>
            <div class="form-group select">
              <select v-model="qualification" id="category" class="editing-dropdown">
                <option value="" disabled selected>Select Qualification Status</option>
                <option v-for="option in filteredQualificationOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
              </select>
            </div>
            </td>
          <td>
                <!-- Edit Icon -->
                <b-button @click="editTeam(startIndex + index -1)" variant="outline-primary" class="delete-button">
                  <span v-if="!team.editing"><b-icon icon="pencil"></b-icon></span>
                  <span v-else><b-icon icon="save"></b-icon></span>
                </b-button>
                <!-- Delete Icon -->
                <b-button
                class="delete-button"
                variant="outline-primary"
                @click="deleteTeam(startIndex + index -1)"
                >
                  <b-icon icon="trash"></b-icon>
                </b-button>
              </td>
        </tr>
    <!-- Child rows for metrics -->
    <tr v-if="activeRow === index " class="child-row">
          <td :colspan="10"> <!-- Use colspan to span all columns in the row -->
            <table class="metrics-table">
              <thead>
                <tr>
                  <th></th>
                  <th>Metric Name</th>
                  <th>Metric Weightage</th>
                  <th>Judge Score</th>
                  <th>Judge By</th>
                  <th>Action</th>
                  <th>Score</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(metric, metricIndex) in filteredMetricsForTeam(index)" :key="metricIndex" class="child-row">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td>{{ metric.metricName }}</td>
                  <td>{{ metric.metricWeight * 100 +"%" }}</td>
                  <td><input type="number" v-model="metric.enteredScore" class="form-control" :min="0"></td>
                  <td>{{ }}</td>
                  <td>
                    <!-- Edit Icon -->
                    <b-button @click="editMetric(index)" variant="outline-primary" class="delete-button">
                      <span v-if="!metric.editing"><b-icon icon="pencil"></b-icon></span>
                      <span v-else><b-icon icon="save"></b-icon></span>
                    </b-button>
                  </td>
                  <td v-if="metricIndex === filteredMetricsForTeam(index).length - 3"
                :rowspan="filteredMetricsForTeam(index).length"
                class="score-cell">
              <div class="score">80</div>
            </td>
                </tr>

              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
    <button @click="gotoPage(currentPage - 1)" :disabled="currentPage === 1" class="page-button">
      <i class="fas fa-chevron-left icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Previous" icon -->
    </button>
    <span>Page {{ currentPage }} of {{ totalPages }}</span>
    <button @click="gotoPage(currentPage + 1)" :disabled="currentPage === totalPages" class="page-button">
      <i class="fas fa-chevron-right icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Next" icon -->
    </button>
  </div>
  </div>
  <div v-else>
      <!-- Show a loading message or spinner while the data is being fetched -->
      <div class="loader-container">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>
  </div>
      </b-tab>
    </b-tabs>
</template>

<script>
import axios from "axios";
import { QUALIFY_GAME_TEAM_BASE_URL, QUALIFY_IDC_TEAM_BASE_URL,GET_ALL_IDC_METRIC_BASE_URL, GET_ALL_GAME_TEAM_BASE_URL,GET_ALL_IDC_TEAM_BASE_URL,GET_ALL_GAME_METRIC_BASE_URL } from '@/api';
import { competitionChoiceOptions,qualificationOptions } from "../dropdownOptions";
import token from '/config'

export default {
  head() {
    return {
      link: [
        {
          rel: 'stylesheet',
          href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css',
          integrity: 'sha512-...',
          crossorigin: 'anonymous',
          referrerpolicy: 'no-referrer',
        },
      ],
    }
  },

  data() {
    return {
      qualification: "",
      selectedCompetition: "Innovation Design Challenge",
      metricRequestBody: [],
      totalPages: 1,
      totalRecords: 0,
      selectedQualification: null,
      currentTeamId: "",
      currentTeamName: "",
      selectedUsers: [],
      searchQuery: '',
      teams:[],
      activeRow: null,
      itemsPerPage: 10, // Number of teams per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      userList: [],
      metrics: [],
      newChildRow: {
        firstName: "",
        lastName: "",
      },
    };
  },
  computed: {
    qualificationOptions() {
      return qualificationOptions;
    },
    filteredQualificationOptions() {
    if (this.selectedCompetition === "Game Arena") {
      return this.qualificationOptions.filter(option => option.competitionId === '2');
    } else if (this.selectedCompetition === "Innovation Design Challenge") {
      return this.qualificationOptions.filter(option => option.competitionId === '1');
    }
    return [];
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    distinctStageNames() {
      const stageNames = new Set();
      // Check if this.metrics is defined and is an array before iterating over it
      if (Array.isArray(this.metrics)) {
        this.metrics.forEach((metric) => {
          stageNames.add(metric.stageName);
        });
      }
      return Array.from(stageNames);
    },
    ageGroupTextMap() {
    // Define a mapping of age group values to their corresponding text
    const ageGroupMap = {
      'OC': 'Open Category: 8-15 years old',
      'JC': 'Junior Category: 8-12 years old',
      // Add more entries as needed for other age groups
    };
    return ageGroupMap;
  },

    filteredTeamsByQualification() {
      if (!this.selectedQualification) {
        // If no qualification is selected, return all teams
        return this.filteredTeams;
      }

      // Filter teams based on the selected qualification
      return this.filteredTeams.filter((team) => {
        let result = false;
        switch (this.selectedQualification) {
          case "Promotional Round":
            result = team.isQualifiedPromo;
            break;
          case "Final 1st Stage":
            result = team.isQualifiedFinal;
            break;
          case "Final 2nd Stage":
            result = team.isQualifiedFinalSecondStage;
            break;
          default:
            result = false;
        }

        return result;
      });
    },

    filteredTeams() {
    // If the teams data is not available yet, return an empty array
    if (!this.teams || this.teams.length === 0) {
      return [];
    }

    // If the search query is empty, show all teams
    if (this.searchQuery.trim() === '') {
      return this.teams;
    }

    // Otherwise, filter teams based on the search query
    const query = this.searchQuery.trim().toLowerCase();
    return this.teams.filter((team) => team.teamName.toLowerCase().includes(query));
  },
  totalPagesByQualification() {
    return Math.ceil(this.filteredTeamsByQualification.length / this.itemsPerPage);
  },
  paginatedTeamsByQualification() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.filteredTeamsByQualification.slice(startIndex, endIndex);
  },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecordsByQualification() {
      return this.filteredTeamsByQualification.length;
    },
  },
  async mounted() {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
    try {
      this.teamsData = await axios.get(`${GET_ALL_IDC_TEAM_BASE_URL}`, { headers });
      const allTeams = this.teamsData.data.data;

      // Filter out teams that do not have any of the specified qualifications
      this.teams = allTeams.filter((team) => team.isQualifiedPromo || team.isQualifiedFinal || team.isQualifiedFinalSecondStage);
      this.totalRecords = this.teams.length;
      console.log(teams)

      //this.metricData = await axios.get(`${GET_ALL_IDC_METRIC_BASE_URL}`);
      //this.metrics = this.metricData.data.data;
    } catch (error) {
      console.error("Error fetching users:", error);
    }

  },
  methods: {
    async editTeam(index) {
        const team = this.filteredTeams[index];
        if (team.editing) {
          team.editing = false;
          /*
          'FTS', 'Final 2nd Stage'
          'FFS', 'Final 1st Stage'
          'PRE', 'Preliminary Round'
          'PRO', 'Promotional Round'
          'DIQ', 'Not Qualified'
          */
          let qualifiedPromo = false;
          let qualifiedPrelim = false;;
          let qualifiedFinal = false ;
          let qualifiedFinalSec = false ;
          let requestBody;

          if (this.qualification==='PRO'){
            qualifiedPromo = true;
          }else if(this.qualification==='PRE'){
            qualifiedPromo = true;
            qualifiedPrelim = true;
          }else if(this.qualification==='FFS'){
            qualifiedPromo = true;
            qualifiedPrelim = true;
            qualifiedFinal = true;
          }else if(this.qualification==='FTS'){
            qualifiedPromo = true;
            qualifiedPrelim = true;
            qualifiedFinal = true;
            qualifiedFinalSec = true;
          }
          let response;

           console.log(requestBody)

          const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          };

          try {
             if(this.selectedCompetition === "Game Arena") {
              requestBody = {
                  id: team.id,
                };
               response = await axios.put(`${QUALIFY_GAME_TEAM_BASE_URL}`, requestBody, { headers });
              }else if (this.selectedCompetition === "Innovation Design Challenge") {
                requestBody = {
                  id: team.id,
                  isQualifiedPromo : qualifiedPromo,
                  isQualifiedFinal : qualifiedFinal,
                  isQualifiedFinalSecondStage : qualifiedFinalSec
                };
                response = await axios.put(`${QUALIFY_IDC_TEAM_BASE_URL}`, requestBody, { headers });
              }
            console.log('Response from server:', response.data);
            this.loadTeam();

          } catch (error) {
            console.error('Error editing team:', error);
          }
        } else {
          // Enter editing mode
          team.editing = true;
        }
      },

    async loadTeam() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
      try {
          if (this.selectedCompetition === "Game Arena") {
            this.teamsData = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, { headers });
            const allTeams = this.teamsData.data.data;
            console.log('ga called')
            this.teams = allTeams.filter((team) => team.isQualifiedForElimination);
          }else if (this.selectedCompetition === "Innovation Design Challenge") {
            this.teamsData = await axios.get(`${GET_ALL_IDC_TEAM_BASE_URL}`, { headers });
            console.log('IDC called')
            const allTeams = this.teamsData.data.data;
            this.teams = allTeams.filter((team) => team.isQualifiedPromo || team.isQualifiedFinal || team.isQualifiedFinalSecondStage);
          }
          this.totalRecords = this.teams.length;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
    },

    editMetric(index) {
      // Your existing editMetric method implementation...

      // After editing is done, call the new method to create the JSON request body
      this.createMetricRequestBody();
    },
    createMetricRequestBody() {
      // Clear the existing JSON request body

      // Loop through each metric and add the required data to the array
      const metricsForTeam = this.filteredMetricsForTeam(this.activeRow);
      const metricIdsArray = [];
      const metricScoreArray = [];
      for (const metric of metricsForTeam) {
        metricIdsArray.push(metric.id);
        metricScoreArray.push(metric.enteredScore)
      }

      this.metricRequestBody = {
        metricIds: metricIdsArray,
        metricScores: metricScoreArray
      };

      const requestBodyJson = JSON.stringify(this.metricRequestBody);

      // Print the requestBodyJson in the console
      console.log("RequestBody JSON:", requestBodyJson);
    },

    updateSelectedQualification(qualification) {
      this.selectedQualification = qualification;
      this.currentPage = 1; // Reset to the first page when the qualification is changed
    },
    filteredMetricsForTeam(teamIndex) {
      const team = this.filteredTeamsByQualification[teamIndex];
      const qualification = team.isQualifiedFinalSecondStage ? "Final 2nd Stage" :
                         team.isQualifiedFinal ? "Final 1st Stage" :
                         team.isQualifiedPromo ? "Promotional Round" : null;

      if (!qualification) {
        // If the team doesn't have any of the specified qualifications, return an empty array
        return [];
      }

    // Filter the metrics based on the team's qualification status
    return this.metrics.filter((metric) => metric.stageName === qualification);
  },

   async toggleRow(index) {
  if (this.activeRow === index) {
    this.activeRow = null; // Collapse the row if it's already expanded
  } else {
    this.activeRow = index;
    const team = this.filteredTeamsByQualification[index];
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
    try {
      if (this.selectedCompetition === "Game Arena") {
        this.metricData = await axios.get(`${GET_ALL_GAME_METRIC_BASE_URL}`, { headers });
      } else if (this.selectedCompetition === "Innovation Design Challenge") {
        this.metricData = await axios.get(`${GET_ALL_IDC_METRIC_BASE_URL}`, { headers });
      }
      this.metrics = this.metricData.data.data;
      this.totalRecords = this.metrics.length;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }
},
  gotoPage(page) {
      if (page >= 1 && page <= this.totalPagesByQualification) {
        this.currentPage = page;
      }
    },

      // Method to delete a metricData
      deleteTeam(index) {
        if (confirm("Are you sure you want to delete this metricData?")) {
          this.filteredTeams.splice(index, 1);
        }
      },
  },
  watch: {
    // Watch for changes in the filteredTeamsByQualification computed property
    filteredTeamsByQualification() {
      // Update the totalRecords property when the filtered teams change
      this.totalRecords = this.filteredTeamsByQualification.length;
    },
  },
};
</script>

<style>
/* Main Table Styles */
.main-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 16px;
  text-align: center;
  margin-bottom: 20px;
}

.main-table th,
.main-table td {
  padding: 10px 20px;
  border-bottom: 1px solid #ccc;
  text-align: center;
}

/* Nested Table Styles */
.user-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.user-table th,
.user-table td {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
}

/* Toggle Row Styling */
.nested-row {
  display: table-row;
}

.nested-row td {
  border: none;
  padding: 0;
  vertical-align: middle; /* Center the content vertically within the cell */
}

/* Hover effect for clickable rows */
.main-table tbody tr:hover {
  background-color: #f2f2f2;
  cursor: pointer;
}

.active-row {
  background-color: rgb(218, 234, 253)/* Light blue for active parent row */
}

.child-row {
  background-color: rgb(244, 249, 255);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* Add some top margin for spacing */
}

.page-button {
  padding: 8px 16px;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin: 0 5px;
  background-color: transparent;
}

.search-container {
  display: flex;
  justify-content: flex-end; /* Aligns the search box to the right */
  margin-bottom: 20px;
  margin-right:65px;
}

/* Search Box Styles */
.search-box {
  padding: 8px 16px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* Adjust the width as needed */
}

.page-button .icon {
  background-color: transparent;
  border: none;
  padding: 0; /* Remove padding to make the icons fit better */
  font-size: 16px;
}

.parent-row .expand-icon {
  margin-right: 10px; /* Add some spacing between the icon and text */
  font-size: 16px;
  color: rgb(65, 127, 202); /* Set the color to blue (#3498db) */
}

.main-table th:first-child,
.main-table td:first-child {
  /* Set a fixed width for the new column */
  width: 30px;
}
.parent-row .expand-icon {
  /* Adjust the icon's size, margin, and color as needed */
  font-size: 16px;
  margin-right: 10px;
  color: rgb(65, 127, 202);
}

.delete-button {
  margin-left: 5px;
}

/* Styles for the text box in editing mode */
input.form-control.editing-textbox {
  font-size: 14px;
  min-width: 100px;
}

.text-center {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  text-align: center;
}

.loader-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px; /* Adjust the height as needed */
}

.fa-spinner {
  font-size: 32px; /* Adjust the size as needed */
}

.filter-dropdown {
  font-size: 16px;
  min-width: 200px;
}

.child-row td {
  text-align: center;
}

.child-row .text-center {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.filter-dropdown {
  font-size: 16px;
  min-width: 200px;
  padding: 8px 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
  color: #333;
}

.filter-dropdown:focus {
  outline: none;
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.filter-dropdown::placeholder {
  color: #999;
}

.filter-dropdown option {
  background-color: #fff;
  color: #333;
}

.filter-dropdown option:checked {
  background-color: #007bff;
  color: #fff;
}

.score-cell {
    text-align: center;
    vertical-align: middle;

  }

  .score {
    padding: 20px;
    font-weight: bold;
    font-size: 70px;
    color: #11059c;
  }

.editing-dropdown {
  font-size: 14px; /* Adjust the font size as needed */
  min-width: 200px; /* Set the minimum width of the dropdown */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px 16px;
  background-color: #fff;
  color: #555;
}

/* Optionally, add styles for the dropdown arrow icon */
.editing-dropdown .dropdown-toggle::after {
  font-family: 'Font Awesome'; /* Assuming you're using Font Awesome for icons */
  content: '\f107'; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}
</style>