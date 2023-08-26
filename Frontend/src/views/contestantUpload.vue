<template>
  <b-tabs >
    <b-tab v-for="option in filteredCompetitionChoices" :key="option.id" :title="option.text" @click="selectedCompetition = option.text">
      <div class="upload">
        <form class="upload-type">
          <div>
            <span style="font-size: 20px; color: #676767;">{{ displayText(teams) }}</span>
          </div>
          <br>
          <div class="form-row">

            <input type="file" name="file-upload" id="file-upload" @change="onFileChange" accept=".pdf,.doc,.docx,.xlsx,.csv" />
          </div>
          <br>
          <b-button @click="upload(teams)" variant="outline-primary" class="delete-button">Upload</b-button>
        </form>
      </div>
    </b-tab>
  </b-tabs>
</template>

<script>
import {competitionChoiceOptions,adminUploadTypeOptions,qualificationOptions } from "../dropdownOptions";
import {UPLOAD_PRIM_FILE_IDC_BASE_URL,UPLOAD_PROMO_FILE_IDC_BASE_URL,VIEW_IDC_TEAM_BASE_URL} from '@/api';
import axios from "axios";
import Vue from 'vue';

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      adminUploadTypeOptions : adminUploadTypeOptions,
      selectedCompetition: "Innovation Design Challenge",
      selectedQualification: '',
      selectedFile: null, // Initialize the selectedFile variable
      idcTeamId: "136d0c4b-025f-4cbf-b80e-441f93fb3f44",
      teams: null,
    };
  },
  computed: {
    qualificationOptions() {
      return qualificationOptions;
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    filteredQualificationOptions() {
    if (this.selectedCompetition === "Game Arena") {
      return this.qualificationOptions.filter(option => option.competitionId === '2');
    } else if (this.selectedCompetition === "Innovation Design Challenge") {
      return this.qualificationOptions.filter(option => option.competitionId === '1' && option.value !== 'DIQ');
    }
    return [];
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
      const allTeams = this.teamsData.data.data;

      // Filter out teams that do not have any of the specified qualifications
      //this.teams = allTeams.filter((team) => team.isQualifiedPromo || team.isQualifiedFinal || team.isQualifiedFinalSecondStage);
      this.teams = allTeams

    } catch (error) {
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    displayText(team) {
      if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        return "Please upload proposal";
      } else if (team.isQualifiedPromo && team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
        return "Please upload video";
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
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };

      const formData = new FormData();
      formData.append("file", this.selectedFile);
      formData.append("localFilePath", this.selectedFile.name);
      formData.append("idcTeamId", team.id);

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
            console.log('ga called');
          } else if (this.selectedCompetition === "Innovation Design Challenge") {

             if (team.isQualifiedPromo && !team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
                 response = await axios.post(`${UPLOAD_PROMO_FILE_IDC_BASE_URL}`, formData, {
                  headers: {
                  "Content-Type": "multipart/form-data",
                  "Authorization": `Bearer ${Vue.$keycloak.token}`
                }});
                console.log('IDC PRO called'+ response);
              } else if (team.isQualifiedPromo && team.isQualifiedFinal && !team.isQualifiedFinalSecondStage) {
                response = await axios.post(`${UPLOAD_PRIM_FILE_IDC_BASE_URL}`, formData, {
                headers: {
                  "Content-Type": "multipart/form-data",
                  "Authorization": `Bearer ${Vue.$keycloak.token}`
                }});
              console.log('IDC PRE called');
              }
          }

          // Handle the response, if needed
          console.log('Response from server:', response.data);
        };

        // Read the file as an ArrayBuffer (binary data)
        reader.readAsArrayBuffer(this.selectedFile);
      } catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
    }
  },
};
</script>

<style scoped>
.upload {
  display: flex;
  /* Create a column layout for the form container */
  justify-content: center;
  /* Center horizontally */
  align-items: center;
  /* Center vertically */
  margin-top: 100px;
}

.upload-type {
  background-color: #f5f5f5;
  /* Change background color to match the login page */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 20px;
  /* Adjust padding for consistency */
  width: 30%;
  /* Set the width to match the login page */
  text-align: left;
  display: block;
}

/* Add the white-background class to set the background of the dropdown to white */
.white-background {
  background-color: #fff;
}

.form-row {
  margin-bottom: 20px;
}

.form-row label {
  display: block;
  margin-bottom: 5px;
  font-size: 20px;
  color: #676767;
}

.form-row select,
.form-row input[type="file"] {
  width: 100%;
  height: 50px;
  /* Match the height of text inputs in the login page */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 10px;
  background-color: #fff;
}

.form-row select {
  appearance: none;
  background: url('https://cdn4.iconfinder.com/data/icons/ionicons/512/icon-ios7-arrow-down-512.png') no-repeat right center;
  background-size: 20px;
  padding-right: 40px;
  background-color: #fff;
}

.form-row select::-ms-expand {
  display: none;
}

button {
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
