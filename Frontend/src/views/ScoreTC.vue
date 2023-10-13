<template>
  <div>
    <br />
    <div v-if="techComps && techComps.length > 0">
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

      <table class="instruction-table">
        <tr>
          <u>Instructions :</u>
        </tr>
        <tr>
          1. Click on
          <b-icon icon="person-plus"></b-icon>
          to create matchup between any 2 teams.
        </tr>
        <tr>
          2. Click on
          <b-icon icon="play-circle"></b-icon>
          to start the match.
        </tr>
        <tr>
          <td>
            3. Click on either the <span style="color: red">Red</span> or
            <span style="color: blue">Blue</span> team name to declare the
            winner.
          </td>
        </tr>
      </table>
      <div class="d-flex justify-content-end">
        <b-button
          id="addNewTechComp"
          variant="outline-primary"
          @click="addNewTechComp"
        >
          <b-icon
            icon="person-plus"
            v-b-tooltip.hover="'Click to create new techComp'"
          ></b-icon>
        </b-button>
      </div>
      <br />
      <table class="main-table">
        <thead>
          <tr>
            <th></th>
            <th>S/No</th>
            <th>Game Name</th>
            <th>Team Name</th>
            <th></th>
            <th>Team Name</th>
            <th>Game Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody v-for="(techComp, index) in paginatedTechComps" :key="index">
          <tr>
            <td></td>
            <td>{{ startIndex + index }}</td>
            <td v-if="!techComp.editing" class="normal-td">
              {{ techComp.gameName }}
            </td>
            <td v-else>
              <div class="form-group select">
                <select
                  v-model="selectedTechComp"
                  id="tcGameOptions"
                  class="editing-dropdown"
                >
                  <option value="" disabled selected>Select Game</option>
                  <option
                    v-for="option in tcGameOptions"
                    :value="option.value"
                    :key="option.id"
                  >
                    {{ option.value }}
                  </option>
                </select>
              </div>
            </td>
            <td
              v-if="!techComp.editing"
              :class="
                techComp.gameStatus === 'done' ? 'host-td' : 'big-button-td'
              "
            >
              <div
                :class="
                  techComp.gameStatus === 'done'
                    ? 'host-container'
                    : techComp.gameStatus === 'pending'
                    ? 'button-container-disabled'
                    : 'button-container-host'
                "
              >
                <template v-if="techComp.gameStatus !== 'done'">
                  <!-- Use the :disabled attribute to disable the button -->
                  <!-- Apply the .disabled-button class when disabled -->
                  <button
                    class="big-button-host"
                    @click="qualifyWinner(techComp.id, 'win')"
                    :disabled="techComp.gameStatus === 'pending'"
                    :class="{
                      'disabled-button': techComp.gameStatus === 'pending',
                    }"
                  >
                    {{ techComp.gameTeamIdHostName }}
                  </button>
                </template>
                <template v-else>
                  <span v-if="techComp.gameOutcome === 'win'">
                    <i class="fas fa-star gold-star"></i><br />
                  </span>
                  <span style="color: rgba(0, 0, 255, 0.744)">{{
                    techComp.gameTeamIdHostName
                  }}</span>
                </template>
              </div>
            </td>

            <td v-else>
              <div class="form-group select">
                <v-select
                  v-model="selectedHostTeam"
                  :options="teamList"
                  label="teamName"
                  placeholder="Select Team"
                  id="hostTeam"
                  class="editing-dropdown"
                ></v-select>
              </div>
            </td>
            <td><img src="../assets/Versus_icon.png" alt="Versus" /></td>
            <td
              v-if="!techComp.editing"
              :class="
                techComp.gameStatus === 'done' ? 'host-td' : 'big-button-td'
              "
            >
              <div
                :class="
                  techComp.gameStatus === 'done'
                    ? 'host-container'
                    : techComp.gameStatus === 'pending'
                    ? 'button-container-disabled'
                    : 'button-container-oppo'
                "
              >
                <template v-if="techComp.gameStatus !== 'done'">
                  <!-- Use the :disabled attribute to disable the button -->
                  <!-- Apply the .disabled-button class when disabled -->
                  <button
                    class="big-button-oppo"
                    @click="qualifyWinner(techComp.id, 'lose')"
                    :disabled="techComp.gameStatus === 'pending'"
                    :class="{
                      'disabled-button': techComp.gameStatus === 'pending',
                    }"
                  >
                    {{ techComp.gameTeamIdOppoName }}
                  </button>
                </template>
                <template v-else>
                  <span v-if="techComp.gameOutcome === 'lose'">
                    <i class="fas fa-star gold-star"></i><br />
                  </span>
                  <span style="color: red">{{
                    techComp.gameTeamIdOppoName
                  }}</span>
                </template>
              </div>
            </td>

            <td v-else>
              <div class="form-group select">
                <v-select
                  v-model="selectedOppoTeam"
                  :options="teamList"
                  label="teamName"
                  placeholder="Select Team"
                  id="hostTeam"
                  class="editing-dropdown"
                ></v-select>
              </div>
            </td>
            <td>
              {{ gameStatusTextMap[techComp.gameStatus] }}
            </td>
            <td v-if="techComp.gameStatus === ''">
              <!-- Edit Icon -->
              <b-button
                id="edit-button"
                @click="editingUser(startIndex + index - 1)"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="'Click to edit techComp particulars'"
              >
                <span v-if="!techComp.editing"
                  ><b-icon icon="pencil"></b-icon
                ></span>
                <span v-else><b-icon icon="save"></b-icon></span>
              </b-button>
              <!-- Delete Icon -->
              <b-button
                id="deleteUser"
                class="delete-button"
                variant="outline-primary"
                @click="deleteUser(startIndex + index - 1)"
                v-b-tooltip.hover="'Click to delete techComp'"
              >
                <b-icon icon="trash"></b-icon>
              </b-button>
            </td>
            <td v-if="gameStatusTextMap[techComp.gameStatus] === 'Not Started'">
              <b-button
                @click="startGame(techComp.id)"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="'Click to start game for this matchup'"
              >
                <b-icon icon="play-circle"></b-icon>&nbsp;Start
              </b-button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <br />
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
import { api } from "../api";
import { tcGameOptions } from "../dropdownOptions";
import Swal from "sweetalert2";
import Vue from "vue";
import vSelect from "vue-select";

