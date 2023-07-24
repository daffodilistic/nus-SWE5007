<template>
  <div id="app">
    <div class="form-container">
      <div class="form-group select">
        <label><i class="fas fa-trophy" style='color: rgb(65, 127, 202)'></i></label>
        <label class="red-asterisk">*</label>
        <label class="label-color">Competition Choice:</label>
        <select v-model="competitionChoice" id="category" class="custom-select">
          <option value="" disabled selected>Select Competition Choice</option>
          <option v-for="option in competitionChoiceOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
        </select>
      </div>

      <div class="form-group select">
      <label><i class="fas fa-user-tag" style='color: rgb(65, 127, 202)'></i></label>
      <label class="red-asterisk">*</label>
      <label class="label-color">Age Group:</label>
      <select v-model="ageGroup" id="category" class="custom-select">
        <option value="" disabled selected>Select Age Group</option>
        <option v-for="option in ageGroupOptions" :value="option.value" :key="option.value">{{ option.text }}</option>
      </select>
      </div>

      <div class="form-group">

        <label><i class="far fa-id-card fa-lg" style='color: rgb(65, 127, 202)'></i></label> <label class="red-asterisk">*</label>
        <label class="label-color">Team Name:</label>
        <input v-model="teamName" type="text" placeholder="Enter Team Name" />
      </div>


    </div>

    <br />
    <EditableTable
      v-model="users"
      :fields="fields"
      :categories="categories"
      @submit="handleUpdateUser($event)"
      @remove="handleRemoveUser($event)"
    ></EditableTable>
    <br><br>
    <button class="submit-button" v-on:click="register()">Register</button>
    <pre class="json-display">{{ JSON.stringify(newTeam, null, 2) }}</pre>
  </div>
</template>

<script>
import Vue from 'vue';
import EditableTable from "@/components/RegisterEditableTable.vue";
import { addUser, deleteUser, getUsers, updateUser } from "@/services/user";
import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import { v4 as uuidv4 } from 'uuid'; // Import the uuidv4 function from the uuid library
Vue.use(VueSweetalert2);
import { ageGroupOptions, competitionChoiceOptions } from "../dropdownOptions";

export default {

  components: {
    EditableTable,
  },
  data() {
    return {
      teamName: "",
      ageGroup: "",
      competitionChoice: "",
      data:[],
      country: null,
      fields: [
        { key: "firstName", label: "First Name", type: "text", required: true },
        { key: "lastName", label: "Last Name", type: "text", required: true },
        { key: "email", label: "Email", type: "email", required: true, pattern: ".+@bootstrapvue\\.com" },
        { key: "phone", label: "Phone", type: "tel", required: true },
        { key: "country", label: "Country", type: "select", required: true },
        { key: "state", label: "State", type: "select", required: true },
        { key: "dateOfBirth", label: "Birthday", type: "date", required: true },
        { key: "schoolName", label: "School Name", type: "text" },
        { key: "yearsOfExp", label: "Experience (Year)", type: "number" },
        { key: "edit", label: "Actions", type: "edit" },
      ],

      users:[],
      newTeam:[],
    };
  },
  computed: {
    ageGroupOptions() {
      return ageGroupOptions;
    },
    competitionChoiceOptions() {
      return competitionChoiceOptions;
    },
  },
  methods: {
    async handleUpdateUser(user) {
    if (!user.id) {
      // Add UUID to new users
      user.id = uuidv4();
    }
    if (user.id) {
      await updateUser(user);
    } else {
      await addUser(user);
    }
  },
    async handleRemoveUser(user) {
      if (user.length > 0) {
        await user.map(async (item) => {
          await deleteUser(item.id);
        })
      } else {
        await deleteUser(user.id);
      }
    },
    removeExtraProperty() {
      this.users = this.users.map(user => {
        // Create a new object without the isEdit property
        const { isEdit,edit, ...rest } = user;
        return rest;
      });
    },
    register() {
      // Create a new team object with form data
      this.removeExtraProperty();

      const newTeamData = {
        teamName: this.teamName,
        ageGroup: this.ageGroup,
        competitionChoice: this.competitionChoice,
        userResponses: this.users.map(user => ({ ...user, id: uuidv4() })),
      };

      // Push the new team object to the newTeam array
      this.newTeam.push(newTeamData);

      // Reset the form data to clear the input fields after registration
      //this.teamName = null;
      //this.ageGroup = null;
      //this.competitionChoice = null;
    },

  },
  mounted() {
    this.removeExtraProperty(); // Call the method when the component is mounted
  }

};
</script>
<style>
#app {
  padding: 20px;
  font-family: Arial, sans-serif;
  color: #333;

}

.form-container {
  margin-top: 50px;
  display: flex;

  flex-wrap: wrap;

  justify-content: center;
}

.form-group {
  display: flex;
  align-items: center;
  margin-right: 50px;
}

.form-group label {
  margin-right: 5px;
  font-weight: bold;
  font-size: 18px;
}

.form-group input,
.form-group select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 18px;
}

.json-display {
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
  max-width: 100%;
}

.submit-button {
  padding: 10px 20px;
  background-color: #5DADE2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.red-asterisk {
  color: red;
}

.label-color {
  color: #676767;
}

/* Style the select element to set the placeholder text color */
/* You can adjust the color value as desired */
.custom-select {
  color: #888; /* Placeholder text color */
}

/* Style the option elements to set the color when an option is selected */
.custom-select option {
  color: #000000; /* Selected option text color */
}
</style>