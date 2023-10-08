<template>
  <div>
    <!-- Add Member Modal START-->
    <b-modal
      v-model="showAddMemberModal"
      modal-class="modal-lg"
      hide-footer
      id="showUserModal"
    >
      <!-- Search bar for filtering users -->
      <div class="search-container">
        <p class="h3 mb-2">
          <b-icon icon="search" style="color: rgb(65, 127, 202)"></b-icon>
        </p>
        &nbsp;&nbsp;
        <input
          type="text"
          v-model="searchQueryModal"
          placeholder="Search User Name"
          class="search-box"
        />
      </div>
      <p class="pagination-info">
        Showing {{ (currentPageModal - 1) * itemsPerPageModal + 1 }} to
        {{
          Math.min(currentPageModal * itemsPerPageModal, totalRecordsModal)
        }}
        of {{ totalRecordsModal }} records
      </p>
      <div class="pagination">
        <button
          @click="gotoPageModal(currentPageModal - 1)"
          :disabled="currentPageModal === 1"
          class="page-button"
        >
          <i
            class="fas fa-chevron-left icon"
            style="color: rgb(65, 127, 202)"
          ></i>
        </button>
        <span>Page {{ currentPageModal }} of {{ totalPagesModal }}</span>
        <button
          @click="gotoPageModal(currentPageModal + 1)"
          :disabled="currentPageModal === totalPagesModal"
          class="page-button"
        >
          <i
            class="fas fa-chevron-right icon"
            style="color: rgb(65, 127, 202)"
          ></i>
        </button>
      </div>
      <br />
      <!-- List of users to be displayed inside the modal -->
      <table class="modal-table">
        <thead>
          <tr>
            <th></th>
            <th>Select</th>
            <th>User Name</th>
            <th>First Name</th>
            <th>Last Name</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(user, userIndex) in paginatedModalUserList"
            :key="user.id"
          >
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
              <input
                type="checkbox"
                :checked="selectedUsers.includes(user.id)"
                @change="toggleUserSelection(user.id)"
              />
            </td>
            <!--<td>{{ userIndex + 1 }}</td>-->
            <td>{{ user.userName }}</td>
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastName }}</td>
          </tr>
          <tr></tr>
        </tbody>
      </table>
      <div class="text-center">
        <button
          id="addMembersToTeam"
          @click="addMembersToTeam"
          class="add-member-button"
          v-b-tooltip.hover="'Click to save your entry'"
        >
          Save
        </button>
      </div>
    </b-modal>
    <!-- Add Member Modal END-->
    <!-- show IDC history Modal START-->
    <b-modal
      v-model="showHistoryModal"
      id="showHistoryModal"
      modal-class="modal-lg"
      title="Competition Score"
      hide-footer
    >
      <!-- List of users to be displayed inside the modal -->
      <table class="modal-table">
        <thead>
          <tr>
            <th>S/No</th>
            <th>Stage</th>
            <th>Score</th>
            <th>Date / Time</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="presentationList.length === 0">
            <tr>
              <td colspan="10" class="centered-cell">
                <br /><br />No records available.
              </td>
            </tr>
            <tr>
              <br /><br />
            </tr>
          </template>
          <template v-else>
            <tr
              v-for="(presentation, presentationIndex) in presentationList"
              :key="presentation.id"
            >
              <td>{{ presentationIndex + 1 }}</td>
              <td>{{ presentation.stage }}</td>
              <td>{{ presentation.score }} / 100</td>
              <td>{{ formatDateTime(presentation.dateTime) }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </b-modal>
    <!-- show IDC history Modal END-->
    <!-- show GA history Modal START-->
    <b-modal
      v-model="showGAHistoryModal"
      id="showGAHistoryModal"
      modal-class="modal-lg"
      title="Competition Score"
      hide-footer
    >
      <!-- List of users to be displayed inside the modal -->
      <table class="modal-table">
        <thead>
          <tr>
            <th>Qualification Stage</th>
          </tr>
          <tr>
            <th>&nbsp;</th>
          </tr>
          <tr>
            <th class="center-align">Team Name</th>
            <th class="center-align">Match Played</th>
            <th class="center-align">Total credits scored</th>
            <th class="center-align">Points</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="gameTeamList.length === 0">
            <tr>
              <td colspan="10" class="centered-cell">
                <br /><br />No records available.
              </td>
            </tr>
            <tr>
              <br /><br />
            </tr>
          </template>
          <template v-else>
            <tr
              v-for="(gameTeam, gameTeamIndex) in gameTeamList"
              :key="gameTeam.id"
            >
              <td class="center-align">{{ gameTeam.teamName }}</td>
              <td class="center-align">
                {{ gameTeam.qualificationRoundNumMatchesPlayed }}
              </td>
              <td class="center-align">
                {{ gameTeam.qualificationRoundScore }}
              </td>
              <td class="center-align">
                {{ gameTeam.qualificationRoundPoint }}
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </b-modal>
    <!-- show GA history Modal END-->
    <br /><br />
    <div class="search-container">
      <table>
        <tr>
          <td>
            <p class="h3 mb-2">
              <b-icon icon="search" style="color: rgb(65, 127, 202)"></b-icon>
            </p>
          </td>
          &nbsp;
          <td>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search Team Name"
              class="search-box"
            />
          </td>
        </tr>
      </table>
    </div>
    <div v-if="teams && teams.length > 0">
      <p>
        Showing {{ startIndex }} to {{ endIndex }} of {{ totalRecords }} records
      </p>
      <div class="pagination">
        <button
          @click="gotoPage(currentPage - 1)"
          :disabled="currentPage === 1"
          class="page-button"
        >
          <i
            class="fas fa-chevron-left icon"
            style="color: rgb(65, 127, 202)"
          ></i>
          <!-- Font Awesome "Previous" icon -->
        </button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button
          @click="gotoPage(currentPage + 1)"
          :disabled="currentPage === totalPages"
          class="page-button"
        >
          <i
            class="fas fa-chevron-right icon"
            style="color: rgb(65, 127, 202)"
          ></i>
          <!-- Font Awesome "Next" icon -->
        </button>
      </div>
      <table class="main-table">
        <thead>
          <tr>
            <th></th>
            <th>S/No</th>
            <th>Team Name</th>
            <th>Age Group</th>
            <th>Teacher Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody v-for="(team, index) in paginatedTeams" :key="index">
          <tr
            :class="{ 'parent-row': true, 'active-row': activeRow === index }"
            @click="toggleRow(index, team.id)"
          >
            <td>
              <i
                :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'"
                id="expand"
                class="expand-icon"
                @click="toggleRow(index)"
              ></i>
            </td>
            <td>{{ startIndex + index }}</td>
            <td v-if="!team.editing">
              {{ team.teamName }}
            </td>
            <td v-else>
              <input
                type="text"
                v-model="team.editingTeamName"
                class="form-control editing-textbox"
              />
            </td>
            <td v-if="!team.editing">
              {{ ageGroupTextMap[team.ageGroup] }}
            </td>
            <td v-else>
              <b-form-select
                v-model="team.editingAgeGroup"
                :options="filteredAgeGroupChoices"
                class="editing-dropdown"
              >
                <template v-slot:first>
                  <option :value="null" disabled>Select Age Group</option>
                </template>
              </b-form-select>
            </td>
            <td v-if="!team.editing">
              {{ team.teacherName }}
            </td>
            <td v-else>
              <input
                type="text"
                v-model="team.editingteacherName"
                class="form-control editing-textbox"
              />
            </td>
            <td>
              <b-button
                variant="outline-primary"
                @click="fetchUsers(team.id, team.teamName)"
                id="addUserButton"
                v-b-tooltip.hover="'Click to add or remove users from team'"
              >
                <b-icon icon="person-plus"></b-icon>
              </b-button>
              <!-- View Icon -->

              <b-button
                @click="viewGAScore(team)"
                id="viewGAScore"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="'Click to view team score'"
              >
                <b-icon icon="eye"></b-icon>
              </b-button>
              <!-- Edit Icon -->
              <b-button
                id="edit-button"
                @click="editTeam(startIndex + index - 1)"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="'Click to edit team details'"
              >
                <span v-if="!team.editing"
                  ><b-icon icon="pencil"></b-icon
                ></span>
                <span v-else><b-icon icon="save"></b-icon></span>
              </b-button>
              <!-- Delete Icon -->
              <b-button
                class="delete-button"
                id="delete-button"
                variant="outline-primary"
                @click="deleteTeam(startIndex + index - 1)"
                v-b-tooltip.hover="'Click to delete team'"
              >
                <b-icon icon="trash"></b-icon>
              </b-button>
            </td>
          </tr>
          <!-- Child rows -->
          <tr v-if="activeRow === index" class="child-row">
            <td :colspan="10">
              <!-- Use colspan to span all columns in the row -->
              <table class="user-table">
                <thead>
                  <tr>
                    <th></th>
                    <th>No.</th>
                    <th>User Name</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Birthday</th>
                    <th>School Name</th>
                    <th>Experience (Year)</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="teamMembers.length === 0">
                    <td colspan="11" style="color: red">
                      0 member in the team
                    </td>
                  </tr>
                  <tr
                    v-else
                    v-for="(user, userIndex) in teamMembers"
                    :key="userIndex"
                    class="child-row"
                  >
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td>{{ userIndex + 1 }}</td>
                    <td>{{ user.userName }}</td>
                    <td>{{ user.firstName }}</td>
                    <td>{{ user.lastName }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.phone }}</td>
                    <td>{{ user.dateOfBirth }}</td>
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
        <button
          @click="gotoPage(currentPage - 1)"
          :disabled="currentPage === 1"
          class="page-button"
        >
          <i
            class="fas fa-chevron-left icon"
            style="color: rgb(65, 127, 202)"
          ></i>
          <!-- Font Awesome "Previous" icon -->
        </button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button
          @click="gotoPage(currentPage + 1)"
          :disabled="currentPage === totalPages"
          class="page-button"
        >
          <i
            class="fas fa-chevron-right icon"
            style="color: rgb(65, 127, 202)"
          ></i>
          <!-- Font Awesome "Next" icon -->
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
import { ageGroupOptions, competitionChoiceOptions } from "../dropdownOptions";
import {
  ADD_MEMBER_GAME_TEAM_BASE_URL,
  UPDATE_GAME_TEAM_BASE_URL,
  VIEW_GAME_TEAM_BASE_URL,
  GET_ALL_USER_INFO_BASE_URL,
  GET_ALL_GAME_TEAM_BASE_URL,
  GET_ALL_GAME_GROUP_BASE_URL,
} from "@/api";
import Swal from "sweetalert2";
import Vue from "vue";

