<template>
  <div>
    <br />
    <div style="text-align: right">
      <b-button
        id="gameManual"
        @click="downloadAdminFile('GM')"
        variant="outline-primary"
        class="delete-button"
        v-b-tooltip.hover="'Click to download Game Manual'"
      >
        <b-icon icon="book"></b-icon>&nbsp;Game Manual </b-button
      >&nbsp;
      <b-button
        id="gameManual"
        @click="downloadAdminFile('TT')"
        variant="outline-primary"
        class="delete-button"
        v-b-tooltip.hover="'Click to download Time Table'"
      >
        <b-icon icon="table"></b-icon>&nbsp;Time Table </b-button
      ><br /><br />
    </div>

    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.first-accordion
          variant="info"
          class="accordion-button"
          @click="loadTeam()"
        >
          &nbsp;&nbsp;<img
            src="../assets/team.png"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; Team Details
        </b-button>
      </b-card-header>
      <b-collapse id="first-accordion" accordion="my-accordion" role="tabpanel">
        <!-- End of Accordion -->
        <div class="form-container">
          <table>
            <tr>
              &nbsp;
            </tr>
            <tr>
              &nbsp;
            </tr>
            <tr>
              <td>
                <div class="form-group">
                  <label
                    ><i
                      class="fa fa-users fa-lg"
                      style="color: rgb(65, 127, 202)"
                    ></i
                  ></label>
                  <label class="label-color">Team Name :</label>
                  {{ team.teamName }}
                </div>
              </td>
              <td>
                <div class="form-group select">
                  <label
                    ><i
                      class="fas fa-user-tag"
                      style="color: rgb(65, 127, 202)"
                    ></i
                  ></label>
                  <label class="label-color">Age Group :</label>
                  {{ ageGroupTextMap[team.ageGroup] }}
                </div>
              </td>
              <td>
                <div class="form-group select">
                  <label
                    ><i
                      class="fas fa-trophy"
                      style="color: rgb(65, 127, 202)"
                    ></i
                  ></label>
                  <label class="label-color">Team Qualification :</label>
                  {{
                    getQualificationStatus(team)
                  }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </div>
              </td>
            </tr>

            <tr>
              &nbsp;
            </tr>
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
              <td :colspan="10">
                <!-- Use colspan to span all columns in the row -->
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
                    <tr v-if="teamMembers.length === 0">
                      <td colspan="11" style="color: red">
                        0 member in the team
                      </td>
                    </tr>
                    <tr v-else v-for="(user, userIndex) in teamMembers">
                      <td>{{ userIndex + 1 }}</td>
                      <td>{{ user.firstName }}</td>
                      <td>{{ user.lastName }}</td>
                      <td>{{ user.email }}</td>
                      <td>{{ user.phone }}</td>
                      <td>{{ user.country }}</td>
                      <td>{{ user.state }}</td>
                      <td>{{ user.dateOfBirth }}</td>
                      <td>{{ user.schoolName }}</td>
                      <td>{{ user.yearsOfExp }}</td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
          </table>
        </div>
      </b-collapse>
    </b-card>

    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.second-accordion
          variant="info"
          class="accordion-button"
          @click="loadTechComp()"
        >
          &nbsp;&nbsp;<img
            src="../assets/qualification.png"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; Match History
        </b-button>
      </b-card-header>
      <b-collapse
        id="second-accordion"
        accordion="my-accordion"
        role="tabpanel"
      >
        <table class="main-table">
          <thead>
            <tr>
              <th></th>
              <th>Game Name</th>
              <th>Team Name</th>
              <th></th>
              <th>Team Name</th>
            </tr>
          </thead>
          <tbody v-for="(techComp, index) in techComps" :key="index">
            <tr>
              <td></td>
              <td class="normal-td">
                {{ techComp.gameName }}
              </td>
              <td class="oppo2-td">
                <span v-if="techComp.gameOutcome === 'win'">
                  <i class="fas fa-star gold-star"></i><br />
                </span>
                {{ techComp.gameTeamIdHostName }}
              </td>
              <td><img src="../assets/Versus_icon.png" alt="Versus" /></td>
              <td class="oppo-td">
                <span v-if="techComp.gameOutcome === 'lose'">
                  <i class="fas fa-star gold-star"></i><br />
                </span>
                {{ techComp.gameTeamIdOppoName }}
              </td>
            </tr>
          </tbody>
        </table>
      </b-collapse>
    </b-card>
  </div>
</template>

<script>
import { competitionChoiceOptions } from "../dropdownOptions";
import axios from "axios";
import Vue from "vue";
import { api } from "../api";

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      gameTeamId: "",
      team: "",
      presentationList: [],
      downloadFileList: [],
      downloadAdminFileList: [],
      selectedFile: null, // Initialize the selectedFile variable
      gameTeamList: [],
      techComps: [],
      teamMembers: [],
    };
  },
  async mounted() {
    this.getTeamID();
  },
  computed: {
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
    async getTeamID() {
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

      const tokenData = Vue.$keycloak.tokenParsed;

      const userName = tokenData.preferred_username;

      // Assign the user's name to a variable or use it as needed
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`${api.GET_ALL_USER_INFO_BASE_URL}`, {
          headers,
        });
        const team = response.data.data;
        console.log(userName, "team", team);

        const filteredUser = team.filter(
          (record) => record.firstName === userName
        );
        this.gameTeamId = filteredUser[0].gameTeam;

        this.teamMembers = team.filter((record) => {
          return (
            record.hasOwnProperty("gameTeam") &&
            record.gameTeam === this.gameTeamId
          );
        });
      } catch (error) {
        // Handle any errors that might occur during the request
        console.error("Error fetching users:", error);
      }
    },
    async loadTechComp() {
      console.log("Tech Comp Loaded");
      this.techComps = [];

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
        const techCompsData = await axios.get(`${api.VIEW_ALL_TC_BASE_URL}`, {
          headers,
        });
        const techComp = techCompsData.data.data;

        const response2 = await axios.get(`${api.GET_ALL_GAME_TEAM_BASE_URL}`, {
          headers,
        });
        const teamList = response2.data.data;

        //merge 2 arrays
        for (let i = 0; i < teamList.length; i++) {
          for (let j = 0; j < techComp.length; j++) {
            if (teamList[i].id === techComp[j].gameTeamIdHost) {
              techComp[j].gameTeamIdHostName = teamList[i].teamName; // Add gameTeamIdHostName property to techComp
            } else if (teamList[i].id === techComp[j].gameTeamIdOppo) {
              techComp[j].gameTeamIdOppoName = teamList[i].teamName; // Add gameTeamIdOppoName property to techComp
            }
          }
        }

        for (let j = 0; j < techComp.length; j++) {
          if (techComp[j].gameStatus !== "done") {
            // Use splice to remove the element at index j
            techComp.splice(j, 1);
            // Decrement j to account for the removed element
            j--;
          } else if (techComp[j].gameTeamIdHost !== this.gameTeamId) {
            if (techComp[j].gameTeamIdOppo !== this.gameTeamId)
              techComp.splice(j, 1);
            // Decrement j to account for the removed element
            j--;
          }
        }
        this.techComps = techComp;
        console.log(this.techComps, " - ", this.gameTeamId);
      } catch (error) {
        // Handle any errors that might occur during the request
        console.error("Error fetching techComps:", error);
      }
    },

    formatDateTime(dateTime) {
      const date = new Date(dateTime);
      const day = String(date.getDate()).padStart(2, "0");
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const year = date.getFullYear();
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      const seconds = String(date.getSeconds()).padStart(2, "0");

      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    },

    getQualificationStatus(team) {
      let qualificationStatus;

      if (team.isQualifiedForElimination) {
        qualificationStatus = "Elimination Round";
      } else if (team.isQualifiedFinal) {
        qualificationStatus = "Qualification Round";
      }

      return qualificationStatus;
    },
    async loadTeam() {
      this.teamsData = [];
      this.team = "";
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
        const response = await axios.get(`${api.GET_ALL_GAME_TEAM_BASE_URL}`, {
          headers,
        });
        const teamObj = response.data.data.filter(
          (team) => team.id === this.gameTeamId
        );
        this.team = teamObj[0];

        console.log("teamsData", this.team);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    async downloadAdminFile(docType) {
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
        const response = await axios.get(
          `${api.VIEW_ALL_ADMIN_FILES_BASE_URL}`,
          {
            headers,
          }
        );
        const originalArray = response.data.data;

        // Remove the "participants/" prefix from each item
        const interimArray = originalArray.map((item) => {
          // Use string manipulation to remove the prefix
          return item.replace("admin/", "");
        });

        this.downloadAdminFileList = interimArray.filter((item) => {
          const parts = item.split("-");
          if (parts.length >= 2) {
            // Check if the first part of the filename matches the prefix
            return parts[0] === "GAC" && parts[1] === docType;
          }
          return false;
        });
      } catch (error) {
        console.error("Error fetching users:", error);
      }
      axios({
        url: `${api.DOWNLOAD_ADMIN_FILE_IDC_BASE_URL}/${this.downloadAdminFileList}`,
        method: "POST",
        responseType: "blob",
        headers: {
          Authorization: `Bearer ${Vue.$keycloak.token}`,
        },
      })
        .then((res) => {
          // Get the file type (MIME type) from the response Blob
          const fileType = res.data.type;

          // Extract the filename from the URL or generate it dynamically
          const urlParts = res.config.url.split("/");
          const filename = urlParts[urlParts.length - 1];

          // Create a Blob from the response data
          const blob = new Blob([res.data], { type: fileType });

          // Create a URL for the Blob
          const url = window.URL.createObjectURL(blob);

          // Create a link element
          const link = document.createElement("a");
          link.href = url;

          // Set the download attribute to the extracted/generated filename
          link.setAttribute("download", filename);

          // Trigger the download
          document.body.appendChild(link);
          link.click();

          // Clean up
          window.URL.revokeObjectURL(url);
          document.body.removeChild(link);
        })
        .catch((error) => {
          console.error("Error downloading file:", error);
          // Handle the error here (e.g., show an error message to the user)
        });
    },
  },
};
</script>

<style scoped>
.form-container {
  display: flex;

  flex-wrap: wrap;

  justify-content: left;

  margin-left: 20px;
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
.custom-modal .modal-dialog {
  max-width: 1300px;
  min-width: 1300px; /* Set the max width of the modal */
  text-align: center;
}

/* Center the modal title */

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */
  min-width: 450px;
  text-align: center;
  margin-left: 50px;
}

button[type="submit"] {
  padding: 10px 0;
  width: 100%;
  /* Set the button width to 100% of its parent container */
  background: linear-gradient(to bottom right, #5dade2);
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 20px;
}

.accordion-button {
  color: #100101;
}

.accordion-button:hover {
  background-color: #0056b3;
  color: #ffffff;
}

.gold-star {
  color: gold; /* Apply gold color to the trophy icon */
}

.normal-td {
  font-size: 18x;
}
.oppo-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 25px;
  color: rgba(255, 0, 0, 0.807);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
  height: 30px;
}

.host-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 25x;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
  height: 30px;
}

.oppo2-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 25px;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
  height: 30px;
}

.main-table {
  margin-top: 20px;
  margin-left: 20px;
  margin-bottom: 20px;
}
</style>
