<template>
  <div>
    <!-- show upload START-->
    <b-modal v-model="showUploadModal" modal-class="custom-modal" hide-footer>
      <!-- List of users to be displayed inside the modal -->
      <form class="upload-type">
        <div>
          <span style="font-size: 18px; color: #676767">{{
            displayTitleText(team)
          }}</span
          ><br /><br />
          <p v-html="displayGuideline(team)"></p>
        </div>
        <br />
        <div class="form-row">
          <input
            type="file"
            name="file-upload"
            id="file-upload"
            @change="onFileChange"
            accept=".pdf,.doc,.docx,.xlsx,.csv"
          />
        </div>
        <br /><br />
        <b-button
          @click="upload(team)"
          variant="outline-primary"
          class="delete-button"
          >Upload</b-button
        >
      </form>
    </b-modal>
    <!-- show upload Modal END-->

    <div style="text-align: right">
      <br />
      <b-button
        id="viewUpload"
        @click="viewUpload(team)"
        variant="outline-primary"
        class="delete-button"
        v-b-tooltip.hover="'Click to upload submission'"
      >
        <b-icon icon="cloud-upload"></b-icon>&nbsp;Upload </b-button
      >&nbsp;
      <b-button
        id="gameManual"
        @click="downloadAdminFile(selectedCompetition, 'GM')"
        variant="outline-primary"
        class="delete-button"
        v-b-tooltip.hover="'Click to download Game Manual'"
      >
        <b-icon icon="book"></b-icon>&nbsp;Game Manual </b-button
      >&nbsp;
      <b-button
        id="gameManual"
        @click="downloadAdminFile(selectedCompetition, 'TT')"
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
                    <tr v-if="team.userResponses.length === 0">
                      <td colspan="11" style="color: red">
                        0 member in the team
                      </td>
                    </tr>
                    <tr v-else v-for="(user, userIndex) in team.userResponses">
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
          @click="viewScore(team.id)"
        >
          &nbsp;&nbsp;<img
            src="../assets/qualification.png"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; Qualification History
        </b-button>
      </b-card-header>
      <b-collapse
        id="second-accordion"
        accordion="my-accordion"
        role="tabpanel"
      >
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
                <td colspan="5"><br /><br />No records available.</td>
              </tr>
              <tr>
                <br /><br />
              </tr>
            </template>
            <template v-else>
              <tr
                v-for="(presentation, presentationIndex) in presentationList"
                :key="presentation.id"
              >
                <td>{{ presentation.stage }}</td>
                <td>{{ presentation.venue }}</td>
                <td>{{ presentation.score }}</td>
                <td>{{ formatDateTime(presentation.dateTime) }}</td>
              </tr>
              <tr>
                <br /><br />
              </tr>
            </template>
          </tbody>
        </table>
      </b-collapse>
    </b-card>

    <b-card no-body class="mb-2">
      <b-card-header
        header-tag="header"
        class="p-2"
        role="tab"
        style="background-color: #f5f5f5"
      >
        <b-button
          block
          v-b-toggle.third-accordion
          variant="info"
          class="accordion-button"
          @click="viewDownload(team.teamName)"
        >
          &nbsp;&nbsp;<img
            src="../assets/folder.png"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; File Submission History
        </b-button>
      </b-card-header>
      <b-collapse id="third-accordion" accordion="my-accordion" role="tabpanel">
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
              <tr
                v-for="(file, fileIndex) in downloadFileList"
                :key="fileIndex"
              >
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
      </b-collapse>
    </b-card>
  </div>
</template>

