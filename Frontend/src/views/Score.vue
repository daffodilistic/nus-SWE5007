<template>
  <div>
    <!-- show download START-->
    <b-modal
      v-model="showDownloadModal"
      modal-class="custom-modal"
      title="Download File"
      hide-footer
    >
      <!-- List of users to be displayed inside the modal -->
      <table class="modal-table">
        <thead>
          <tr>
            <th>Filename</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="downloadFileList.length === 0">
            <tr>
              <td colspan="5"><br /><br />No records available.</td>
            </tr>
            <tr>
              <br /><br />
            </tr>
          </template>
          <template v-else>
            <tr v-for="(file, fileIndex) in downloadFileList" :key="fileIndex">
              <td>{{ file }}</td>
              <td>
                <b-button
                  id="downloadFile"
                  @click="downloadFile(file)"
                  variant="outline-primary"
                  class="delete-button"
                >
                  <b-icon icon="cloud-download"></b-icon>
                </b-button>
              </td>
            </tr>
            <tr>
              <br /><br />
            </tr>
          </template>
        </tbody>
      </table>
    </b-modal>
    <!-- show download Modal END-->
    <!-- Show Member Modal START-->
    <b-modal
      v-model="showMemberModal"
      modal-class="modal"
      hide-footer
      id="showUserModal"
      title="Mark Attendance"
    >
      <!-- List of users to be displayed inside the modal -->
      <table class="modal-table">
        <thead>
          <tr>
            <th>S/No</th>
            <th>User Name</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Attendance</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, userIndex) in userList" :key="user.id">
            <td>{{ userIndex + 1 }}</td>
            <td>{{ user.userName }}</td>
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastName }}</td>

            <td>
              <b-button
                id="edit-button"
                @click="markAttendance(user.id, user.isRegistered)"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="
                  'Click to update attendance for this participant!'
                "
              >
                <span v-if="user.isRegistered === true"
                  ><b-icon icon="check-lg"></b-icon
                ></span>
                <span v-else><b-icon icon="x-lg"></b-icon></span>
              </b-button>
            </td>
          </tr>
          <tr></tr>
        </tbody>
      </table>
    </b-modal>
    <!-- Show Member Modal END-->
    <div class="search-container">
      <table>
        <tr>
          <td>
            <p class="h3 mb-2">
              <b-icon icon="filter" style="color: rgb(65, 127, 202)"></b-icon>
            </p>
          </td>
          &nbsp;
          <td>
            <select
              name="game-type"
              class="filter-dropdown"
              v-model="selectedQualification"
            >
              <option value="" disabled selected>Select Stage Name</option>
              <!-- Use v-for to loop through competitionChoiceOptions -->
              <option
                v-for="option in filteredQualificationOptions"
                :value="option.value"
                :key="option.value"
              >
                {{ option.text }}
              </option>
            </select>
          </td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td>
            <p class="h3 mb-2">
              <b-icon icon="search" style="color: rgb(65, 127, 202)"></b-icon>
            </p>
          </td>
          &nbsp;
          <td>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search Team Name"
              class="search-box"
            />
          </td>
        </tr>
      </table>
    </div>
    <table class="instruction-table">
      <tr>
        <u>Instructions :</u>
      </tr>
      <tr>
        1. Select the stage to be scored using the 'Select Stage Name' dropdown
        box.
      </tr>
      <tr>
        2. Click on
        <b-icon icon="card-checklist"></b-icon>
        to mark team attendance.
      </tr>
      <tr>
        3. Click on
        <b-icon icon="folder2-open"></b-icon>
        to view team submission.
      </tr>
      <tr>
        4. Click on
        <i :class="'fas fa-plus'" class="expand-icon"></i>
        to view scoring matrics of the team & follow the steps.
      </tr>
      <tr>
        5. Repeat steps 1 to 3 for all teams.
      </tr>
    </table>
    <div v-if="teams && teams.length > 0">
      <p>Showing {{ totalRecords }} records</p>

      <table class="main-table">
        <thead>
          <tr>
            <th></th>
            <th>S/No</th>
            <th>Team Name</th>
            <th>Age Group</th>
            <th>Score</th>
            <th>Qualification Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody
          v-for="(team, index) in filteredTeamsByQualification"
          :key="index"
        >
          <tr
            :class="{ 'parent-row': true, 'active-row': activeRow === index }"
            @click="toggleRow(index)"
          >
            <td>
              <i
                :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'"
                class="expand-icon"
                @click="toggleRow(index)"
              ></i>
            </td>
            <td>{{ startIndex + index }}</td>
            <td>{{ team.teamName }}</td>
            <td>{{ ageGroupTextMap[team.ageGroup] }}</td>
            <td v-if="team.presentationResponses.length === 0">Not Scored</td>
            <td
              v-else
              v-for="(response, index) in team.presentationResponses"
              :key="index"
            >
              <span
                v-if="response.stage === getQualificationStatus(team)"
                :class="{
                  'green-score': response.score > 50,
                  'red-score': response.score <= 50,
                }"
              >
                {{ response.score }}
              </span>
            </td>
            <td>
              {{ getQualificationStatus(team) }}
            </td>

            <td>
              <b-button
                id="viewDownload"
                @click="showTeam(team.id)"
                variant="outline-primary"
                v-b-tooltip.hover="'Click to view team members'"
              >
                <b-icon icon="card-checklist"></b-icon> </b-button
              >&nbsp;
              <b-button
                id="viewDownload"
                @click="viewDownload(team.teamName)"
                variant="outline-primary"
                v-b-tooltip.hover="'Click to view team submission'"
              >
                <b-icon icon="folder2-open"></b-icon>
              </b-button>
            </td>
          </tr>
          <!-- Child rows for metrics -->
          <tr v-if="activeRow === index" class="child-row">
            <td :colspan="10">
              <!-- Use colspan to span all columns in the row -->
              <table class="metrics-table">
                <thead>
                  <tr>
                    <th></th>
                    <th>Metric Name</th>
                    <th>Metric Weightage</th>
                    <th>
                      <span class="step-text">Step 1:</span> <br />
                      <span class="grey-font"
                        >Enter Score <br />(out of 100)</span
                      >
                    </th>
                    <th>
                      <span class="step-text">Step 2:</span> <br />
                      <span class="grey-font"> Preview Score</span>
                    </th>
                    <th>
                      <span class="step-text">Step 3:</span> <br />
                      <span class="grey-font"> Update Qualification</span>
                    </th>
                    <th>
                      <span class="step-text">Step 4:</span> <br />
                      <span class="grey-font"> Submit</span>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="(metric, metricIndex) in filteredMetricsForTeam(
                      index
                    )"
                    :key="metricIndex"
                    class="child-row"
                  >
                    <td>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>{{ metric.metricName }}</td>
                    <td>{{ metric.metricWeight * 100 + "%" }}</td>
                    <td>
                      <input
                        type="number"
                        v-model="metric.enteredScore"
                        class="form-control"
                        :min="0"
                      />
                    </td>
                    <td
                      v-if="
                        metricIndex === filteredMetricsForTeam(index).length - 3
                      "
                      :rowspan="filteredMetricsForTeam(index).length"
                      class="score-cell"
                    >
                      <b-button
                        id="previewScore"
                        @click="previewScore(index)"
                        variant="outline-primary"
                        class="delete-button"
                        v-b-tooltip.hover="'Click to preview score'"
                      >
                        <b-icon icon="calculator"></b-icon>
                      </b-button>
                      <div class="score">{{ calculatedScore }}</div>
                    </td>
                    <td
                      v-if="
                        metricIndex === filteredMetricsForTeam(index).length - 3
                      "
                      :rowspan="filteredMetricsForTeam(index).length"
                    >
                      <div class="form-group select">
                        <select
                          v-model="qualification"
                          id="category"
                          class="editing-dropdown"
                        >
                          <option value="" disabled selected>
                            Select Qualification Status
                          </option>
                          <option
                            v-for="option in filteredQualificationOptions"
                            :value="option.value"
                            :key="option.value"
                          >
                            {{ option.text }}
                          </option>
                        </select>
                      </div>
                    </td>

                    <td
                      v-if="
                        metricIndex === filteredMetricsForTeam(index).length - 3
                      "
                      :rowspan="filteredMetricsForTeam(index).length"
                    >
                      <b-button
                        id="saveScore"
                        @click="editMetric(index)"
                        variant="outline-primary"
                        class="delete-button"
                        v-b-tooltip.hover="
                          'Click to submit score and qualification for this team'
                        "
                      >
                        <b-icon icon="save"></b-icon>
                      </b-button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <!-- Show a loading message or spinner while the data is being fetched -->
      <div class="loader-container">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { api } from "../api";
