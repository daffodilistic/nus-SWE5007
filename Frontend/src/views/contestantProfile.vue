<template>
  <b-tabs >
     <b-tab v-for="option in filteredCompetitionChoices" :key="option.id" :title="option.text" @click="selectedCompetition = option.text; loadTeam()">
    <br><br>
     <!-- show history Modal START-->
        <b-modal
          v-model="showHistoryModal"
          modal-class="custom-modal"
          title="Competition Score"
          hide-footer
        >
            <!-- List of users to be displayed inside the modal -->
            <table class="modal-table">
                  <thead>
                    <tr>
                      <th>Stage</th>
                      <th>Venue</th>
                      <th>Score</th>
                      <th>Date / Time</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="presentationList.length === 0">
                      <tr>
                        <td colspan="5"><br><br>No records available.</td>
                      </tr>
                      <tr><br><br></tr>
                    </template>
                    <template v-else>
                      <tr v-for="(presentation, presentationIndex) in presentationList" :key="presentation.id">
                        <td>{{ presentation.stage }} </td>
                        <td>{{ presentation.venue }} </td>
                        <td>{{ presentation.score }} </td>
                        <td>{{ formatDateTime(presentation.dateTime) }}</td>
                      </tr>
                      <tr><br><br></tr>
                    </template>

                  </tbody>
                </table>

        </b-modal>
        <!-- show history Modal END-->
      <div class="form-container">
      <table>
            <tr>
          <td>
            <div class="form-group">
              <label><i class="fa fa-users fa-lg" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Team Name :&nbsp;&nbsp;</label>
              {{ team.teamName }}
            </div>
          </td>
          <td>
            <div class="form-group select">
              <label><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Age Group :&nbsp;&nbsp;</label>
              {{ ageGroupTextMap[team.ageGroup] }}
            </div>
          </td>
          <td>
            <div class="form-group select">
              <label><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Team Qualification :&nbsp;&nbsp;</label>
              {{ getQualificationStatus(team) }}&nbsp;&nbsp;
              <b-button @click="viewScore(team.id)" variant="outline-primary" class="delete-button">
                <b-icon icon="eye"></b-icon>
              </b-button>
            </div>

          </td>
        </tr>
        <tr>&nbsp;</tr>
        <!--
        <tr>
          <td>
            <div class="form-group select">
              <label><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">School Name :&nbsp;&nbsp;</label>
            </div>
          </td>
          <td>
            <div class="form-group">
              <label><i class="far fa-id-card fa-lg" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Teacher Name :&nbsp;&nbsp;</label>
            </div>
          </td>
        </tr>
        <tr>&nbsp;</tr>-->
         <tr>
              <td :colspan="10"> <!-- Use colspan to span all columns in the row -->
                <table class="user-table">
                  <thead>
                    <tr>
                      <th>S/No.</th>
                      <th>First Name</th>
                      <th>Last Name</th>
                      <th>Email</th>
                      <th>Phone</th>
                      <th>Country</th>
                      <th>State</th>
                      <th>Birthday</th>
                      <th>School Name</th>
                      <th>Experience (Year)</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="team.userResponses.length === 0">
                      <td colspan="11"  style="color: red;">0 member in the team</td>
                    </tr>
                    <tr v-else v-for="(user, userIndex) in team.userResponses">
                      <td> {{ userIndex + 1 }} </td>
                      <td> {{ user.firstName }} </td>
                      <td> {{ user.lastName }} </td>
                      <td> {{ user.email }} </td>
                      <td> {{ user.phone }} </td>
                      <td> {{ user.country }} </td>
                      <td> {{ user.state }} </td>
                      <td> {{ user.dateOfBirth }} </td>
                      <td> {{ user.schoolName }} </td>
                      <td> {{ user.yearsOfExp }} </td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
      </table>
    </div>
    </b-tab>
  </b-tabs>
</template>

<script>
import {competitionChoiceOptions,} from "../dropdownOptions";
import {VIEW_IDC_TEAM_BASE_URL} from '@/api';
import axios from "axios";
import Vue from 'vue';

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      selectedCompetition: "Innovation Design Challenge",
      idcTeamId: "7c8f5f5b-e85e-4e66-a40f-bde7ff9db5ab",
      team:'',
      showHistoryModal: false,
      presentationList: [],
    };
  },
  computed: {
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
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
  },
   async mounted() {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${Vue.$keycloak.token}`
    };
    const requestBody = {
      id: this.idcTeamId,
    };
    try {
      this.teamsData = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
      this.team  = this.teamsData.data.data;
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    formatDateTime(dateTime) {
      const date = new Date(dateTime);
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');

      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
  },
    async viewScore(teamId) {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      const requestBody = {
        id: teamId,
      };
      try {
        const response = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
        const teamObject = response.data.data
        this.presentationList = teamObject.presentationResponses
        console.log("data:", this.presentationList);

        this.showHistoryModal = true; // Show the modal after fetching the users
      }
      catch (error) {
        console.error("Error fetching users:", error);
      }
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
        qualificationStatus = "Not Qualified";
      }

      return qualificationStatus;
    },
    async loadTeam() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      const requestBody = {
        id: this.idcTeamId,
      };
      try {
          if (this.selectedCompetition === "Game Arena") {
            this.teamsData = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
          console.log('ga called')
          } else if (this.selectedCompetition === "Innovation Design Challenge") {
            this.teamsData = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
            console.log('IDC called')
          }
          this.team  = this.teamsData.data.data;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
    },
  },
};
</script>

<style scoped>
.form-container {
  display: flex;

  flex-wrap: wrap;

  justify-content: left;
}

.form-group {
  display: flex;
  align-items: center;
  margin-right: 50px;
}

.form-group label {
  margin-right: 5px;
  font-weight: bold;
  font-size: 17px;
}

.label-color {
  color: #676767;
}
.table-container {
  max-width: 100%;
  overflow-x: auto;
  margin-bottom: 35px;
}

.user-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border: none;
  border-radius: 15px;
  overflow: hidden;
  font-size: 22px;
}

.user-table th,
.user-table td {
  padding: 8px;
  text-align: center;
  font-size: 14px;
  background-color: #f6f6f6;
}

.user-table th {
  background-color: #d7e7f2;
  font-weight: bold;
}

.no-members {
  color: red;
  text-align: center;
  padding: 10px;
}

/* Modal Styles */
.custom-modal .modal-dialog  {
    max-width: 1300px; /* Set the max width of the modal */
    text-align: center;
  }

/* Center the modal title */
.custom-modal .modal-header {
  text-align: center;
}

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */
  /* max-width: 800px; */
}
</style>
