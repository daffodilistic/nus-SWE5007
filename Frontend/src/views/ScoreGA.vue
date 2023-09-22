<template>
      <div>
        <!-- Add Member Modal -->
        <b-modal
          v-model="showAddTeamModal"
          modal-class="custom-modal"
          hide-footer
        >
        <!-- Search bar for filtering users -->
        <div class="search-container">
            <p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p>&nbsp;&nbsp;
            <input
              type="text"
              v-model="searchQueryModal"
              placeholder="Search team Name"
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
                      <th>Select</th>
                      <th>Team Name</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(team, userIndex) in paginatedModalTeamList" :key="team.id">

                      <td>
                      <input
                        type="checkbox"
                        :checked="selectedTeams.includes(team.id)"
                        @change="toggleTeamSelection(team.id)"
                      />
                    </td>
                      <!--<td>{{ userIndex + 1 }}</td>-->
                      <td>{{ team.teamName }}</td>
                    </tr>
                    <tr></tr>
                  </tbody>
                </table>
        </b-modal>

        <!--END OF MODAL-->
        <div class="search-container">
          <table>
            <tr>
              <td><p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
              <td><input type="text" v-model="searchQuery" placeholder="Search group Name" class="search-box"></td>
            </tr>
          </table>
        </div>
        <div v-if="groups && groups.length > 0">
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
               <th>S/No</th>
              <th>Group Name</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody v-for="(group, index) in paginatedTeams" :key="index">
            <tr :class="{'parent-row': true, 'active-row': activeRow === index}" @click="group.editing ? null : toggleRow(index)">
              <td>
                <i :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'" class="expand-icon"></i>
              </td>
              <td>{{ startIndex + index }}</td>
              <td v-if="!group.editing">
                {{ group.groupName }}
              </td>
              <td v-else>
                <input type="text" v-model="group.editingGroupName" class="form-control editing-textbox" />
              </td>
              <td>
                <!-- Edit Icon -->
                <b-button @click="editGroup(startIndex + index -1)" variant="outline-primary" class="delete-button">
                  <span v-if="!group.editing"><b-icon icon="pencil"></b-icon></span>
                  <span v-else><b-icon icon="save"></b-icon></span>
                </b-button>
                <b-button @click="editGroup(startIndex + index -1)" variant="outline-primary" class="delete-button">
                 <b-icon icon="table"></b-icon>
                </b-button>
                <b-button @click="createGame(startIndex + index -1)" variant="outline-primary" class="delete-button">
                 <b-icon icon="collection-play"></b-icon>
                </b-button>
              </td>
            </tr>
            <!-- Child rows -->
            <tr v-if="activeRow === index" class="child-row">
              <td :colspan="10"> <!-- Use colspan to span all columns in the row -->
                <table class="team-table">
                  <thead>
                    <tr>
                      <th>Matchup</th>
                      <th class="longer-td">Team Name</th>
                      <th>Team Score</th>
                      <th>Game Outcome</th>
                      <th>Game Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                     <template v-for="(group, groupIndex) in groupedData">
                      <tr v-for="(item, rowIndex) in group" :key="`row-${groupIndex}-${rowIndex}`">
                        <td v-if="rowIndex === 0" :rowspan="group.length">{{ groupIndex+1}}</td>
                        <td class="longer-td">{{ item.teamName  }}</td>

                        <td v-if="rowIndex === 0 && item.gameStatus!=='done'"><input type="number" v-model="item.enteredScore" :min="0" :disabled="item.gameStatus !== 'ongoing'"></td>
                        <td v-else-if ="rowIndex === 0 && item.gameStatus==='done'">{{ item.hostScore }}</td>
                        <td v-if="rowIndex === 1 && item.gameStatus!=='done'"><input type="number" v-model="item.enteredScore" :min="0" :disabled="item.gameStatus !== 'ongoing'"></td>
                        <td v-else-if ="rowIndex === 1 && item.gameStatus==='done'">{{ item.oppoScore }}</td>


                        <td v-if="rowIndex === 0">
                          <span v-if="item.gameOutcome === 'win' && item.gameStatus==='done'"
                          >
                            <i class="fas fa-trophy gold-trophy"></i> Win
                          </span>
                          <span v-else-if="item.gameOutcome === 'lose' && item.gameStatus==='done'">
                            Lose
                          </span>
                          <span v-else>
                            Not Scored
                          </span>
                        </td>
                        <td v-if="rowIndex === 1">
                          <span v-if="item.gameOutcome === 'lose' && item.gameStatus==='done'"
                          >
                            <i class="fas fa-trophy gold-trophy"></i> Win
                          </span>
                          <span v-else-if="item.gameOutcome === 'win' && item.gameStatus==='done'">
                            Lose
                          </span>
                          <span v-else>
                            Not Scored
                          </span>
                        </td>
                        <td v-if="rowIndex === 0" :rowspan="group.length">{{ gameStatusTextMap[item.gameStatus] }}</td>
                        <td
                          v-if="rowIndex === 0 && item.gameStatus === 'pending'"
                          :rowspan="group.length"
                        >
                           <b-button @click="startGame(item.id)" variant="outline-primary" class="delete-button">
                            <b-icon icon="play-circle"></b-icon>&nbsp;Start
                            </b-button>
                        </td>

                        <td
                          v-if="rowIndex === 0 && item.gameStatus == 'ongoing'"
                          :rowspan="group.length"
                        >
                           <b-button @click="submitScore(item.id,group)" variant="outline-primary" class="delete-button">
                            <b-icon icon="save"></b-icon>&nbsp;Score
                            </b-button>
                        </td>
                        <td
                          v-if="rowIndex === 0 && item.gameStatus == 'done'"
                          :rowspan="group.length"
                        >
                          &nbsp;
                        </td>

                      </tr>
                    </template>
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
import { ageGroupOptions, competitionChoiceOptions} from "../dropdownOptions";
import { VIEW_GAME_GROUP_BASE_URL,GET_ALL_GAME_GROUP_BASE_URL,GET_ALL_GAME_TEAM_BASE_URL,CREATE_GAME_BASE_URL,GET_ALL_GAMES_BASE_URL,UPDATE_GAME_ONGOING_STATUS_BASE_URL,UPDATE_GAME_SCORE_BASE_URL} from '@/api';
import Swal from 'sweetalert2';
import Vue from 'vue'

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
      currentGroupId: "",
      currentGroupName: "",
      selectedTeams: [],
      searchQuery: '',
      searchQueryModal: '',
      groups:[],
      gameTeamResponses:[],
      activeRow: null,
      itemsPerPage: 10, // Number of groups per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      showAddTeamModal: false,
      teamList: [],
      groupMembers:[],
      objectsWithIdAndTeamArray : []
    };
  },
  computed: {

    gameStatusTextMap() {
    // Define a mapping of age group values to their corresponding text
    const gameStatusMap = {
      'pending': 'Not Yet Started',
      'ongoing': 'In-Progress',
      'done': 'Completed',
      // Add more entries as needed for other age groups
    };
    return gameStatusMap;
  },

     groupedData() {
      const grouped = {};
      this.objectsWithIdAndTeamArray.forEach((item) => {
        const id = item.id;
        if (!grouped[id]) {
          grouped[id] = [];
        }
        grouped[id].push(item);
      });

      return Object.values(grouped);
    },
    totalRecordsModal() {
      return this.filteredModalTeamList.length;

    },
    filteredGroups() {
    // If the groups data is not available yet, return an empty array
    if (!this.groups || this.groups.length === 0) {
      return [];
    }

    // If the search query is empty, show all groups
    if (this.searchQuery.trim() === '') {
      return this.groups;
    }

    // Otherwise, filter groups based on the search query
    const query = this.searchQuery.trim().toLowerCase();
    return this.groups.filter((group) => group.groupName.toLowerCase().includes(query));
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
      return Math.ceil(this.filteredGroups.length / this.itemsPerPage);
    },
    paginatedTeams() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredGroups.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredGroups.length;
    },
    filteredModalTeamList() {
      if (!this.teamList) {
        return [];
      }

    // Apply search query filter
    const query = this.searchQueryModal.trim().toLowerCase();
    if (!query) {
      return this.teamList; // Return the entire team list if the query is empty
    }

    return this.teamList.filter((team) => {
      // Check if team.teamName and team.lastName are defined before using toLowerCase()
      const teamName = team.teamName ? team.teamName.toLowerCase() : '';
      const lastName = team.lastName ? team.lastName.toLowerCase() : '';

      return teamName.includes(query) || lastName.includes(query);
    });
  },
  paginatedModalTeamList() {
    const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
    const endIndex = startIndex + this.itemsPerPageModal;
    return this.filteredModalTeamList.slice(startIndex, endIndex);
  },
  totalPagesModal() {
    return Math.ceil(this.filteredModalTeamList.length / this.itemsPerPageModal);
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
    this.fetchTeams();


    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${Vue.$keycloak.token}`
    };
    try {
      this.groupsData = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, { headers });
      this.groups = this.groupsData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    async submitScore(groupId,groupObj) {
      const confirmation = await Swal.fire({
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, Submit Score!'
      });
      if (confirmation.isConfirmed) {
      let requestBody={};
       for (let i = 0; i < groupObj.length; i++) {
          for (let j = i + 1; j < groupObj.length; j++) {

        requestBody = {
          id: groupId,
          gameTeamIdHost: groupObj[i].teamId,
          gameTeamIdOppo: groupObj[j].teamId,
          gameScoreHost: groupObj[i].enteredScore,
          gameScoreOppo: groupObj[j].enteredScore,
        };
       }}

      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
       const response = await axios.put(`${UPDATE_GAME_SCORE_BASE_URL}`,requestBody, { headers });
      }
      catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }}
  },

    async startGame(groupId) {

      const requestBody = {
        id:groupId
      };
      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
       const response = await axios.put(`${UPDATE_GAME_ONGOING_STATUS_BASE_URL}`,requestBody, { headers });
      }

      catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
  },
    async createGame(index) {

      const confirmation = await Swal.fire({
        title: 'Are you sure?',
        text: 'Matchups will be created for all teams!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, create matchups!'
      });

      if (confirmation.isConfirmed) {
      const group = this.filteredGroups[index];
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      const teams = group.gameTeamResponses; // Assuming your group object has a 'teams' property containing an array of teams

    // Loop through each team in the group
    for (let i = 0; i < teams.length; i++) {
      for (let j = i + 1; j < teams.length; j++) {
        // Create a matchup object with two teams
        const requestBody = {
          gameTeamIdHost: teams[i].id,
          gameTeamIdOppo: teams[j].id,
        };

        try {
          // If the group has an ID, update the existing record using a PUT request
          const response = await axios.post(`${CREATE_GAME_BASE_URL}`, requestBody, { headers });

           Swal.fire({
            title: 'Game Set!',
            text: 'Matchups for all teams are created successfully.',
            icon: 'success'
          });
        }
        catch (error) {
        // Handle errors, if any
          console.error('Error saving group:', error);
        }}
        }
      }
    },

    async loadGroup() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      try {

          this.groupsData = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, { headers });
          this.groups = this.groupsData.data.data;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
    },

    async fetchTeams() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
    let response = '';
    try {
        response = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, { headers });
        this.teamList = response.data.data

      }catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    gotoPageModal(page) {
    if (page >= 1 && page <= this.totalPagesModal) {
      this.currentPageModal = page;
    }
  },

    async toggleRow(index) {
    this.objectsWithIdAndTeamArray = []
    if (this.activeRow === index) {
      this.activeRow = null; // Collapse the row if it's already expanded
    } else {
      this.activeRow = index;
      const group = this.filteredGroups[index];
      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
        const response = await axios.get(`${GET_ALL_GAMES_BASE_URL}`, { headers });

        this.groupMembers = response.data.data;

        // Iterate through the data objects
        for (const item of this.groupMembers) {
          // Create an object with "id" and associated teamArray

          const gameTeamIdHost = this.teamList.find(team => team.id === item.gameTeamIdHost);

          const gameScoreOppo = this.teamList.find(team => team.id === item.gameTeamIdOppo);

          const objectWithIdAndHostArray = {
            id: item.id,
            teamId: item.gameTeamIdHost,
            gameStatus : item.gameStatus,
            gameOutcome : item.gameOutcome,
            hostScore: item.gameScoreHost,
            teamName : gameTeamIdHost.teamName
          };

          const objectWithIdAndOppoArray = {
            id: item.id,
            teamId: item.gameTeamIdOppo,
            gameStatus : item.gameStatus,
            gameOutcome : item.gameOutcome,
            oppoScore: item.gameScoreOppo,
            teamName : gameScoreOppo.teamName
          };
          this.objectsWithIdAndTeamArray.push(objectWithIdAndHostArray);
          this.objectsWithIdAndTeamArray.push(objectWithIdAndOppoArray);
        }
        const jsonArray = JSON.stringify(this.objectsWithIdAndTeamArray, null, 2);
        console.log(jsonArray)

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

      toggleTeamSelection(groupId) {
      if (this.selectedTeams.includes(groupId)) {
        this.selectedTeams = this.selectedTeams.filter((selectedUserId) => selectedUserId !== groupId);
      } else {
        this.selectedTeams.push(groupId);
      }
      const jsonPayload = {
        teamIds: this.selectedTeams,
      };
    },
  },
};
</script>

<style scoped>
/* Main Table Styles */
.main-table {

  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border: none;
  border-radius: 15px;
  overflow: hidden;
  font-size: 22px;
}

.main-table th,
.main-table td {
padding: 8px;
  text-align: center;
  font-size: 14px;
  background-color: #f6f6f6;
}
.main-table th {
  background-color: #d7e7f2;
  font-weight: bold;
}


/* Hover effect for clickable rows */
.main-table tbody tr:hover {
  background-color: #f2f2f2;
  cursor: pointer;
}

.team-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.team-table th{
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: #bad4e6;
}

.team-table td {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: white;
}

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
  min-width: 50px;
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

.editing-dropdown {
  font-size: 14px; /* Adjust the font size as needed */
  min-width: 200px; /* Set the minimum width of the dropdown */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px 16px;
  background-color: #fff;
  color: #555;
}

/* Optionally, add styles for the dropdown arrow icon */
.editing-dropdown .dropdown-toggle::after {
  font-family: 'Font Awesome'; /* Assuming you're using Font Awesome for icons */
  content: '\f107'; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}

/* Style the button icon */
.add-button b-icon {
  font-size: 20px;
  margin-right: 5px; /* Add some space between the icon and the button */
}

.add-button {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
  margin-right: 27px;
}
.gold-trophy {
  color: gold; /* Apply gold color to the trophy icon */
}

.longer-td {
  width: 140px; /* Adjust the width as needed */
}

.row-even {
  background-color: #f0f0f0; /* Even row color */
}

.row-odd {
  background-color: #ffffff; /* Odd row color */
}

</style>