export default {
  components: {
    "v-select": vSelect,
  },
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
      selectedTechComp: "",
      selectedHostTeam: "",
      selectedOppoTeam: "",
      searchQuery: "",
      techComps: [],
      activeRow: null,
      itemsPerPage: 10, // Number of techComps per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      teamList: [],
    };
  },
  computed: {
    tcGameOptions() {
      return tcGameOptions;
    },

    gameStatusTextMap() {
      // Define a mapping of age group values to their corresponding text
      const gameStatusMap = {
        pending: "Not Started",
        ongoing: "In-Progress",
        done: "Completed",
        // Add more entries as needed for other age groups
      };
      return gameStatusMap;
    },

    filteredTechComps() {
      // If the techComps data is not available yet, return an empty array
      if (!this.techComps || this.techComps.length === 0) {
        return [];
      }

      // If the search query is empty, show all techComps
      if (this.searchQuery.trim() === "") {
        // Sort the techComps by "Stage Name" in ascending order (A to Z)
        return this.techComps;
      }
      // Otherwise, filter techComps based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.techComps.filter((techComp) =>
        techComp.firstName.toLowerCase().includes(query)
      );
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    totalPages() {
      return Math.ceil(this.filteredTechComps.length / this.itemsPerPage);
    },
    paginatedTechComps() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredTechComps.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredTechComps.length;
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
      const techCompsData = await axios.get(`${api.VIEW_ALL_TC_BASE_URL}`, {
        headers,
      });
      const techComp = techCompsData.data.data;

      const response2 = await axios.get(`${api.GET_ALL_GAME_TEAM_BASE_URL}`, {
        headers,
      });
      this.teamList = response2.data.data;

      //merge 2 arrays
      for (let i = 0; i < this.teamList.length; i++) {
        for (let j = 0; j < techComp.length; j++) {
          if (this.teamList[i].id === techComp[j].gameTeamIdHost) {
            techComp[j].gameTeamIdHostName = this.teamList[i].teamName; // Add gameTeamIdHostName property to techComp
          } else if (this.teamList[i].id === techComp[j].gameTeamIdOppo) {
            techComp[j].gameTeamIdOppoName = this.teamList[i].teamName; // Add gameTeamIdOppoName property to techComp
          }
        }
      }
      this.techComps = techComp;
      console.log(this.techComps);
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching techComps:", error);
    }
  },
  methods: {
    async startGame(gameId) {
      console.log("startGame Called");

      const requestBody = {
        id: gameId,
      };
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Vue.$keycloak.token}`,
      };
      try {
        const response = await axios.put(
          `${api.UPDATE_TC_STATUS_BASE_URL}`,
          requestBody,
          { headers }
        );

        this.loadTechComp();
      } catch (error) {
        // Handle errors, if any
        console.error("Error calling API:", error);
      }
    },
    async qualifyWinner(gameID, result) {
      const confirmation = await Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Confirm Winner!",
      });
      if (confirmation.isConfirmed) {
        console.log("gameID - ", gameID, " winnerID - ", result);

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
          const requestBody = {
            id: gameID,
            gameOutcome: result,
          };
          const response = await axios.put(
            `${api.UPDATE_TC_OUTCOME_BASE_URL}`,
            requestBody,
            { headers }
          );
          this.loadTechComp();
        } catch (error) {
          // Handle any errors that might occur during the request
          console.error("Error fetching techComps:", error);
        }
      }
    },

    async loadTechComp() {
      console.log("Tech Comp Loaded");
      this.techComps = [];
      this.teamList = [];

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
        const techCompsData = await axios.get(`${api.VIEW_ALL_TC_BASE_URL}`, {
          headers,
        });
        const techComp = techCompsData.data.data;

        const response2 = await axios.get(`${api.GET_ALL_GAME_TEAM_BASE_URL}`, {
          headers,
        });
        this.teamList = response2.data.data;

        //merge 2 arrays
        for (let i = 0; i < this.teamList.length; i++) {
          for (let j = 0; j < techComp.length; j++) {
            if (this.teamList[i].id === techComp[j].gameTeamIdHost) {
              techComp[j].gameTeamIdHostName = this.teamList[i].teamName; // Add gameTeamIdHostName property to techComp
            } else if (this.teamList[i].id === techComp[j].gameTeamIdOppo) {
              techComp[j].gameTeamIdOppoName = this.teamList[i].teamName; // Add gameTeamIdOppoName property to techComp
            }
          }
        }
        this.techComps = techComp;
        console.log(this.techComps);
      } catch (error) {
        // Handle any errors that might occur during the request
        console.error("Error fetching techComps:", error);
      }
    },
    async addNewTechComp() {
      // Create a new techComp object and add it to the beginning of the techComps array
      const newTechComp = {
        editing: true, // Set editing to true to enable editing mode for the new techComp
        selectedTechComp: "",
        selectedHostTeam: "",
        selectedOppoTeam: "",
        gameStatus: "",
      };

      this.techComps.unshift(newTechComp);

      // Update the currentPage to 1 to ensure the newly added techComp appears on the first page
      this.currentPage = 1;
    },

    gotoPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },

    // Method to toggle editing mode for a techComp
    async editingUser(index) {
      const techComp = this.filteredTechComps[index];

      if (techComp.editing) {
        // Save the changes
        techComp.userName = techComp.editingUserName;
        (techComp.gameStatus = ""), (techComp.editing = false);
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
          const url = api.UPDATE_USER_INFO_BASE_URL;
          const url2 = api.CREATE_TC_BASE_URL;

          // If the techComp has an ID, update the existing record using a PUT request
          if (techComp.id) {
            const requestBody = {
              id: techComp.id,
              userName: techComp.userName,
            };
            const response = await axios.put(`${url}`, requestBody, {
              headers,
            });
          }
          // If the techComp doesn't have an ID, create a new record using a POST request
          else {
            if (this.selectedHostTeam.id === this.selectedOppoTeam.id) {
              Swal.fire({
                icon: "error",
                title: "Error",
                text: "Please select 2 different teams.",
              });
            } else {
              const requestBody = {
                gameTeamIdHost: this.selectedHostTeam.id,
                gameTeamIdOppo: this.selectedOppoTeam.id,
                gameName: this.selectedTechComp,
                gameVenue: "some venue",
              };
              console.log(requestBody);
              const response = await axios.post(`${url2}`, requestBody, {
                headers,
              });

              // Add the newly created techComp to the beginning of the techComps array
              this.loadTechComp();
              url = "";
            }
          }

          // Optional: Perform any additional actions, such as updating the UI.
        } catch (error) {
          // Handle errors, if any
          console.error("Error saving techComp:", error);
        }
      } else {
        // Enter editing mode
        techComp.editingUserName = techComp.userName;
        techComp.editingFirstName = techComp.firstName;
        techComp.editingLastName = techComp.lastName;
        techComp.editingPhoneNumber = techComp.phoneNumber;
        techComp.editingEmail = techComp.email;
        techComp.editing = true;
      }
    },
    // Method to delete a techComp
    async deleteUser(index) {
      const techComp = this.filteredTechComps[index];
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
          id: techComp.id,
        };

        try {
          const response = await axios.delete(`${api.DELETE_USER_INFO_BASE_URL}`, {
            data: requestBody,
            headers: headers,
          });

          // Show a success message
          Swal.fire({
            title: "Deleted!",
            text: "The techComp has been deleted.",
            icon: "success",
          });

          this.loadTechComp();
        } catch (error) {
          console.error("Error fetching data:", error);
        }
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
  font-size: 18px;
}

.main-table th,
.main-table td {
  text-align: center;
  height: 30px;
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* Add some top margin for spacing */
}

.page-button {
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin: 0 5px;
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
  border: none;
  padding: 0; /* Remove padding to make the icons fit better */
  font-size: 16px;
}

.delete-button {
  margin-left: 5px;
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

.add-button {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
  margin-right: 27px;
}

/* Style the button icon */
.add-button b-icon {
  font-size: 20px;
  margin-right: 5px; /* Add some space between the icon and the button */
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
.editing-dropdown {
  font-family: "Font Awesome"; /* Assuming you're using Font Awesome for icons */
  content: "\f107"; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
}

.host-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 25x;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
  height: 30px;
}

.normal-td {
  font-size: 18x;
}
.oppo-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 25px;
  color: rgba(255, 0, 0, 0.807);
  font-weight: bold;
  font-family: "Tourney", sans-serif;
  height: 30px;
}

.big-button-td {
  padding: 15px; /* Add margin around the TD element */
}

.button-container-host {
  background-image: linear-gradient(
    to bottom,
    #9ec0e6,
    #003271
  ); /* Gradient background */
  padding: 10px; /* Adjust padding to control the container size */
  border-radius: 8px; /* Rounded corners for the container */
  display: inline-block; /* Make the container inline-block to shrink to content */
  color: #fff; /* Text color on the gradient background */
}

.big-button-host {
  font-size: 18px; /* Adjust font size to make text bigger */
  background-color: transparent; /* Make button background transparent */
  color: #ffffff; /* Change text color */
  border: none;
  cursor: pointer;
  font-family: "Tourney", sans-serif;
}

.big-button-host:hover {
  background-color: #0056b3; /* Change the background color on hover */
}

.button-container-oppo {
  background-image: linear-gradient(
    to bottom,
    #e9a1a1,
    #d70606
  ); /* Gradient background */
  padding: 10px; /* Adjust padding to control the container size */
  border-radius: 8px; /* Rounded corners for the container */
  display: inline-block; /* Make the container inline-block to shrink to content */
  color: #fff; /* Text color on the gradient background */
}

.big-button-oppo {
  font-size: 18px; /* Adjust font size to make text bigger */
  background-color: transparent; /* Make button background transparent */
  color: #ffffff; /* Change text color */
  border: none;
  cursor: pointer;
  font-family: "Tourney", sans-serif;
}

.big-button-oppo:hover {
  background-color: #b30021; /* Change the background color on hover */
}

.gold-star {
  color: gold; /* Apply gold color to the trophy icon */
}

.button-container-disabled {
  background-image: linear-gradient(
    to bottom,
    #c7c3c3,
    #464444
  ); /* Gradient background */
  padding: 10px; /* Adjust padding to control the container size */
  border-radius: 8px; /* Rounded corners for the container */
  display: inline-block; /* Make the container inline-block to shrink to content */
  color: #313131; /* Text color on the gradient background */
}

.instruction-table {
  text-align: left;
  font-size: 14px;
  color: #120b51;
  margin-left: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
