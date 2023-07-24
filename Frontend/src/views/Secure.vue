<template>
  <div>
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
          <th>Team Size</th>
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
          <td>{{ team.userResponses.length }}</td>

          <td v-if="!team.editing">
            {{ team.status }} <!-- Display text instead of value -->
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
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, userIndex) in team.userResponses" :key="userIndex" class="child-row">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td>{{ userIndex + 1 }}</td>
                  <td>{{ user.firstName }}</td>
                  <td>{{ user.lastName }}</td>
                  <td>{{ user.email }}</td>
                  <td>{{ user.phone }}</td>
                  <td>{{ user.country }}</td>
                  <td>{{ user.state }}</td>
                  <td>{{ formatDate(user.dateOfBirth) }}</td>
                  <td>{{ user.schoolName }}</td>
                  <td>{{ user.yearsOfExp }}</td>
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
  </div>
  </div>
</template>


<script>
import axios from "axios";
import { ageGroupOptions, competitionChoiceOptions, countriesOptions, idcQualificationOptions,gameArenaQualificationOptions } from "../dropdownOptions";

export default {
  created() {
    this.fetchData();
  },
  data() {
    return {
      searchQuery: '',
      teams:[],
      activeRow: null,
      itemsPerPage: 10, // Number of teams per page
      currentPage: 1, // Current page
      editingStatus: null,
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
      return idcQualificationOptions;
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
  },async mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get("http://localhost:3000/teams");
        this.teams = response.data; // Assuming the data is an array of teams
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    toggleRow(index) {
      this.activeRow = this.activeRow === index ? null : index;
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
        team.status = team.editingStatus;
        team.editing = false;
      } else {
        // Enter editing mode
        team.editingTeamName = team.teamName;
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

/* Styles for the dropdown in editing mode */
.editing-dropdown {
  min-width: 150px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px;
  font-size: 14px;
}


</style>