import { competitionChoiceOptions, stageNameOptions } from "../dropdownOptions";
import Vue from "vue";
import VueSweetalert2 from "vue-sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
Vue.use(VueSweetalert2);

export default {
  name: "Page1",
  head() {
    return {
      link: [
        {
          rel: "stylesheet",
          href: "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css",
          integrity: "sha512-...",
          crossorigin: "anonymous",
          referrerpolicy: "no-referrer",
        },
      ],
    };
  },

  data() {
    return {
      calculatedScore: null,
      qualification: "",
      metricRequestBody: [],
      totalRecords: 0,
      selectedQualification: "",
      searchQuery: "",
      teams: [],
      presentations: [],
      activeRow: null,
      itemsPerPage: 10, // Number of teams per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      metrics: [],
      showDownloadModal: false,
      downloadFileList: [],
      teamToScore: "",
      showMemberModal: false,
      currentTeamID: "",
      userList: [],
    };
  },
  computed: {
    totalRecordsModal() {
      return this.userList.length;
    },
    paginatedModalUserList() {
      const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
      const endIndex = startIndex + this.itemsPerPageModal;
      return this.userList.slice(startIndex, endIndex);
    },
    stageNameOptions() {
      return stageNameOptions;
    },
    filteredQualificationOptions() {
      return this.stageNameOptions.filter(
        (option) => option.competitionId === "1"
      );
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },

    ageGroupTextMap() {
      // Define a mapping of age group values to their corresponding text
      const ageGroupMap = {
        OC: "Open Category: 8-15 years old",
        JC: "Junior Category: 8-12 years old",
        // Add more entries as needed for other age groups
      };
      return ageGroupMap;
    },
    filteredTeamsByQualification() {
      if (!this.selectedQualification) {
        // If no qualification is selected, return only not qualified teams.

        return this.filteredTeams.filter((team) => {
          let result = false;
          result = !team.isQualifiedPromo;
          return result;
        });
      }

      //Filter teams based on the selected qualification
      return this.filteredTeams.filter((team) => {
        let result = false;
        switch (this.selectedQualification) {
          case "Promotional Round":
            result =
              team.isQualifiedPromo &&
              !team.isQualifiedFinal &&
              !team.isQualifiedFinalSecondStage;
            break;
          case "Final 1st Stage":
            result = team.isQualifiedFinal && !team.isQualifiedFinalSecondStage;
            break;
          case "Final 2nd Stage":
            result = team.isQualifiedFinalSecondStage;
            break;
          case "Preliminary Round":
            result = !team.isQualifiedPromo;
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
      if (this.searchQuery.trim() === "") {
        return this.teams;
      }

      // Otherwise, filter teams based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.teams.filter((team) =>
        team.teamName.toLowerCase().includes(query)
      );
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
  },
  async mounted() {
    let token = "";
    if (
      Vue.$keycloak &&
      Vue.$keycloak.token &&
      Vue.$keycloak.token.length > 0
    ) {
      token = Vue.$keycloak.token;
    } else {
      token = "mockedToken"; //for unit test
    }
    const headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    };
    try {
      this.teamsData = await axios.get(`${api.GET_ALL_IDC_TEAM_BASE_URL}`, {
        headers,
      });
      const allTeams = this.teamsData.data.data;

      // Filter out teams that do not have any of the specified qualifications
      //this.teams = allTeams.filter((team) => team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage);
      this.teams = allTeams;
      this.presentations = teams.presentationResponses;
      this.totalRecords = this.teams.length;
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    async markAttendance(userId, userRegStatus) {
      console.log("userId", userId, "userRegStatus", userRegStatus);
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      let isRegistered = "false";
      if (userRegStatus) {
        //true
        isRegistered = "false";
      } else {
        isRegistered = "true";
      }

      const requestBody = {
        id: userId,
        isRegistered: isRegistered,
      };

      console.log("requestBody", requestBody);

      try {
        const response = await axios.post(
          `${api.MARK_ATTENDANCE_BASE_URL}`,
          requestBody,
          {
            headers,
          }
        );
        this.showTeam(this.currentTeamID);
        console.log("this.currentTeamID", this.currentTeamID);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    async showTeam(teamId) {
      this.currentTeamID = teamId;
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`${api.GET_ALL_USER_INFO_BASE_URL}`, {
          headers,
        });

        const allUsers = response.data.data;
        this.userList = allUsers.filter(
          (record) =>
            !record.hasOwnProperty("gameTeam") && record.idcTeam === teamId
        );
        this.showMemberModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    async viewDownload(teamName) {
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`${api.VIEW_ALL_FILES_BASE_URL}`, {
          headers,
        });
        const originalArray = response.data.data;
        const prefix = teamName;

        // Remove the "participants/" prefix from each item
        const interimArray = originalArray.map((item) => {
          // Use string manipulation to remove the prefix
          return item.replace("participants/", "");
        });

        this.downloadFileList = interimArray.filter((item) => {
          const parts = item.split("-");
          if (parts.length > 1) {
            // Check if the first part of the filename matches the prefix
            return parts[0] === prefix;
          }
          return false;
        });
        this.showDownloadModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    goToTab(route) {
      this.$router.push(route);
    },
    getQualificationStatus(team) {
      let qualificationStatus;

      if (team.isQualifiedFinalSecondStage) {
        qualificationStatus = "Final 2nd Stage";
      } else if (team.isQualifiedFinal) {
        qualificationStatus = "Final 1st Stage";
      } else if (team.isQualifiedPromo) {
        qualificationStatus = "Promotional Round";
      } else {
        qualificationStatus = "Preliminary Round";
      }
      return qualificationStatus;
    },

    async loadTeam() {
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        this.teamsData = await axios.get(`${api.GET_ALL_IDC_TEAM_BASE_URL}`, {
          headers,
        });

        this.teams = this.teamsData.data.data;

        //this.teams = allTeams.filter((team) => team.isQualifiedPromo || team.isQualifiedFinal || team.isQualifiedFinalSecondStage);

        this.totalRecords = this.teams.length;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },

    async editMetric(index) {
      let token = "";

      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      // Loop through each metric and add the required data to the array
      const metricsForTeam = this.filteredMetricsForTeam(this.activeRow);
      const team = this.filteredTeams[index];

      const metricIdsArray = [];
      const metricScoreArray = [];

      /*
      'FTS', 'Final 2nd Stage'
      'FFS', 'Final 1st Stage'
      'PRO', 'Promotional Round'
      'DIQ', 'Not Qualified'
       */
      let qualifiedPromo = false;
      let qualifiedFinal = false;
      let qualifiedFinalSec = false;
      let requestBody;
      let response;

      if (this.qualification === "Promotional Round") {
        qualifiedPromo = true;
      } else if (this.qualification === "Final 1st Stage") {
        qualifiedPromo = true;
        qualifiedFinal = true;
      } else if (this.qualification === "Final 2nd Stage") {
        qualifiedPromo = true;
        qualifiedFinal = true;
        qualifiedFinalSec = true;
      }

      for (const metric of metricsForTeam) {
        metricIdsArray.push(metric.id);
        metricScoreArray.push(metric.enteredScore);
      }

      this.metricRequestBody = {
        metricIds: metricIdsArray,
        metricScores: metricScoreArray,
      };
      let CalScoreResponse = "";
      let updateTeamURL = "";
      try {
        requestBody = {
          id: this.teamToScore,
          isQualifiedPromo: qualifiedPromo,
          isQualifiedFinal: qualifiedFinal,
          isQualifiedFinalSecondStage: qualifiedFinalSec,
        };
        CalScoreResponse = await axios.post(
          `${api.CALCULATE_IDC_SCORE_BASE_URL}`,
          this.metricRequestBody,
          { headers }
        );
        updateTeamURL = api.UPDATE_IDC_TEAM_BASE_URL;
        response = await axios.put(
          `${api.QUALIFY_IDC_TEAM_BASE_URL}`,
          requestBody,
          { headers }
        );
        this.$swal({
          title: "Success!",
          text: "Team updated successfully!",
          icon: "success",
          timer: 2000, // Display the success message for 2 seconds
        });
      } catch (error) {
        console.error("Error fetching data:", error);
      }

      const presentationArray = [];

      const presentationObject = {
        score: Math.round(CalScoreResponse.data.data),
        stage: this.qualification,
        venue: "some venue",
      };

      presentationArray.push(presentationObject);
      const updateTeamRequestBody = {
        id: this.teamToScore,
        presentationRequestsList: presentationArray,
      };

      try {
        const updateTeamResponse = await axios.put(
          `${updateTeamURL}`,
          updateTeamRequestBody,
          { headers }
        );
        this.teamToScore = "";
        this.loadTeam();
      } catch (error) {
        console.error("Error updating team:", error);
      }
    },
    async previewScore(index) {
      let token = "";

      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      // Loop through each metric and add the required data to the array
      const metricsForTeam = this.filteredMetricsForTeam(this.activeRow);
      const team = this.filteredTeams[index];
      const metricIdsArray = [];
      const metricScoreArray = [];
      for (const metric of metricsForTeam) {
        metricIdsArray.push(metric.id);
        metricScoreArray.push(metric.enteredScore);
      }

      this.metricRequestBody = {
        metricIds: metricIdsArray,
        metricScores: metricScoreArray,
      };

      let CalScoreResponse = "";
      try {
        CalScoreResponse = await axios.post(
          `${api.CALCULATE_IDC_SCORE_BASE_URL}`,
          this.metricRequestBody,
          { headers }
        );

        this.calculatedScore = Math.round(CalScoreResponse.data.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },

    updateSelectedQualification(qualification) {
      this.selectedQualification = qualification;
      this.currentPage = 1; // Reset to the first page when the qualification is changed
    },
    filteredMetricsForTeam(teamIndex) {
      const team = this.filteredTeamsByQualification[teamIndex];
      const qualification = team.isQualifiedFinalSecondStage
        ? "Final 2nd Stage"
        : team.isQualifiedFinal
        ? "Final 1st Stage"
        : team.isQualifiedPromo
        ? "Promotional Round"
        : !team.isQualifiedPromo
        ? "Preliminary Round"
        : null;

      if (!qualification) {
        // If the team doesn't have any of the specified qualifications, return an empty array
        return [];
      }

      // Filter the metrics based on the team's qualification status
      return this.metrics.filter(
        (metric) => metric.stageName === qualification
      );
    },

    async toggleRow(index) {
      if (this.activeRow === index) {
        this.activeRow = null; // Collapse the row if it's already expanded
      } else {
        this.calculatedScore = null;
        this.activeRow = index;

        const team = this.filteredTeamsByQualification[index];

        this.teamToScore = team.id;

        let token = "";
        if (
          Vue.$keycloak &&
          Vue.$keycloak.token &&
          Vue.$keycloak.token.length > 0
        ) {
          token = Vue.$keycloak.token;
        } else {
          token = "mockedToken"; //for unit test
        }

        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        try {
          this.metricData = await axios.get(
            `${api.GET_ALL_IDC_METRIC_BASE_URL}`,
            {
              headers,
            }
          );
          this.metrics = this.metricData.data.data;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
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

<style scoped>
.step-text {
  color: black; /* Change the color to your desired color */
}

.grey-font {
  color: grey;
}
.green-score {
  color: green;
}

.red-score {
  color: red;
}
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
  background-color: rgb(218, 234, 253); /* Light blue for active parent row */
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
  margin-right: 65px;
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
  height: 40px;
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
  font-family: "Font Awesome"; /* Assuming you're using Font Awesome for icons */
  content: "\f107"; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}

.instruction-table {
  text-align: left;
  font-size: 14px;
  color: #120b51;
  margin-left: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center; /* Center horizontally */
  justify-content: center; /* Center vertically */
  height: 100%; /* Fill the height of the modal */
}
</style>
