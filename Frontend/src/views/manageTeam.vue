<template>
  <div>
     <!-- Add Member Modal -->

     <b-modal
      v-model="showAddMemberModal"
      modal-class="custom-modal"
      hide-footer
    >
     <!-- Search bar for filtering users -->
     <div class="search-container">
        <p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p>&nbsp;&nbsp;
        <input
          type="text"
          v-model="searchQueryModal"
          placeholder="Search User Name"
          class="search-box"
        >
      </div>

      <p>
        Showing {{ (currentPageModal - 1) * itemsPerPageModal + 1 }}
        to {{ Math.min(currentPageModal * itemsPerPageModal, totalRecordsModal) }}
        of {{ totalRecordsModal }} records
      </p>

      <div class="pagination">
        <button @click="gotoPageModal(currentPageModal - 1)" :disabled="currentPageModal === 1" class="page-button">
          <i class="fas fa-chevron-left icon" style='color: rgb(65, 127, 202)'></i>
        </button>
        <span>Page {{ currentPageModal }} of {{ totalPagesModal }}</span>
        <button @click="gotoPageModal(currentPageModal + 1)" :disabled="currentPageModal === totalPagesModal" class="page-button">
          <i class="fas fa-chevron-right icon" style='color: rgb(65, 127, 202)'></i>
        </button>
      </div><br>

        <!-- List of users to be displayed inside the modal -->
        <table class="modal-table">
              <thead>
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
                <tr v-for="(user, userIndex) in paginatedModalUserList" :key="user.id">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td>
                  <input
                    type="checkbox"
                    :checked="selectedUsers.includes(user.id)"
                    @change="toggleUserSelection(user.id)"
                  />
                </td>
                  <!--<td>{{ userIndex + 1 }}</td>-->
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
                <tr></tr>
              </tbody>

            </table>
            <div class="text-center">
              <button @click="addMembersToTeam" class="add-member-button">Save</button>
            </div>
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
    <div v-if="teams && teams.length > 0">
    <p>Showing {{ startIndex }} to {{ endIndex }} of {{ totalRecords }} records</p>
    <div class="pagination">
      <button @click="gotoPage(currentPage - 1)" :disabled="currentPage === 1" class="page-button">
        <i class="fas fa-chevron-left icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Previous" icon -->
      </button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button @click="gotoPage(currentPage + 1)" :disabled="currentPage === totalPages" class="page-button">
        <i class="fas fa-chevron-right icon" style='color: rgb(65, 127, 202)'></i> <!-- Font Awesome "Next" icon -->
      </button>
    </div>
    <table class="main-table">
      <thead>
        <tr>
          <th></th>
          <th>Team Name</th>
          <th>Age Group</th>
          <th>Teacher Name</th>
          <th>Qualification Status</th>
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
            {{ ageGroupTextMap[team.ageGroup] }}
          </td>
          <td v-else>
            <b-form-select v-model="team.editingAgeGroup" :options="filteredAgeGroupChoices" class="editing-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Age Group</option>
              </template>
            </b-form-select>
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
            <p v-if="team.isQualifiedFinalSecondStage">
              Qualified Final Second Stage
            </p>
            <p v-else-if="team.isQualifiedFinal">
              Qualified Final First Stage
            </p>
            <p v-else-if="team.isQualifiedPromo">
              Qualified Promotional Round
            </p>
            <p v-else>
              Not qualified
            </p>
          </td>
          <td v-else>
            <b-form-select v-model="team.editingStatus" :options="filteredStatusChoices" class="editing-dropdown">
              <template v-slot:first>
                <option :value="null" disabled>Select Status</option>
              </template>
            </b-form-select>
          </td>

          <td>
            <b-button variant="outline-primary" @click="fetchUsers(team.id,team.teamName)" >
              <b-icon icon="person-plus" ></b-icon>
            </b-button>
            <!-- Edit Icon -->
            <b-button @click="editTeam(index)" variant="outline-primary" class="delete-button">
              <span v-if="!team.editing"><b-icon icon="pencil"></b-icon></span>
              <span v-else><b-icon icon="save"></b-icon></span>
            </b-button>
            <!-- Delete Icon -->
            <b-button
            class="delete-button"
            variant="outline-primary"
            @click="deleteTeam(index)"
            >
              <b-icon icon="trash"></b-icon>
            </b-button>
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
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, userIndex) in team.userResponses" :key="userIndex" class="child-row">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
  <div v-else>
      <!-- Show a loading message or spinner while the data is being fetched -->
      <div class="loader-container">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import { ageGroupOptions, competitionChoiceOptions, QualificationOptions,countriesOptions,statesOptions } from "../dropdownOptions";
import { IDC_TEAM_BASE_URL, USER_INFO_BASE_URL } from '@/api';