export default {
  head() {
    return {
      link: [
        {
          rel: "stylesheet",
          href: "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css",
          integrity: "sha512-...",
          crossorigin: "anonymous",
          referrerpolicy: "no-referrer",
        },
      ],
    };
  },

  data() {
    return {
      currentPageModal: 1,
      itemsPerPageModal: 10,
      currentTeamId: "",
      currentTeamName: "",
      selectedUsers: [],
      searchQuery: "",
      searchQueryModal: "",
      teams: [],
      activeRow: null,
      itemsPerPage: 10, // Number of teams per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      showHistoryModal: false,
      showGAHistoryModal: false,
      showAddMemberModal: false,
      userList: [],
      presentationList: [],
      teamMembers: [],
      gameTeamList: [],
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
      if (this.searchQuery.trim() === "") {
        return this.teams;
      }

      // Otherwise, filter teams based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.teams.filter((team) =>
        team.teamName.toLowerCase().includes(query)
      );
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
      const startIndex = (this.currentPage - 1) * this.itemsPerPage + 1;
      const endIndex = Math.min(
        startIndex + this.itemsPerPage - 1,
        this.totalRecords
      );
      return this.filteredTeams.slice(startIndex - 1, endIndex); // Adjust indices to 0-based
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
        const firstName = user.firstName ? user.firstName.toLowerCase() : "";
        const lastName = user.lastName ? user.lastName.toLowerCase() : "";

        return firstName.includes(query) || lastName.includes(query);
      });
    },
    paginatedModalUserList() {
      const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
      const endIndex = startIndex + this.itemsPerPageModal;
      return this.filteredModalUserList.slice(startIndex, endIndex);
    },
    totalPagesModal() {
      return Math.ceil(
        this.filteredModalUserList.length / this.itemsPerPageModal
      );
    },
    ageGroupTextMap() {
      // Define a mapping of age group values to their corresponding text
      const ageGroupMap = {
        OC: "Open Category: 8-15 years old",
        JC: "Junior Category: 8-12 years old",
        // Add more entries as needed for other age groups
      };
      return ageGroupMap;
    },
  },
  async mounted() {
    let token = "";

    if (
      Vue.$keycloak &&
      Vue.$keycloak.token &&
      Vue.$keycloak.token.length > 0
    ) {
      token = Vue.$keycloak.token;
    } else {
      token = "mockedToken"; //for unit test
    }
    const headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    };
    try {
      this.teamsData = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, {
        headers,
      });
      this.teams = this.teamsData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    formatDateTime(dateTime) {
      const date = new Date(dateTime);
      const day = String(date.getDate()).padStart(2, "0");
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const year = date.getFullYear();
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      const seconds = String(date.getSeconds()).padStart(2, "0");

      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    },
    async loadTeam() {
      let token = "";

      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      try {
        this.teamsData = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, {
          headers,
        });
        this.teams = this.teamsData.data.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },

    async fetchUsers(teamId, teamName) {
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`${GET_ALL_USER_INFO_BASE_URL}`, {
          headers,
        });
        this.currentTeamId = teamId;
        const allUsers = response.data.data;

        this.userList = allUsers.filter((record) =>
          record.hasOwnProperty("gameTeam")
        );
        console.log("this.userList", this.userList);
        this.currentTeamName = teamName;
        this.showAddMemberModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },

    async viewGAScore(teamObj) {
      let groupID = "";
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      console.log("teamObj", teamObj.id, teamObj.teamName);
      try {
        const response = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, {
          headers,
        });
        const teamObject = response.data.data;
        console.log("teamObject BEFORE", teamObject);

        for (const group of teamObject) {
          const teamId = group.id;

          for (const team of group.gameTeamResponses) {
            const teamId = team.id;
            if (teamObj.id === team.id) {
              groupID = group.id;
            }
          }
        }

        let groupObj = teamObject.filter((team) => team.id === groupID);
        this.gameTeamList = groupObj[0].gameTeamResponses;
        console.log("teamObject AFTER", this.gameTeamList);

        this.showGAHistoryModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    gotoPageModal(page) {
      if (page >= 1 && page <= this.totalPagesModal) {
        this.currentPageModal = page;
      }
    },

    async toggleRow(index, teamID) {
      if (this.activeRow === index) {
        this.activeRow = null; // Collapse the row if it's already expanded
      } else {
        this.activeRow = index;
        const team = this.filteredTeams[index];
        let token = "";
        if (
          Vue.$keycloak &&
          Vue.$keycloak.token &&
          Vue.$keycloak.token.length > 0
        ) {
          token = Vue.$keycloak.token;
        } else {
          token = "mockedToken"; //for unit test
        }
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        try {
          // Make the API call here using the team ID as the request body

          const response = await axios.get(`${GET_ALL_USER_INFO_BASE_URL}`, {
            headers,
          });
          const allUsers = response.data.data;

          this.teamMembers = allUsers.filter((record) => {
            return (
              record.hasOwnProperty("gameTeam") && record.gameTeam === teamID
            );
          });

          // Optional: Perform any additional actions, such as updating the UI.
        } catch (error) {
          // Handle errors, if any
          console.error("Error calling API:", error);
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
      const day = date.getDate().toString().padStart(2, "0");
      const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Months are zero-based
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
        team.isQualifiedPromo = team.editingStatus;

        const requestBody = {
          id: team.id,
          teamName: team.teamName,
          ageGroup: team.ageGroup,
        };
        let token = "";
        if (
          Vue.$keycloak &&
          Vue.$keycloak.token &&
          Vue.$keycloak.token.length > 0
        ) {
          token = Vue.$keycloak.token;
        } else {
          token = "mockedToken"; //for unit test
        }
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };

        try {
          const response = await axios.put(
            `${UPDATE_GAME_TEAM_BASE_URL}`,
            requestBody,
            { headers }
          );
        } catch (error) {
          // Handle errors, if any
          console.error("Error adding members to team:", error);
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
    async deleteTeam(index) {
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }

      const team = this.filteredTeams[index];
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      const confirmation = await Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Yes, delete it!",
      });

      if (confirmation.isConfirmed) {
        const requestBody = {
          id: team.id,
        };
        const requestBody2 = {
          id: team.id,
          teamName: team.teamName,
          userIds: [],
        };

        try {
          if (this.selectedCompetition === "Game Arena") {
            const response2 = await axios.put(``, requestBody2, { headers });
            const response = await axios.delete(``, {
              data: requestBody,
              headers: headers,
            });
          }
          // Show a success message
          Swal.fire({
            title: "Deleted!",
            text: "The Team has been deleted.",
            icon: "success",
          });

          this.loadTeam();
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }
    },

    toggleUserSelection(userId) {
      if (this.selectedUsers.includes(userId)) {
        this.selectedUsers = this.selectedUsers.filter(
          (selectedUserId) => selectedUserId !== userId
        );
      } else {
        this.selectedUsers.push(userId);
      }
      const jsonPayload = {
        userIds: this.selectedUsers,
      };
    },

    async addMembersToTeam() {
      // Make sure currentTeamId and selectedUsers are defined

      if (!this.currentTeamId || this.selectedUsers.length === 0) {
        console.error("Team ID or selected users not available.");
        return;
      }

      // Prepare the payload to be sent in the request body
      const requestBody = {
        id: this.currentTeamId,
        userIds: this.selectedUsers,
      };
      let token = "";
      if (
        Vue.$keycloak &&
        Vue.$keycloak.token &&
        Vue.$keycloak.token.length > 0
      ) {
        token = Vue.$keycloak.token;
      } else {
        token = "mockedToken"; //for unit test
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        // Make the HTTP POST request to the API endpoint

        const response = await axios.put(
          `${ADD_MEMBER_GAME_TEAM_BASE_URL}`,
          requestBody,
          { headers }
        );

        // Close the modal after the request is successful
        this.showAddMemberModal = false;

        // Optional: Perform any additional actions, such as updating the UI.
      } catch (error) {
        // Handle errors, if any
        console.error("Error adding members to team:", error);
      }
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

/* Nested Table Styles */
.user-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.user-table th {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: #bad4e6;
}
.user-table td {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: white;
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
  background-color: rgb(218, 234, 253); /* Light blue for active parent row */
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
  margin-right: 65px;
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
.custom-modal .modal-dialog {
  max-width: 800px; /* Set the max width of the modal */
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
  background-color: #5dade2;
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
  font-family: "Font Awesome"; /* Assuming you're using Font Awesome for icons */
  content: "\f107"; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}

.pagination-info {
  text-align: center; /* Center-align the text */
  margin: 20px 0; /* Add some margin for spacing */
}

.centered-cell {
  text-align: center;
}
.center-align {
  text-align: center;
}
</style>
