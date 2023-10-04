<template>
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
      <br><br>
      <button type="submit" @click="upload()"><i class="fas fa-upload"></i> Upload</button>
    </form>
  </div>
</template>

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

<script>
import {competitionChoiceOptions,adminUploadTypeOptions } from "../dropdownOptions";
import {UPLOAD_ADMIN_FILES_BASE_URL} from '@/api';
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
      selectedUploadType:''
    };
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
      } catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
    },
  },
};
</script>
