<template>
  <b-tabs >
     <b-tab v-for="option in filteredCompetitionChoices" :key="option.id" :title="option.text" @click="selectedCompetition = option.text; loadTeam()">
    <br><br>
     <!-- show upload START-->
        <b-modal
          v-model="showUploadModal"
          modal-class="custom-modal"
          hide-footer
        >
            <!-- List of users to be displayed inside the modal -->
           <form class="upload-type">
            <div>
              <span style="font-size: 18px; color: #676767;">{{ displayTitleText(team) }}</span><br><br>
              <p v-html="displayGuideline(team)"></p>
            </div>
            <br>
            <div class="form-row">

              <input type="file" name="file-upload" id="file-upload" @change="onFileChange" accept=".pdf,.doc,.docx,.xlsx,.csv" />
            </div>
            <br><br>
            <b-button @click="upload(team)" variant="outline-primary" class="delete-button">Upload</b-button>
          </form>
        </b-modal>
        <!-- show upload Modal END-->
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
              <label class="label-color">Team Name :</label>
              {{ team.teamName }}
            </div>
          </td>
          <td>
            <div class="form-group select">
              <label><i class="fas fa-user-tag" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Age Group :</label>
              {{ ageGroupTextMap[team.ageGroup] }}
            </div>
          </td>
          <td>
            <div class="form-group select">
              <label><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i></label>
              <label class="label-color">Team Qualification :</label>
              {{ getQualificationStatus(team) }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <b-button id = "viewScore" @click="viewScore(team.id)" variant="outline-primary" class="delete-button">
                <b-icon icon="eye"></b-icon>
              </b-button>&nbsp;
              <b-button id = "viewUpload" @click="viewUpload(team)" variant="outline-primary" class="delete-button">
                <b-icon icon="cloud-upload"></b-icon>
              </b-button>&nbsp;
               <b-button id = "downloadFile" @click="downloadFile()" variant="outline-primary" class="delete-button">
                <b-icon icon="cloud-download"></b-icon>
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
import {VIEW_IDC_TEAM_BASE_URL,UPLOAD_PRIM_FILE_IDC_BASE_URL,UPLOAD_PROMO_FILE_IDC_BASE_URL,DOWNLOAD_PROMO_FILE_IDC_BASE_URL} from '@/api';
import axios from "axios";
import Vue from 'vue';

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      selectedCompetition: "Innovation Design Challenge",
      idcTeamId: "14a2413b-ff72-41ec-bd6c-c9273d9041fe",
      team:'',
      showHistoryModal: false,
      showUploadModal: false,
      presentationList: [],
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
      'OC': 'Open Category: 8-15 years old',
      'JC': 'Junior Category: 8-12 years old',
      // Add more entries as needed for other age groups
    };
    return ageGroupMap;
  },
  },
   async mounted() {
     let token='';
     if (Vue.$keycloak && Vue.$keycloak.token && Vue.$keycloak.token.length > 0) {
            token = Vue.$keycloak.token;
          } else {
            token = "mockedToken";//for unit test
          }
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
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
       let token='';
       if (Vue.$keycloak && Vue.$keycloak.token && Vue.$keycloak.token.length > 0) {
            token = Vue.$keycloak.token;
          } else {
            token = "mockedToken";//for unit test
          }
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
      const requestBody = {
        id: teamId,
      };
      try {
        const response = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
        const teamObject = response.data.data
        this.presentationList = teamObject.presentationResponses
        this.showHistoryModal = true; // Show the modal after fetching the users
      }
      catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    async viewUpload(teamId) {
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
        qualificationStatus = "Not Qualified";
      }

      return qualificationStatus;
    },
    async loadTeam() {
       let token='';
       if (Vue.$keycloak && Vue.$keycloak.token && Vue.$keycloak.token.length > 0) {
            token = Vue.$keycloak.token;
          } else {
            token = "mockedToken";//for unit test
          }
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
      const requestBody = {
        id: this.idcTeamId,
      };
      try {
          if (this.selectedCompetition === "Game Arena") {
            this.teamsData = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
          } else if (this.selectedCompetition === "Innovation Design Challenge") {
            this.teamsData = await axios.post(`${VIEW_IDC_TEAM_BASE_URL}`,requestBody, { headers });
          }
          this.team  = this.teamsData.data.data;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
    },
    displayTitleText(team) {
      if (!team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        return "Please upload proposal";
      } else if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        return "Please upload video";
      }
    },

    displayGuideline(team) {
      if (!team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        return `
          <span style="font-size: 14px; color: #333;">Guidelines:<br>
          a) The writing is concise, easy to understand, and demonstrates good choice of words.<br>
          b) The proposal demonstrates the efforts that the students have put in to understand the user story.<br>
          c) Max file size of 20MB.</span>`;
      } else if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
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
      console.log("Selected File:", selectedFile);
      console.log("File Name:", selectedFile.name);
      console.log("File Size:", selectedFile.size);
      console.log("File Type:", selectedFile.type);
      // You can perform any further processing with the selected file here
    },
    async upload(team) {

      if (!this.selectedFile) {
        console.log("No file selected.");
        return;
      }
       if (Vue.$keycloak && Vue.$keycloak.token && Vue.$keycloak.token.length > 0) {
            token = Vue.$keycloak.token;
          } else {
            token = "mockedToken";//for unit test
          }
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };

      const formData = new FormData();
      formData.append("files", this.selectedFile);
      formData.append("localFilePath", this.selectedFile.name);
      formData.append("team", team.id);

      try {
        const requestBody = {
          localFilePath: this.selectedFile, // Using the file name here
          idcTeamId: team.id, // Fill in the team ID
          fileContent: null // This will hold the file content
        };

        // Read the file content using FileReader
        const reader = new FileReader();

        // Define the onload event handler for the reader
        reader.onload = async (event) => {
          requestBody.fileContent = event.target.result;
          console.log('requestBody:', requestBody.fileContent);

          let response = '';

          if (this.selectedCompetition === "Game Arena") {
            response = await axios.post(`${test}`, formData, { headers });

          } else if (this.selectedCompetition === "Innovation Design Challenge") {

             if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
                 response = await axios.post(`${UPLOAD_PROMO_FILE_IDC_BASE_URL}`, formData, {
                  headers: {
                  "Content-Type": "multipart/form-data",
                  "Authorization": `Bearer ${token}`
                }});

              } else if (!team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
                response = await axios.post(`${UPLOAD_PRIM_FILE_IDC_BASE_URL}`, formData, {
                headers: {
                  "Content-Type": "multipart/form-data",
                  "Authorization": `Bearer ${token}`
                }});
              }
          }
        };

        // Read the file as an ArrayBuffer (binary data)
        reader.readAsArrayBuffer(this.selectedFile);

         // Show a success message to the user using VueSweetalert2
        this.$swal({
          title: 'Success!',
          text: 'Upload successful!',
          icon: 'success',
          timer: 2000, // Display the success message for 2 seconds
        });

        this.showUploadModal = false;
      } catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
    },
    async download(team) {
  const headers = {
    'Authorization': `Bearer ${Vue.$keycloak.token}`
  };

  try {
    let response;

    if (this.selectedCompetition === "Game Arena") {
      // Replace 'test' with the appropriate URL for Game Arena download
      response = await axios.post('YOUR_GAME_ARENA_DOWNLOAD_URL', {
        headers,
        responseType: 'blob', // To indicate a binary response
      });
    } else if (this.selectedCompetition === "Innovation Design Challenge") {
      const fileName = '1693974117222.mp4';

      if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        // Construct the URL for downloading the promotional file
        const promoFileURL = `${DOWNLOAD_PROMO_FILE_IDC_BASE_URL}/${team.teamName}-Promotional-${fileName}`;

        response = await axios.post(promoFileURL, {
          headers,
          responseType: 'blob',
        });
      } else if (!team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        // If you want to upload a file, you should use a POST request, not here.
        // You can add the file upload logic separately.
        console.log('Upload logic goes here');
        return;
      }
    }

    // Check if the response contains a valid file
    if (response && response.status === 200) {
      const blob = response.data;
      const url = window.URL.createObjectURL(blob);

      // Create a link element to trigger the download
      const a = document.createElement('a');
      a.style.display = 'none';
      a.href = url;
      a.download = 'downloaded-file.mp4'; // Set the desired file name
      document.body.appendChild(a);

      // Trigger the download and cleanup
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    }
  } catch (error) {
    // Handle errors, if any
    console.error('Error calling API:', error);
  }
},
downloadFile() {
            axios({
                url: 'http://localhost:8082/idcteam/download-file?file=participants/Team%20ABC-Promotional-1693974117222.mp4', // Download File URL Goes Here
                method: 'POST',
                responseType: 'blob',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Access-Control-Allow-Methods': ' GET, PUT, POST, DELETE, OPTIONS',
                    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
                    'Access-Control-Allow-Credentials': 'false',
                    'Authorization': `Bearer ${Vue.$keycloak.token}`
                },
            }).then((res) => {
                var FILE = window.URL.createObjectURL(new Blob([res.data]));
                var docUrl = document.createElement('x');
                docUrl.href = FILE;
                docUrl.setAttribute('download', 'video.mp4');
                document.body.appendChild(docUrl);
                docUrl.click();
            });
        }
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
  background: linear-gradient(to bottom right, #5DADE2);
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 20px;
}
</style>