export default {
  head() {
    return {
      link: [
        {
          rel: 'stylesheet',
          href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css',
          integrity: 'sha512-...',
          crossorigin: 'anonymous',
          referrerpolicy: 'no-referrer',
        },
      ],
    }
  },

  data() {
    return {
      currentPageModal: 1,
      itemsPerPageModal: 10,
      currentTeamId: "",
      currentTeamName: "",
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
    totalRecordsModal() {
    return this.filteredModalUserList.length;
  },
    filteredTeams() {
    // If the teams data is not available yet, return an empty array
    if (!this.teams || this.teams.length === 0) {
      return [];
    }

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
  if (!this.userList) {
    return [];
  }

  // Apply search query filter
  const query = this.searchQueryModal.trim().toLowerCase();
  if (!query) {
    return this.userList; // Return the entire user list if the query is empty
  }

  return this.userList.filter((user) => {
    // Check if user.firstName and user.lastName are defined before using toLowerCase()
    const firstName = user.firstName ? user.firstName.toLowerCase() : '';
    const lastName = user.lastName ? user.lastName.toLowerCase() : '';

    return firstName.includes(query) || lastName.includes(query);
  });
},
  paginatedModalUserList() {
    const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
    const endIndex = startIndex + this.itemsPerPageModal;
    return this.filteredModalUserList.slice(startIndex, endIndex);
  },
  totalPagesModal() {
    return Math.ceil(this.filteredModalUserList.length / this.itemsPerPageModal);
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
    try {
      this.teamsData = await axios.get(`${IDC_TEAM_BASE_URL}idcteam/teams`);
      this.teams = this.teamsData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    async fetchUsers(teamId,teamName) {
      try {
        const response = await axios.get(`${USER_INFO_BASE_URL}userinfo/users`);
        this.currentTeamId = teamId;
        this.userList = response.data.data;
        this.currentTeamName = teamName;
        this.showAddMemberModal = true; // Show the modal after fetching the users
      }
      catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    gotoPageModal(page) {
    if (page >= 1 && page <= this.totalPagesModal) {
      this.currentPageModal = page;
    }
  },

    async toggleRow(index) {
    if (this.activeRow === index) {
      this.activeRow = null; // Collapse the row if it's already expanded
    } else {
      this.activeRow = index;
      const team = this.filteredTeams[index];

      try {
        // Make the API call here using the team ID as the request body
        const requestBody = {
          id: team.id,
        };

        // Make the HTTP PUT request to the API endpoint
        const requestBodyJson = JSON.stringify(requestBody);
        console.log('Request Body:', requestBodyJson);

        const response = await axios.get(`${IDC_TEAM_BASE_URL}idcteam/team`, requestBodyJson);

        // Handle the response, if needed
        console.log('Response from server:', response.data);

        // Optional: Perform any additional actions, such as updating the UI.
      } catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }


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
      async editTeam(index) {
        const team = this.filteredTeams[index];
        if (team.editing) {
          // Save the changes
          team.teamName = team.editingTeamName;
          team.ageGroup = team.editingAgeGroup;
          team.competitionChoice = team.editingCompetitionChoice;
          team.teacherName = team.editingteacherName;
          team.status = team.editingStatus;
          team.editing = false;

          team.isQualifiedPromo = team.editingStatus

          const requestBody = {
            id: team.id,
            isQualifiedPromo: team.isQualifiedPromo
          };

          console.log('Request Payload 222:', requestBody);

          try {
            // Make the HTTP POST request to the API endpoint
            const response = await axios.put(`${IDC_TEAM_BASE_URL}idcteam/team`, requestBody);

            // Handle the response, if needed
            console.log('Response from server:', response.data);

            // Close the modal after the request is successful
            this.showAddMemberModal = false;

            // Optional: Perform any additional actions, such as updating the UI.
          } catch (error) {
            // Handle errors, if any
            console.error('Error adding members to team:', error);
          }
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
      toggleUserSelection(userId) {
      if (this.selectedUsers.includes(userId)) {
        this.selectedUsers = this.selectedUsers.filter((selectedUserId) => selectedUserId !== userId);
      } else {
        this.selectedUsers.push(userId);
      }
      const jsonPayload = {
        userIds: this.selectedUsers,
      };
      console.log(jsonPayload)
    },

    async addMembersToTeam() {
    // Make sure currentTeamId and selectedUsers are defined
    if (!this.currentTeamId || this.selectedUsers.length === 0) {
      console.error('Team ID or selected users not available.');
      return;
    }

    // Prepare the payload to be sent in the request body
    const requestBody = {
      id: this.currentTeamId,
      teamName : this.currentTeamName,
      userIds: this.selectedUsers,
    };

    console.log('Request Payload:', requestBody);

    try {
      // Make the HTTP POST request to the API endpoint
      const response = await axios.put(`${IDC_TEAM_BASE_URL}idcteam/team`, requestBody, {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
          "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token, Authorization, Accept,charset,boundary,Content-Length,X-Requested-With"
        },
      });

      // Handle the response, if needed
      console.log('Response from server:', response.data);

      // Close the modal after the request is successful
      this.showAddMemberModal = false;

      // Optional: Perform any additional actions, such as updating the UI.
    } catch (error) {
      // Handle errors, if any
      console.error('Error adding members to team:', error);
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



.active-row {
  background-color: rgb(218, 234, 253)/* Light blue for active parent row */
}

.child-row {
  background-color: rgb(244, 249, 255);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* Add some top margin for spacing */
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

/* Modal Styles */
.custom-modal .modal-dialog  {
    max-width: 1300px; /* Set the max width of the modal */
    text-align: center;
  }

/* Center the modal title */
.custom-modal .modal-header {
  text-align: center;
}

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */
  /* max-width: 800px; */
}

.add-member-button {
  padding: 10px 20px;
  background-color: #5DADE2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;

}

.text-center {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  text-align: center;
}

.loader-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px; /* Adjust the height as needed */
}

.fa-spinner {
  font-size: 32px; /* Adjust the size as needed */
}

.modal-title-center {
  text-align: center;
  font-size: 30px; /* Adjust the font size as needed */
  margin-bottom: 10px; /* Add some margin at the bottom */
}



</style>