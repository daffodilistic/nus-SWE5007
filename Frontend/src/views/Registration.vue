<template>
  <div id="app">
    <div class="form-container">
      <table>
        <tr>
          <td>
            <div class="form-group select">
              <label
                ><i class="fas fa-trophy" style="color: rgb(65, 127, 202)"></i
              ></label>
              <label class="red-asterisk">*</label>
              <label class="label-color">Competition Choice:&nbsp;&nbsp;</label>
              <select
                v-model="competitionChoice"
                id="category"
                class="custom-select"
              >
                <option value="" disabled selected>
                  Select Competition Choice
                </option>
                <option
                  v-for="option in competitionChoiceOptions"
                  :value="option.value"
                  :key="option.value"
                >
                  {{ option.text }}
                </option>
              </select>
            </div>
          </td>
          <td>
            <div class="form-group select">
              <label
                ><i class="fas fa-user-tag" style="color: rgb(65, 127, 202)"></i
              ></label>
              <label class="red-asterisk">*</label>
              <label class="label-color"
                >Age
                Group:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label
              >
              <select v-model="ageGroup" id="category" class="custom-select">
                <option value="" disabled selected>Select Age Group</option>
                <option
                  v-for="option in ageGroupOptions"
                  :value="option.value"
                  :key="option.value"
                >
                  {{ option.text }}
                </option>
              </select>
            </div>
          </td>
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
              <label class="red-asterisk">*</label>
              <label class="label-color"
                >Team
                Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label
              >
              <input
                v-model="teamName"
                type="text"
                placeholder="Enter Team Name"
              />
            </div>
          </td>
          <td>
            <div class="form-group">
              <label
                ><i
                  class="far fa-id-card fa-lg"
                  style="color: rgb(65, 127, 202)"
                ></i
              ></label>
              <label class="red-asterisk">*</label>
              <label class="label-color">School Name:&nbsp;&nbsp;&nbsp;</label>
              <input
                v-model="schoolName"
                type="text"
                placeholder="Enter School Name"
              />
            </div>
          </td>
        </tr>
      </table>
    </div>
    <br />
    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.first-accordion
          variant="info"
          class="accordion-button"
        >
          &nbsp;&nbsp;<img
            src="../assets/teacher.jpg"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; Teachers
        </b-button>
      </b-card-header>
      <b-collapse id="first-accordion" accordion="my-accordion" role="tabpanel">
        <br />
        <EditableTable
          v-model="teachers"
          :fields="fields"
          @submit="handleUpdateUser($event)"
          @remove="handleRemoveUser($event)"
        ></EditableTable>
      </b-collapse>
    </b-card>
    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.second-accordion
          variant="info"
          class="accordion-button"
        >
          &nbsp;&nbsp;<img
            src="../assets/student.png"
            alt="Versus"
            width="35px"
            height="30px"
          />&nbsp; Participants
        </b-button>
      </b-card-header>
      <b-collapse
        id="second-accordion"
        accordion="my-accordion"
        role="tabpanel"
      >
        <br />
        <EditableTable
          v-model="users"
          :fields="fields"
          @submit="handleUpdateUser($event)"
          @remove="handleRemoveUser($event)"
        ></EditableTable>
      </b-collapse>
    </b-card>
    <br />
    <button class="submit-button" v-on:click="register()">Register</button>
  </div>
</template>

<script>
import Vue from "vue";
import EditableTable from "@/components/RegisterEditableTable.vue";
import VueSweetalert2 from "vue-sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
Vue.use(VueSweetalert2);
import { ageGroupOptions, competitionChoiceOptions } from "../dropdownOptions";
import axios from "axios";
import { api } from "../api";