<script>
import { competitionChoiceOptions } from "../dropdownOptions";
import { api } from "../api";
import axios from "axios";
import Vue from "vue";

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      selectedCompetition: "Innovation Design Challenge",
      idcTeamId: "",
      team: "",
      showHistoryModal: false,
      showUploadModal: false,
      showDownloadModal: false,
      presentationList: [],
      downloadFileList: [],
      downloadAdminFileList: [],
      selectedFile: null, // Initialize the selectedFile variable
    };
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
  },
  async mounted() {
    this.getTeamID();
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
        const filteredUser = team.filter(
          (record) => record.firstName === userName
        );
        this.idcTeamId = filteredUser[0].idcTeam;

        const requestBody = {
          id: this.idcTeamId,
        };
        this.teamsData = await axios.post(
          `${api.VIEW_IDC_TEAM_BASE_URL}`,
          requestBody,
          { headers }
        );
        this.team = this.teamsData.data.data;
      } catch (error) {
        // Handle any errors that might occur during the request
        console.error("Error fetching users:", error);
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
    async viewScore(teamId) {
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
      const requestBody = {
        id: teamId,
      };
      try {
        const response = await axios.post(
          `${api.VIEW_IDC_TEAM_BASE_URL}`,
          requestBody,
          { headers }
        );
        const teamObject = response.data.data;
        this.presentationList = teamObject.presentationResponses;
        //this.showHistoryModal = true; // Show the modal after fetching the users
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
        //this.showDownloadModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    async viewUpload(team) {
      this.showUploadModal = true;
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
      const requestBody = {
        id: this.idcTeamId,
      };
      try {
        this.teamsData = await axios.post(
          `${api.VIEW_IDC_TEAM_BASE_URL}`,
          requestBody,
          { headers }
        );
        this.team = this.teamsData.data.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    displayTitleText(team) {
      if (
        !team.isQualifiedPromo &&
        !team.isQualifiedFinal &&
        !team.isQualifiedFinalSecondStage
      ) {
        return "Please Upload Proposal";
      } else if (
        team.isQualifiedPromo &&
        !team.isQualifiedFinal &&
        !team.isQualifiedFinalSecondStage
      ) {
        return "Please Upload Video";
      }
    },

    displayGuideline(team) {
      if (
        !team.isQualifiedPromo &&
        !team.isQualifiedFinal &&
        !team.isQualifiedFinalSecondStage
      ) {
        return `
          <span style="font-size: 14px; color: #333;">Guidelines:<br>
          a) The writing is concise, easy to understand, and demonstrates good choice of words.<br>
          b) The proposal demonstrates the efforts that the students have put in to understand the user story.<br>
          c) Max file size of 20MB.</span>`;
      } else if (
        team.isQualifiedPromo &&
        !team.isQualifiedFinal &&
        !team.isQualifiedFinalSecondStage
      ) {
        return `
          <span style="font-size: 14px; color: #333;">Guidelines:<br>
          a) Background Summary: The video should concisely describe the user story and the robotics idea.<br>
          b) Design idea: The participant should explain the theory and knowledge behind the design.<br>
          c) Functionality: The video should explain the functionality of the robot, how it is implemented and why it performs the use case.<br>
          d) Presentation: The presentation should be logical, fluent and easy to understand. The presenter should appear decently, and the video should be smooth and good in quality.<br>
          e) Max file size of 20MB.</span>`;
      }
    },

    onFileChange(event) {
      // Handle the file change event here
      this.selectedFile = event.target.files[0];
      // You can perform any further processing with the selected file here
    },
    async upload(team) {
      let token = "";
      if (!this.selectedFile) {
        return;
      }
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

      const formData = new FormData();
      formData.append("files", this.selectedFile);
      formData.append("localFilePath", this.selectedFile.name);
      formData.append("team", team.id);

      try {
        const requestBody = {
          localFilePath: this.selectedFile, // Using the file name here
          idcTeamId: team.id, // Fill in the team ID
          fileContent: null, // This will hold the file content
        };

        // Read the file content using FileReader
        const reader = new FileReader();

        // Define the onload event handler for the reader
        reader.onload = async (event) => {
          requestBody.fileContent = event.target.result;

          let response = "";

          if (
            team.isQualifiedPromo &&
            !team.isQualifiedFinal &&
            !team.isQualifiedFinalSecondStage
          ) {
            response = await axios.post(
              `${api.UPLOAD_PROMO_FILE_IDC_BASE_URL}`,
              formData,
              {
                //video .mp4
                headers: {
                  "Content-Type": "multipart/form-data",
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          } else if (
            !team.isQualifiedPromo &&
            !team.isQualifiedFinal &&
            !team.isQualifiedFinalSecondStage
          ) {
            response = await axios.post(
              `${api.UPLOAD_PRIM_FILE_IDC_BASE_URL}`,
              formData,
              {
                //pdf
                headers: {
                  "Content-Type": "multipart/form-data",
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          }
        };

        // Read the file as an ArrayBuffer (binary data)
        reader.readAsArrayBuffer(this.selectedFile);

        // Show a success message to the user using VueSweetalert2
        this.$swal({
          title: "Success!",
          text: "Upload successful!",
          icon: "success",
          timer: 2000, // Display the success message for 2 seconds
        });

        this.showUploadModal = false;
      } catch (error) {
        // Handle errors, if any
        console.error("Error calling API:", error);
      }
    },

    downloadFile(file) {
      axios({
        url: `${api.DOWNLOAD_FILE_IDC_BASE_URL}/${file}`,
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
    async downloadAdminFile(compType, docType) {
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
            return parts[0] === "IDC" && parts[1] === docType;
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
</style>
