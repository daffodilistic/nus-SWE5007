<template>
  <div class="upload">
    <form @submit.prevent="onSubmit" class="upload-type">
      <div class="form-row">
        <label for="game-type"><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Competition Choice</label>
        <select name="game-type" id="game-type" class="white-background">
          <option value="" disabled selected>Select an option</option>
          <!-- Use v-for to loop through competitionChoiceOptions -->
          <option v-for="option in competitionChoiceOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
        </select>
      </div>
      <br>
      <div class="form-row">
        <label for="upload-type"><i class="fas fa-file" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Document Category</label>
        <select name="upload-type" id="upload-type" class="white-background">
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
      <button type="submit"><i class="fas fa-upload"></i> Upload</button>
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

export default {
  data() {
    return {
      competitionChoiceOptions: competitionChoiceOptions,
      adminUploadTypeOptions : adminUploadTypeOptions,
    };
  },
  methods: {
    onFileChange(event) {
      // Handle the file change event here
      const selectedFile = event.target.files[0];
      console.log("Selected File:", selectedFile);
      // You can perform any further processing with the selected file here
    },
    onSubmit() {
      // Handle form submission here, if needed
    },
  },
};
</script>
