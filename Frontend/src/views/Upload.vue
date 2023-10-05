<template>
<div>

   <b-card no-body class="mb-2">
    <b-card-header header-tag="header" class="p-2" role="tab">
      <b-button
        block
        v-b-toggle.first-accordion
        variant="info"
        class="accordion-button"
      > &nbsp;&nbsp;&nbsp;<b-icon icon="cloud-upload"></b-icon> &nbsp;&nbsp;&nbsp;&nbsp;Upload File
      </b-button>
    </b-card-header>
    <b-collapse id="first-accordion" accordion="my-accordion" role="tabpanel">
    <div class="upload">
    <form @submit.prevent="onSubmit" class="upload-type">
      <div class="form-row">
        <label for="game-type"><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Competition Choice</label>
        <select v-model="selectedComp" name="game-type" id="game-type" class="white-background">
          <option value="" disabled selected>Select an option</option>
          <!-- Use v-for to loop through competitionChoiceOptions -->
          <option v-for="option in competitionChoiceOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
        </select>
      </div>
      <br>
      <div class="form-row">
        <label for="upload-type"><i class="fas fa-file" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Document Category</label>
        <select v-model="selectedUploadType" name="upload-type" id="upload-type" class="white-background">
          <option value="" disabled selected>Select an option</option>
          <option v-for="option in adminUploadTypeOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
        </select>
      </div>
      <br>

      <div class="form-row">
        <label for="file-upload"><i class="fas fa-file-upload" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Upload File</label>
        <input type="file" name="file-upload" id="file-upload" @change="onFileChange" accept=".pdf,.doc,.docx,.xlsx,.csv" />
      </div>
      <br>
      <button type="submit" @click="upload()"><i class="fas fa-upload"></i> Upload</button>
    </form></div>
        </b-collapse>
  </b-card>

  <!-- Separate the b-card from the form -->
  <b-card no-body class="mb-2">
    <b-card-header header-tag="header" class="p-2" role="tab">
      <b-button
        block
        v-b-toggle.second-accordion
        variant="info"
        class="accordion-button"
        @click="viewDownload()"
      > &nbsp;&nbsp;<img src="../assets/folder.png" alt="Versus" width="35px" height="30px">&nbsp;
      File Upload History
      </b-button>
    </b-card-header>
    <b-collapse id="second-accordion" accordion="my-accordion" role="tabpanel">
      <!-- Collapsible content here -->
      <div class="upload">
       <table class="modal-table">
                  <thead>
                    <tr>
                    <th>Competition Type</th>
                     <th>File Type</th>
                      <th>Filename</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="downloadFileList.length === 0">
                      <tr>
                        <td colspan="5"><br><br>No records available.</td>
                      </tr>
                      <tr><br><br></tr>
                    </template>
                    <template v-else>
                       <tr v-for="(file, fileIndex) in downloadFileList" :key="fileIndex">
                       <td>{{ CompTypeTextMap[file.substring(0, 3)] }} </td>
                       <td>{{ fileTypeTextMap[file.substring(4, 6)] }} </td>
                        <td>{{ file.substring(7) }} </td>
                        <td> <b-button id = "downloadFile" @click="downloadFile(file)" variant="outline-primary" class="delete-button">
                <b-icon icon="cloud-download"></b-icon>
              </b-button></td>

                      </tr>
                      <tr><br><br></tr>
                    </template>
                  </tbody>
                </table></div>
    </b-collapse>
  </b-card>
  </div>
</template>

<style scoped>

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */

  text-align: left;
}
.upload {

  /* Create a column layout for the form container */
  justify-content: center;
  /* Center horizontally */
  align-items: center;
  /* Center vertically */
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left:50px;
}

.upload-type {
  background-color: #f5f5f5;
  /* Change background color to match the login page */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 10px;
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
  margin-bottom: 10px;
}

.form-row label {
  display: block;
  margin-bottom: 5px;
  font-size: 15px;
  color: #676767;
}

