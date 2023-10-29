<template>
  <b-tabs>
    <b-tab
      v-for="option in filteredCompetitionChoices"
      :key="option.id"
      :title="option.text"
      @click="
        selectedCompetition = option.text;
        loadGroup();
      "
    >
      <div>
        <!-- Add Member Modal -->
        <b-modal
          v-model="showAddTeamModal"
          modal-class="custom-modal"
          hide-footer
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
              placeholder="Search team Name"
              class="search-box"
            />
          </div>
          <p>
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
                <th>Select</th>
                <th>Team Name</th>
                <!--<th>Age Group</th>-->
                <!-- <th>Qualification Status</th>-->
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(team, userIndex) in paginatedModalTeamList"
                :key="team.id"
              >
                <td>
                  <input
                    type="checkbox"
                    :checked="selectedTeams.includes(team.id)"
                    @change="toggleTeamSelection(team.id)"
                  />
                </td>
                <!--<td>{{ userIndex + 1 }}</td>-->
                <td>{{ team.teamName }}</td>
                <!--<td>{{ team.ageGroup }}</td>-->
                <!--<td>Final 1st Stage</td>-->
              </tr>
              <tr></tr>
            </tbody>
          </table>
          <div class="text-center">
            <button
              @click="addTeamsToGroup"
              class="add-member-button"
              v-b-tooltip.hover="'Click to save entry'"
            >
              Save
            </button>
          </div>
        </b-modal>
        <br /><br />
        <!--END OF MODAL-->
        <div class="search-container">
          <table>
            <tr>
              <td>
                <p class="h3 mb-2">
                  <b-icon
                    icon="search"
                    style="color: rgb(65, 127, 202)"
                  ></b-icon>
                </p>
              </td>
              &nbsp;
              <td>
                <input
                  type="text"
                  v-model="searchQuery"
                  placeholder="Search group Name"
                  class="search-box"
                />
              </td>
            </tr>
          </table>
        </div>
        <div class="add-button">
          <b-button variant="outline-primary" size="lg" @click="addNewGroup"
            ><b-icon
              icon="file-earmark-plus"
              v-b-tooltip.hover="'Click to create new group'"
            ></b-icon> </b-button
          ><br />
        </div>
        <div v-if="groups && groups.length > 0">
          <p>
            Showing {{ startIndex }} to {{ endIndex }} of
            {{ totalRecords }} records
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
                <th>Group Name</th>
                <th v-if="selectedCompetition === 'Grand Arena'">
                  Match Date/time
                </th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody v-for="(group, index) in paginatedTeams" :key="index">
              <tr
                :class="{
                  'parent-row': true,
                  'active-row': activeRow === index,
                }"
                @click="group.editing ? null : toggleRow(index)"
              >
                <td>
                  <i
                    :class="
                      activeRow === index ? 'fas fa-minus' : 'fas fa-plus'
                    "
                    class="expand-icon"
                  ></i>
                </td>
                <td>{{ startIndex + index }}</td>
                <td v-if="!group.editing">
                  {{ group.groupName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="group.editingGroupName"
                    class="form-control editing-textbox"
                  />
                </td>
                <td>
                  <b-button
                    variant="outline-primary"
                    @click="fetchTeams(group.id, group.groupName)"
                    v-b-tooltip.hover="'Click to add / remove teams from group'"
                  >
                    <b-icon icon="person-plus"></b-icon>
                  </b-button>
                  <!-- Edit Icon -->
                  <b-button
                    @click="editGroup(startIndex + index - 1)"
                    variant="outline-primary"
                    class="delete-button"
                    v-b-tooltip.hover="'Click to edit group details'"
                  >
                    <span v-if="!group.editing"
                      ><b-icon icon="pencil"></b-icon
                    ></span>
                    <span v-else><b-icon icon="save"></b-icon></span>
                  </b-button>
                  <!-- Delete Icon -->
                  <b-button
                    class="delete-button"
                    variant="outline-primary"
                    @click="deleteGroup(startIndex + index - 1)"
                    v-b-tooltip.hover="'Click to delete group'"
                  >
                    <b-icon icon="trash"></b-icon>
                  </b-button>
                </td>
              </tr>
              <!-- Child rows -->
              <tr v-if="activeRow === index" class="child-row">
                <td :colspan="10">
                  <!-- Use colspan to span all columns in the row -->
                  <table class="team-table">
                    <thead>
                      <tr>
                        <th></th>
                        <th>No.</th>
                        <th>Team Name</th>
                        <th>Age Group</th>
                        <!--<th>Qualification Status</th>-->
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="groupMembers.length === 0">
                        <td colspan="11" style="color: red">
                          0 team in the group
                        </td>
                      </tr>
                      <tr
                        v-else
                        v-for="(team, userIndex) in groupMembers"
                        :key="userIndex"
                        class="child-row"
                      >
                        <td>
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </td>
                        <td>{{ userIndex + 1 }}</td>
                        <td>{{ team.teamName }}</td>
                        <td>{{ ageGroupTextMap[team.ageGroup] }}</td>
                        <!--<td> Final 1st Stage </td>-->
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
    </b-tab>
  </b-tabs>
</template>

<script>
import axios from "axios";
import { ageGroupOptions, competitionChoiceOptions } from "../dropdownOptions";
import { api } from "../api";
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
      selectedCompetition: "Innovation Design Challenge",
      currentPageModal: 1,
      itemsPerPageModal: 10,
      currentGroupId: "",
      currentGroupName: "",
      selectedTeams: [],
      searchQuery: "",
      searchQueryModal: "",
      groups: [],
      gameTeamResponses: [],
      activeRow: null,
      itemsPerPage: 10, // Number of groups per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      showAddTeamModal: false,
      teamList: [],
      groupMembers: [],
      newChildRow: {
        teamName: "",
        ageGroup: "",
      },
    };
  },
  computed: {
    totalRecordsModal() {
      console.log("length", this.filteredModalTeamList.length);
      return this.filteredModalTeamList.length;
    },
    filteredGroups() {
      // If the groups data is not available yet, return an empty array
      if (!this.groups || this.groups.length === 0) {
        return [];
      }

      // If the search query is empty, show all groups
      if (this.searchQuery.trim() === "") {
        return this.groups;
      }

      // Otherwise, filter groups based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.groups.filter((group) =>
        group.groupName.toLowerCase().includes(query)
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
        const teamName = team.teamName ? team.teamName.toLowerCase() : "";
        const lastName = team.lastName ? team.lastName.toLowerCase() : "";

        return teamName.includes(query) || lastName.includes(query);
      });
    },
    paginatedModalTeamList() {
      const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
      const endIndex = startIndex + this.itemsPerPageModal;
      return this.filteredModalTeamList.slice(startIndex, endIndex);
    },
    totalPagesModal() {
      console.log(this.filteredModalTeamList.length / this.itemsPerPageModal);
      return Math.ceil(
        this.filteredModalTeamList.length / this.itemsPerPageModal
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
    const headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${Vue.$keycloak.token}`,
    };
    try {
      this.groupsData = await axios.get(`${api.GET_ALL_IDC_GROUP_BASE_URL}`, {
        headers,
      });
      this.groups = this.groupsData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    async editGroup(index) {
      const group = this.filteredGroups[index];
      if (group.editing) {
        // Save the changes
        group.groupName = group.editingGroupName;

        group.editing = false;

        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Vue.$keycloak.token}`,
        };
        try {
          let url = "";
          let url2 = "";
          if (this.selectedCompetition === "Grand Arena") {
            url = api.UPDATE_GAME_GROUP_BASE_URL;
            url2 = api.CREATE_GAME_GROUP_BASE_URL;
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            url = api.UPDATE_IDC_GROUP_BASE_URL;
            url2 = api.CREATE_IDC_GROUP_BASE_URL;
          }
          if (group.id) {
            const requestBody = {
              groupName: group.groupName,
            };
            // If the group has an ID, update the existing record using a PUT request
            const response = await axios.post(`${url}`, requestBody, {
              headers,
            });
          } else {
            const requestBody = {
              groupName: group.groupName,
            };
            // If the group doesn't have an ID, create a new record using a POST request
            const response = await axios.post(`${url2}`, requestBody, {
              headers,
            });

            // Add the newly created group to the beginning of the groups array
            this.groups.unshift(response.data.data);
            url = "";
          }
          this.loadGroup();
          // Optional: Perform any additional actions, such as updating the UI.
        } catch (error) {
          // Handle errors, if any
          console.error("Error saving group:", error);
        }
      } else {
        // Enter editing mode
        group.editingGroupName = group.groupName;
        group.editing = true;
      }
    },
    async addNewGroup() {
      // Create a new group object and add it to the beginning of the groups array
      const newGroup = {
        groupName: "",
        editing: true, // Set editing to true to enable editing mode for the new group
        editingGroupName: "",
      };

      this.groups.unshift(newGroup);

      // Update the currentPage to 1 to ensure the newly added group appears on the first page
      this.currentPage = 1;
    },
    async loadGroup() {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
      };
      try {
        if (this.selectedCompetition === "Grand Arena") {
          this.groupsData = await axios.get(
            `${api.GET_ALL_GAME_GROUP_BASE_URL}`,
            {
              headers,
            }
          );
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          this.groupsData = await axios.get(
            `${api.GET_ALL_IDC_GROUP_BASE_URL}`,
            {
              headers,
            }
          );
        }
        this.groups = this.groupsData.data.data;
        console.log("load group", this.groups);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },

    async fetchTeams(groupId, groupName) {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
      };
      let response = "";
      try {
        if (this.selectedCompetition === "Grand Arena") {
          response = await axios.get(`${api.GET_ALL_GAME_TEAM_BASE_URL}`, {
            headers,
          });
          this.currentGroupName = "";
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          response = await axios.get(`${api.GET_ALL_IDC_TEAM_BASE_URL}`, {
            headers,
          });
          this.currentGroupName = groupName;
        }
        this.teamList = response.data.data;
        this.currentGroupId = groupId;
        //this.teamList = response.data.data.filter((team) => team.isQualifiedFinal );

        this.showAddTeamModal = true; // Show the modal after fetching the users
      } catch (error) {
        console.error("Error fetching data:", error);
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
        const group = this.filteredGroups[index];
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Vue.$keycloak.token}`,
        };
        try {
          // Make the API call here using the group ID as the request body
          const requestBody = {
            id: group.id,
          };

          // Make the HTTP PUT request to the API endpoint
          const requestBodyJson = JSON.stringify(requestBody);

          let response;

          if (this.selectedCompetition === "Grand Arena") {
            let groupFilteredObj = this.groups.filter(
              (team) => team.id === group.id
            );

            this.groupMembers = groupFilteredObj[0].gameTeamResponses;
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            response = await axios.post(
              `${api.VIEW_IDC_GROUP_BASE_URL}`,
              requestBodyJson,
              { headers }
            );
            this.groupMembers = response.data.data.idcTeamResponses;

            this.loadGroup();
          }

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

    async deleteGroup(index) {
      const group = this.filteredGroups[index];
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
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
          id: group.id,
        };
        const requestBody2 = {
          id: group.id,
          teamIds: [],
        };

        try {
          if (this.selectedCompetition === "Grand Arena") {
            const response2 = await axios.put(
              `${api.ADD_TEAM_IDC_GROUP_BASE_URL}`,
              requestBody2,
              { headers }
            );
            const response = await axios.delete(
              `${api.DELETE_IDC_GROUP_BASE_URL}`,
              {
                data: requestBody,
                headers: headers,
              }
            );
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            const response2 = await axios.put(
              `${api.ADD_TEAM_IDC_GROUP_BASE_URL}`,
              requestBody2,
              { headers }
            );
            const response = await axios.delete(
              `${api.DELETE_IDC_GROUP_BASE_URL}`,
              {
                data: requestBody,
                headers: headers,
              }
            );
          }

          // Show a success message
          Swal.fire({
            title: "Deleted!",
            text: "The user has been deleted.",
            icon: "success",
          });

          this.loadGroup();
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }
    },
    // Method to add a new child row
    addChildRow(teamIndex) {
      const group = this.filteredGroups[teamIndex];
      group.idcTeamResponses.push({ ...this.newChildRow });
      // Set the initial country and state values for the new child row
      const newUserIndex = group.idcTeamResponses.length - 1;
      const newUser = group.idcTeamResponses[newUserIndex];
      newUser.country = null;
      newUser.statesOptions = [];
    },
    toggleTeamSelection(groupId) {
      if (this.selectedTeams.includes(groupId)) {
        this.selectedTeams = this.selectedTeams.filter(
          (selectedUserId) => selectedUserId !== groupId
        );
      } else {
        this.selectedTeams.push(groupId);
      }
      const jsonPayload = {
        teamIds: this.selectedTeams,
      };
    },

    async addTeamsToGroup() {
      if (!this.currentGroupId || this.selectedTeams.length === 0) {
        console.error("group ID or selected users not available.");
        return;
      }
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
      };
      try {
        if (this.selectedCompetition === "Grand Arena") {
          const requestBody = {
            id: this.currentGroupId,
            gameTeamIds: this.selectedTeams,
          };
          const response = await axios.put(
            `${api.ADD_TEAM_GAME_GROUP_BASE_URL}`,
            requestBody,
            { headers }
          );
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          const requestBody = {
            id: this.currentGroupId,
            teamIds: this.selectedTeams,
          };
          const response = await axios.put(
            `${api.ADD_TEAM_IDC_GROUP_BASE_URL}`,
            requestBody,
            { headers }
          );
          console.log("response", requestBody);
        }

        // Close the modal after the request is successful
        this.showAddTeamModal = false;
        this.selectedTeams = [];
        this.loadGroup;
      } catch (error) {
        console.error("Error adding members to group:", error);
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

.team-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.team-table th {
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
  margin-bottom: 20px;
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
</style>