export default {
  components: {
    EditableTable,
  },
  data() {
    return {
      teamName: "",
      ageGroup: "",
      competitionChoice: "",
      schoolName: "",
      data: [],
      country: null,
      fields: [
        { key: "userName", label: "User Name", type: "text", required: true },
        { key: "firstName", label: "First Name", type: "text", required: true },
        { key: "lastName", label: "Last Name", type: "text", required: true },
        {
          key: "email",
          label: "Email",
          type: "email",
          required: true,
          pattern: ".+@bootstrapvue\\.com",
        },
        { key: "phone", label: "Phone", type: "tel", required: true },
        { key: "country", label: "Country", type: "select", required: true },
        { key: "state", label: "State", type: "select", required: true },
        { key: "dateOfBirth", label: "Birthday", type: "date", required: true },
        { key: "yearsOfExp", label: "Experience (Year)", type: "number" },
        { key: "edit", label: "Actions", type: "edit" },
      ],

      users: [],
      teachers: [],
      newTeam: [],
      allUsers: [],
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
        });
      } else {
        await deleteUser(user.id);
      }
    },

    async register() {
      // Create a new team object with form data
      const userIDs = [];
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
      };

      for (const teacher of this.teachers) {
        const userData = {
          userName: teacher.userName,
          firstName: teacher.firstName,
          lastName: teacher.lastName,
          email: teacher.email,
          phone: teacher.phone,
          country: teacher.country,
          state: teacher.state,
          dateOfBirth: teacher.dateOfBirth,
          schoolName: this.schoolName,
          yearsOfExp: teacher.yearsOfExp,
        };

        // Call the create new user API
        const createUserResponse = await axios.post(
          `${api.CREATE_TEACHER_INFO_BASE_URL}`,
          userData,
          { headers }
        );
      }
      // Create new user for each user in the users array
      for (const user of this.users) {
        const userData = {
          userName: user.userName,
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          phone: user.phone,
          country: user.country,
          state: user.state,
          dateOfBirth: user.dateOfBirth,
          schoolName: this.schoolName,
          yearsOfExp: user.yearsOfExp,
        };

        // Call the create new user API
        const createUserResponse = await axios.post(
          `${api.CREATE_USER_INFO_BASE_URL}`,
          userData,
          { headers }
        );
        // Push the user ID into the userIDs array
        userIDs.push(createUserResponse.data.data.id);
      }

      let teamData = "";
      let url = "";
      let url2 = "";

      if (
        this.competitionChoice === "IDC" ||
        this.competitionChoice === "Innovation Design Challenge"
      ) {
        url = api.CREATE_IDC_TEAM_BASE_URL;
        url2 = api.ADD_MEMBER_IDC_TEAM_BASE_URL;
        teamData = {
          teamName: this.teamName,
          ageGroup: this.ageGroup,
          //teacherName: this.teacherName,
          isQualifiedPromo: false,
          isQualifiedFinal: false,
          isQualifiedFinalSecondStage: false,
        };
      } else if (
        this.competitionChoice === "GAC" ||
        this.competitionChoice === "Game Arena"
      ) {
        teamData = {
          teamName: this.teamName,
          ageGroup: this.ageGroup,
          //teacherName: this.teacherName,
          qualificationRoundScore: 0,
          qualificationRoundPoint: 0,
          qualificationRoundNumMatchesPlayed: 0,
          isQualifiedForElimination: false,
        };
        url = api.CREATE_GAME_TEAM_BASE_URL;
        url2 = api.ADD_MEMBER_GAME_TEAM_BASE_URL;
      }
      try {
        const createTeamResponse = await axios.post(`${url}`, teamData, {
          headers,
        });
        let RegisterTeamData = {};
        if (
          this.competitionChoice === "IDC" ||
          this.competitionChoice === "Innovation Design Challenge"
        ) {
          RegisterTeamData = {
            id: createTeamResponse.data.data,
            teamName: this.teamName,
            userIds: userIDs,
          };
        } else if (
          this.competitionChoice === "GAC" ||
          this.competitionChoice === "Game Arena"
        ) {
          RegisterTeamData = {
            id: createTeamResponse.data.data,
            userIds: userIDs,
          };
        }

        //register created user to team
        const registerTeamResponse = await axios.put(
          `${url2}`,
          RegisterTeamData,
          { headers }
        );
        console.log("Team Allocation successful:", registerTeamResponse.data);

        // Show a success message to the user using VueSweetalert2
        this.$swal({
          title: "Success!",
          text: "Team registration successful!",
          icon: "success",
          timer: 2000, // Display the success message for 2 seconds
        });
      } catch (error) {
        // Handle any errors that occurred during the registration process
        console.error("Error registering team:", error);

        // Show an error message to the user using VueSweetalert2
        this.$swal({
          title: "Error!",
          text: "Failed to register the team. Please try again later.",
          icon: "error",
          timer: 2000, // Display the error message for 2 seconds
        });
      }

      // Reset the form data to clear the input fields after registration
      //this.teamName = null;
      //this.ageGroup = null;
      //this.competitionChoice = null;
      //this.teacherName = null;
      //this.users = []; // Reset the users array as well
    },
  },
};
</script>
<style scoped>
#app {
  padding: 20px;
  font-family: Arial, sans-serif;
  color: #333;
}

.form-container {
  display: flex;

  flex-wrap: wrap;

  justify-content: center;
}

.form-group {
  display: flex;
  align-items: center;
  margin-right: 30px;
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
  font-size: 15px;
  width: 300px;
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
  background-color: #5dade2;
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

.instruction-table {
  text-align: left;
  font-size: 14px;
  color: #120b51;
  margin-left: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.accordion-button {
  color: #100101;
}

.accordion-button:hover {
  background-color: #0056b3;
  color: #ffffff;
}
</style>
