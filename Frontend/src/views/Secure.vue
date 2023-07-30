<template>
  <div>
     <!-- Add Member Modal -->
     <b-modal
      v-model="showAddMemberModal"
      title="Add Member"
      modal-class="custom-modal"
    >
     <!-- Search bar for filtering users -->
     <div class="search-container-modal">
        <b-icon icon="search" style="color: rgb(65, 127, 202)"></b-icon>
        <input
          type="text"
          v-model="searchQueryModal"
          placeholder="Search User Name"
          class="search-box"
        >
      </div>
        <!-- List of users to be displayed inside the modal -->
        <table class="modal-table">
              <thead>
                {{ selectedUsers  }}
                <tr>
                  <th></th>
                  <th>Select</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Country</th>
                  <th>State</th>
                  <th>Birthday</th>
                  <th>School Name</th>
                  <th>Experience <br>(Year)</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, userIndex) in filteredModalUserList" :key="user.id" @click="addMember(user)">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td>
                  <input
                    type="checkbox"
                    :checked="selectedUsers.includes(user)"
                    @change="toggleUserSelection(user)"
                  />
                </td>
                  <!--<td>{{ userIndex + 1 }}</td>-->
                  <td>
                    {{ user.firstName }}
                  </td>
                  <td>
                    {{ user.lastName }}
                  </td>
                  <td>
                    {{ user.email }}
                  </td>
                  <td>
                    {{ user.phone }}
                  </td>
                  <td>
                    {{ user.country }}
                  </td>
                  <td>
                    {{ user.state }}
                  </td>
                  <td>
                    {{ user.dateOfBirth }}
                  </td>
                  <td>
                    {{ user.schoolName }}
                  </td>
                  <td>
                    {{ user.yearsOfExp }}
                  </td>
                </tr>
              </tbody>
            </table>
    </b-modal>
    <br><br>
    <div class="search-container">
      <table>
        <tr>
          <td><p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
          <td><input type="text" v-model="searchQuery" placeholder="Search Team Name" class="search-box"></td>
        </tr>
      </table>
    </div>

    <p>Showing {{ startIndex }} to {{ endIndex }} of {{ totalRecords }} records</p>
    <br>
    <table class="main-table">
      <thead>
        <tr>
          <th></th>
          <th>Team Name</th>
          <th>Age Group</th>
          <th>Competition Choice</th>
          <th>Teacher Name</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody v-for="(team, index) in paginatedTeams" :key="index">
        <tr :class="{'parent-row': true, 'active-row': activeRow === index}" @click="toggleRow(index)">
          <td>
            <i :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'" class="expand-icon" @click="toggleRow(index)"></i>

          </td>
          <td v-if="!team.editing">
            {{ team.teamName }}
          </td>
          <td v-else>
            <input type="text" v-model="team.editingTeamName" class="form-control editing-textbox" />
          </td>

          <td v-if="!team.editing">
            {{ team.ageGroup }} <!-- Display text instead of value -->
          </td>
          <td v-else>
            <b-form-select v-model="team.editingAgeGroup" :options="filteredAgeGroupChoices" class="editing-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Age Group</option>
              </template>
            </b-form-select>
          </td>

          <td v-if="!team.editing">
            {{ team.competitionChoice }}
          </td>
          <td v-else>
            <b-form-select v-model="team.editingCompetitionChoice" :options="filteredCompetitionChoices" class="editing-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Competition Choice</option>
              </template>
            </b-form-select>
          </td>

          <td v-if="!team.editing">
            {{ team.teacherName }}
          </td>
          <td v-else>
            <input type="text" v-model="team.editingteacherName" class="form-control editing-textbox" />
          </td>
          <!--<td>{{ team.userResponses.length }}</td>-->

          <td v-if="!team.editing">
            {{ team.status }}
          </td>
          <td v-else>
            <b-form-select v-model="team.editingStatus" :options="filteredStatusChoices" class="editing-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Status</option>
              </template>
            </b-form-select>
          </td>

          <td>
            <!-- Edit Icon -->

            <b-button @click="editTeam(index)" variant="outline-primary">
            <span v-if="!team.editing"><b-icon icon="pencil"></b-icon></span>
            <span v-else><b-icon icon="save"></b-icon></span>
          </b-button>

            <!-- Delete Icon -->
            <b-button
            class="delete-button"
            variant="outline-primary"
            @click="deleteTeam(index)"
            ><b-icon icon="trash"></b-icon></b-button>
          </td>
        </tr>
        <!-- Child rows -->
        <tr v-if="activeRow === index" class="child-row">
          <td :colspan="10"> <!-- Use colspan to span all columns in the row -->
            <table class="user-table">
              <thead>
                <tr>
                  <th></th>
                  <th>No.</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Country</th>
                  <th>State</th>
                  <th>Birthday</th>
                  <th>School Name</th>
                  <th>Experience (Year)</th>
                  <th><button @click="fetchUsers(team.id)" class="add-member-button">Add Member</button></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, userIndex) in team.userResponses" :key="userIndex" class="child-row">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td>{{ userIndex + 1 }}</td>
                  <td>
                    test
                    {{ user.firstName }}
                  </td>
                  <td>
                    {{ user.lastName }}
                  </td>
                  <td>
                    {{ user.email }}
                  </td>
                  <td>
                    {{ user.phone }}
                  </td>
                  <td>
                    {{ user.country }}
                  </td>
                  <td>
                    {{ user.state }}
                  </td>
                  <td>
                    <input v-if="editingChildRow === userIndex" v-model="user.dateOfBirth" type= "date" class="form-control editing-textbox" />
                    <span v-else>{{ user.dateOfBirth }}</span>
                  </td>
                  <td>
                    <input v-if="editingChildRow === userIndex" v-model="user.schoolName" class="form-control editing-textbox" />
                    <span v-else>{{ user.schoolName }}</span>
                  </td>
                  <td>
                    <input v-if="editingChildRow === userIndex" v-model="user.yearsOfExp" min = 0 type= "number" class="form-control editing-textbox" />
                    <span v-else>{{ user.yearsOfExp }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
    <button @click="gotoPage(currentPage - 1)" :disabled="currentPage === 1" class="page-button">
      <i class="fas fa-chevron-left icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Previous" icon -->
    </button>
    <span>Page {{ currentPage }} of {{ totalPages }}</span>
    <button @click="gotoPage(currentPage + 1)" :disabled="currentPage === totalPages" class="page-button">
      <i class="fas fa-chevron-right icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Next" icon -->
    </button>
    <pre class="json-display">{{ JSON.stringify(teams, null, 2) }}</pre>
  </div>
  </div>
</template>


<script>
import axios from "axios";
import { ageGroupOptions, competitionChoiceOptions, QualificationOptions,countriesOptions,statesOptions } from "../dropdownOptions";

export default {

  data() {
    return {
      selectedUsers: [],
      searchQuery: '',
      searchQueryModal: '',
      teams:[],
      activeRow: null,
      itemsPerPage: 10, // Number of teams per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      showAddMemberModal: false,
      userList: [],
      newChildRow: {
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        country: "",
        state: "",
        birthday: "",
        schoolName: "",
        experience: "",
      },
    };
  },
  computed: {
    filteredTeams() {
      // If the search query is empty, show all teams
      if (this.searchQuery.trim() === '') {
        return this.teams;
      }

      // Otherwise, filter teams based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.teams.filter((team) => team.teamName.toLowerCase().includes(query));
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    filteredAgeGroupChoices() {
      return ageGroupOptions;
    },
    filteredStatusChoices() {
      return QualificationOptions;
    },
    totalPages() {
      return Math.ceil(this.filteredTeams.length / this.itemsPerPage);
    },
    paginatedTeams() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredTeams.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredTeams.length;
    },
    filteredModalUserList() {
    if (this.searchQueryModal.trim() === '') {
      return this.userList;
    }

    const query = this.searchQueryModal.trim().toLowerCase();
    return this.userList.filter(
      (user) =>
        user.firstName.toLowerCase().includes(query) ||
        user.lastName.toLowerCase().includes(query)
    );
  },
  },
  async mounted() {
    try {
      this.teamsData = await axios.get('http://localhost:3000/teams');
      this.teams = this.teamsData.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {

    async fetchUsers(teamId) {
      try {
        const response = await axios.get("api/userinfo/users");
        this.userList = response.data.data;
        this.showAddMemberModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },

   /* async fetchTeamMembers(team) {
      const requestBody = {
        id: team.id // Assuming the server expects a "teamId" property in the request body
      };
      try {
    const response = await axios.get("/api/idcteam/team", {
      headers: {
        "Content-Type": "application/json" // Set the Content-Type header to indicate JSON data
      },
      data: JSON.stringify(requestBody) // Convert the requestBody to JSON string
       });

        team.userResponses = response.data; // Assuming the response contains an array of team members
        console.log(`success GET URL for fetching team members: /api/idcteam/team, Request Body: ${JSON.stringify(requestBody)}`);
      } catch (error) {
        console.error('Error fetching team members:', error);
        console.log(`fail GET URL for fetching team members: /api/idcteam/team, Request Body: ${JSON.stringify(requestBody)}`);
      }
    },*/

  async toggleRow(index) {
    if (this.activeRow === index) {
      this.activeRow = null; // Collapse the row if it's already expanded
    } else {
      this.activeRow = index;
      const team = this.filteredTeams[index];
     // await this.fetchTeamMembers(team);
    }
  },
    gotoPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
    formatDate(dateString) {
      // Create a Date object from the given dateString
      const date = new Date(dateString);

      // Extract day, month, and year from the Date object
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
      const year = date.getFullYear();

      // Return the formatted date as "dd/mm/yyyy"
      return `${day}/${month}/${year}`;
    },
    // Method to toggle editing mode for a team
    editTeam(index) {
      const team = this.filteredTeams[index];
      if (team.editing) {
        // Save the changes
        team.teamName = team.editingTeamName;
        team.ageGroup = team.editingAgeGroup;
        team.competitionChoice = team.editingCompetitionChoice;
        team.teacherName = team.editingteacherName;
        team.status = team.editingStatus;
        team.editing = false;
      } else {
        // Enter editing mode
        team.editingTeamName = team.teamName;
        team.editingteacherName = team.teacherName;
        team.editingAgeGroup = team.ageGroup;
        team.editingCompetitionChoice = team.competitionChoice;
        team.editingStatus = team.status;
        team.editing = true;
      }
    },

    // Method to delete a team
    deleteTeam(index) {
      if (confirm("Are you sure you want to delete this team?")) {
        this.filteredTeams.splice(index, 1);
      }
    },
  // Method to add a new child row
    addChildRow(teamIndex) {
      const team = this.filteredTeams[teamIndex];
      team.userResponses.push({ ...this.newChildRow });
      // Set the initial country and state values for the new child row
      const newUserIndex = team.userResponses.length - 1;
      const newUser = team.userResponses[newUserIndex];
      newUser.country = null;
      newUser.statesOptions = [];
    },
    toggleUserSelection(user) {
    if (this.selectedUsers.includes(user)) {
      this.selectedUsers = this.selectedUsers.filter((selectedUser) => selectedUser !== user);
    } else {
      this.selectedUsers.push(user);
    }
  },


  },
};
</script>

<style>
/* Main Table Styles */
.main-table {

  width: 100%;
  border-collapse: collapse;
  font-size: 16px;
  text-align: left;
  margin-bottom: 20px;
}

.main-table th,
.main-table td {
  padding: 10px 20px;
  border-bottom: 1px solid #ccc;
  text-align: center;
}

.main-table th {

}

/* Nested Table Styles */
.user-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.user-table th,
.user-table td {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
}

.user-table th {
  background-color: ;
}

/* Toggle Row Styling */
.nested-row {
  display: table-row;
  text-align: center; /* Center the text content horizontally */
}

.nested-row td {
  border: none;
  padding: 0;
  text-align: center; /* Center the text content horizontally */
  vertical-align: middle; /* Center the content vertically within the cell */
}

/* Hover effect for clickable rows */
.main-table tbody tr:hover {
  background-color: #f2f2f2;
  cursor: pointer;
}

.parent-row {
  background-color: ;
  cursor: pointer;
}

.active-row {
  background-color: rgb(218, 234, 253)/* Light blue for active parent row */
}

.child-row {
  background-color: rgb(244, 249, 255);
}

/* Pagination Styles */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}

.page-button {
  padding: 8px 16px;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin: 0 5px;
  background-color: transparent;
}

.search-container {
  display: flex;
  justify-content: flex-end; /* Aligns the search box to the right */
  margin-bottom: 20px;
  margin-right:65px;
}

/* Search Box Styles */
.search-box {
  padding: 8px 16px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* Adjust the width as needed */
}

.page-button .icon {
  background-color: transparent;
  border: none;
  padding: 0; /* Remove padding to make the icons fit better */
  font-size: 16px;
}

.parent-row .expand-icon {
  margin-right: 10px; /* Add some spacing between the icon and text */
  font-size: 16px;
  color: rgb(65, 127, 202); /* Set the color to blue (#3498db) */
}

.main-table th:first-child,
.main-table td:first-child {
  /* Set a fixed width for the new column */
  width: 30px;
}
.parent-row .expand-icon {
  /* Adjust the icon's size, margin, and color as needed */
  font-size: 16px;
  margin-right: 10px;
  color: rgb(65, 127, 202);
}

.delete-button {
  margin-left: 5px;
}

/* Styles for the text box in editing mode */
input.form-control.editing-textbox {
  font-size: 14px;
  min-width: 100px;
}

.custom-modal {
  max-width: 100%; /* Optionally set a max-width for the modal */
  width: 100% !important; /* Set the modal width to take full width of the viewport */
}

.modal-dialog {
  max-width: none !important; /* Ensure there are no constraints on modal's width */
  margin: 0 auto; /* Center the modal horizontally */
}

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */
  /* max-width: 800px; */
}

</style>