.form-row select,
.form-row input[type="file"] {
  width: 100%;
  height: 31px;
  /* Match the height of text inputs in the login page */
  border: 1px solid #ccc;
  border-radius: 4px;
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

button[type="submit"] {
  padding: 10px 0;
  width: 100%;
  /* Set the button width to 100% of its parent container */
  background: linear-gradient(to bottom right, #5DADE2);
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 15px;
}

.accordion-button {

  color: #100101;
}

.accordion-button:hover {
  background-color: #0056b3;
  color: #ffffff;
}
</style>

<script>
import {competitionChoiceOptions,adminUploadTypeOptions } from "../dropdownOptions";
import {UPLOAD_ADMIN_FILES_BASE_URL,VIEW_ALL_ADMIN_FILES_BASE_URL,DOWNLOAD_ADMIN_FILE_IDC_BASE_URL} from '@/api';
import axios from "axios";
import Vue from 'vue';
import Swal from 'sweetalert2';

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      adminUploadTypeOptions : adminUploadTypeOptions,
      selectedFile: null,
      selectedComp:'',
      selectedUploadType:'',
      downloadFileList: [],
    };
  },
  computed: {
CompTypeTextMap() {
    // Define a mapping of age group values to their corresponding text

    const compTypeTextMap = {
      'GAC': 'Grand Arena',
      'IDC': 'Innovation Design Challenge',
    };
    return compTypeTextMap;
  },

    fileTypeTextMap() {
    // Define a mapping of age group values to their corresponding text

    const fileTypeTextMap = {
      'GM': 'Game Manual',
      'TT': 'Time Table',
    };
    return fileTypeTextMap;
  }
  },
  methods: {

    onFileChange(event) {
      // Handle the file change event here
      this.selectedFile = event.target.files[0];
      console.log("Selected File:", this.selectedFile);
      // You can perform any further processing with the selected file here
    },
    async upload() {
      console.log(this.selectedUploadType,' ',this.selectedComp);
      let token='';
      if (!this.selectedFile) {
        console.log("No file selected.");
         Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Please upload a file.',
            });
        return;
      }
       if (Vue.$keycloak && Vue.$keycloak.token && Vue.$keycloak.token.length > 0) {
            token = Vue.$keycloak.token;
          } else {
            token = "mockedToken";//for unit test
          }

      const newFileName = `${this.selectedComp}-${this.selectedUploadType}-${this.selectedFile.name}`;

      const formData = new FormData();
        formData.append("files", this.selectedFile, newFileName);
        formData.append("localFilePath",this.selectedFile.name);

      try {
        const requestBody = {
          localFilePath: this.selectedFile, // Using the file name here
          fileContent: null // This will hold the file content
        };

        // Read the file content using FileReader
        const reader = new FileReader();

        // Define the onload event handler for the reader
        reader.onload = async (event) => {
          requestBody.fileContent = event.target.result;
          console.log('requestBody:', requestBody.fileContent);

          const response = await axios.post(`${UPLOAD_ADMIN_FILES_BASE_URL}`, formData, {
                  headers: {
                  "Content-Type": "multipart/form-data",
                  "Authorization": `Bearer ${token}`
                }});
        };

        // Read the file as an ArrayBuffer (binary data)
        reader.readAsArrayBuffer(this.selectedFile);

         // Show a success message to the user using VueSweetalert2
          Swal.fire({
          title: 'Success!',
          text: 'Upload successful!',
          icon: 'success',
          timer: 2000, // Display the success message for 2 seconds
        });
        this.selectedFile=null;
      } catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
    },
    async viewDownload() {
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
      try {
        const response = await axios.get(`${VIEW_ALL_ADMIN_FILES_BASE_URL}`, { headers });
        const originalArray = response.data.data;

        // Remove the "participants/" prefix from each item
        const interimArray = originalArray.map((item) => {
          // Use string manipulation to remove the prefix
          return item.replace('admin/', '');
        });

        this.downloadFileList = interimArray.filter((item) => {
        const parts = item.split('-');
        if (parts.length > 1) {
          // Check if the first part of the filename matches the prefix
          return parts[0] === 'GAC' || parts[0] === 'IDC';
        }
        return false;
      });
        //this.showDownloadModal = true; // Show the modal after fetching the users
      }
      catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    downloadFile(file) {
      axios({
  url: `${DOWNLOAD_ADMIN_FILE_IDC_BASE_URL}/${file}`,
  method: 'POST',
  responseType: 'blob',
  headers: {
    'Authorization': `Bearer ${Vue.$keycloak.token}`
  },
})
.then((res) => {
  // Get the file type (MIME type) from the response Blob
  const fileType = res.data.type;

  // Extract the filename from the URL or generate it dynamically
  const urlParts = res.config.url.split('/');
  const filename = urlParts[urlParts.length - 1];

  // Create a Blob from the response data
  const blob = new Blob([res.data], { type: fileType });

  // Create a URL for the Blob
  const url = window.URL.createObjectURL(blob);

  // Create a link element
  const link = document.createElement('a');
  link.href = url;

  // Set the download attribute to the extracted/generated filename
  link.setAttribute('download', filename);

  // Trigger the download
  document.body.appendChild(link);
  link.click();

  // Clean up
  window.URL.revokeObjectURL(url);
  document.body.removeChild(link);
})
.catch((error) => {
  console.error('Error downloading file:', error);
  // Handle the error here (e.g., show an error message to the user)
});
    },
  },
};
</script>