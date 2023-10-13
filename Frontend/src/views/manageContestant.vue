<template>
  <b-tabs>
    <b-tab
      v-for="option in filteredCompetitionChoices"
      :key="option.id"
      :title="option.text"
      @click="
        selectedCompetition = option.text;
        loadUser();
      "
    >
      <div>
        <br /><br />
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
                  placeholder="Search user Name"
                  class="search-box"
                />
              </td>
            </tr>
          </table>
        </div>
        <div v-if="users && users.length > 0">
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

          <div class="add-button">
            <b-button
              id="addNewuser"
              variant="outline-primary"
              @click="addNewUser"
              ><b-icon
                icon="person-plus"
                v-b-tooltip.hover="'Click to create new user'"
              ></b-icon>
            </b-button>
          </div>
          <br />
          <table class="main-table">
            <thead>
              <tr>
                <th></th>
                <th>S/No</th>
                <th>User Name</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Phone Number</th>
                <th>Country</th>
                <th>State</th>
                <th>Birthday</th>
                <th>School Name</th>
                <th>Experience<br />Year(s)</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody v-for="(user, index) in paginatedUsers" :key="index">
              <tr
                :class="{
                  'parent-row': true,
                  'active-row': activeRow === index,
                }"
                @click="toggleRow(index)"
              >
                <td></td>
                <td>{{ startIndex + index }}</td>
                <td v-if="!user.editing">
                  {{ user.userName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingUserName"
                    class="form-control editing-textbox"
                    :class="{
                      'is-invalid': !isUserNameAvailable(user.editingUserName),
                    }"
                  />
                  <div
                    v-if="!isUserNameAvailable(user.editingUserName)"
                    class="invalid-feedback"
                  >
                    Username is not available.
                  </div>
                </td>
                <td v-if="!user.editing">
                  {{ user.firstName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingFirstName"
                    class="form-control editing-textbox"
                  />
                </td>
                <td v-if="!user.editing">
                  {{ user.lastName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingLastName"
                    class="form-control editing-textbox"
                  />
                </td>
                <td v-if="!user.editing">
                  {{ user.email }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingEmail"
                    class="form-control editing-textbox"
                  />
                </td>

                <td v-if="!user.editing">
                  {{ user.phoneNumber }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingPhoneNumber"
                    class="form-control editing-textbox"
                  />
                </td>

                <td v-if="!user.editing">
                  {{ user.country }}
                </td>
                <td v-else>
                  <div class="form-group select">
                    <select
                      v-model="selectedCountry"
                      id="country"
                      class="editing-dropdown"
                    >
                      <option value="" disabled selected>Select Country</option>
                      <option
                        v-for="option in countriesOptions"
                        :value="option.id"
                        :key="option.id"
                      >
                        {{ option.name }}
                      </option>
                    </select>
                  </div>
                </td>

                <td v-if="!user.editing">
                  {{ user.state }}
                </td>
                <td v-else>
                  <div class="form-group select">
                    <select
                      v-model="selectedState"
                      id="state"
                      class="editing-dropdown"
                    >
                      <option value="" disabled selected>Select State</option>
                      <option
                        v-for="option in filteredStates"
                        :value="option.id"
                        :key="option.id"
                      >
                        {{ option.name }}
                      </option>
                    </select>
                  </div>
                </td>
                <td v-if="!user.editing">
                  {{ user.dateOfBirth }}
                </td>
                <td v-else>
                  <input
                    type="date"
                    v-model="user.editingDateOfBirth"
                    class="form-control editing-textbox"
                  />
                </td>

                <td v-if="!user.editing">
                  {{ user.schoolName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="user.editingSchoolName"
                    class="form-control editing-textbox"
                  />
                </td>

                <td v-if="!user.editing">
                  {{ user.yearsOfExp }}
                </td>
                <td v-else>
                  <input
                    type="number"
                    step="1"
                    min="0"
                    max="10"
                    v-model="user.editingYearsOfExp"
                    class="form-control editing-textbox"
                  />
                </td>

                <td>
                  <!-- Edit Icon -->
                  <b-button
                    id="edit-button"
                    @click="editingUser(startIndex + index - 1)"
                    variant="outline-primary"
                    class="delete-button"
                    v-b-tooltip.hover="'Click to edit user particulars'"
                  >
                    <span v-if="!user.editing"
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
                    v-b-tooltip.hover="'Click to delete user'"
                  >
                    <b-icon icon="trash"></b-icon>
                  </b-button>
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
import {
  api
} from "@/api";
import {
  competitionChoiceOptions,
  countriesOptions,
  statesOptions,
} from "../dropdownOptions";
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
      userName: "",
      firstName: "",
      lastName: "",
      country: "",
      state: "",
      selectedCountry: "", // Holds the selected country ID
      selectedState: "", // Holds the selected state ID
      email: "",
      phoneNumber: "",
      yearsOfExp: "",
      schoolName: "",
      dateOfBirth: "",
      searchQuery: "",
      users: [],
      activeRow: null,
      itemsPerPage: 10, // Number of users per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      userList: [],
      selectedCompetition: "Innovation Design Challenge",
    };
  },
  computed: {
    countriesOptions() {
      return countriesOptions;
    },
    statesOptions() {
      return statesOptions;
    },

    filteredStates() {
      return this.selectedCountry
        ? statesOptions.filter(
            (state) => state.countryId === this.selectedCountry
          )
        : [];
    },

    filteredUsers() {
      // If the users data is not available yet, return an empty array
      if (!this.users || this.users.length === 0) {
        return [];
      }

      // If the search query is empty, show all users
      if (this.searchQuery.trim() === "") {
        // Sort the users by "Stage Name" in ascending order (A to Z)
        return this.users.slice().sort((a, b) => {
          const stageA = a.firstName || "";
          const stageB = b.firstName || "";
          return stageA.localeCompare(stageB);
        });
      }

      // Otherwise, filter users based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.users.filter((user) =>
        user.firstName.toLowerCase().includes(query)
      );
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    },
    paginatedUsers() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredUsers.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredUsers.length;
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
      this.usersData = await axios.get(`${api.GET_ALL_USER_INFO_BASE_URL}`, {
        headers,
      });
      this.users = this.usersData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    isUserNameAvailable(username) {
      // Check if the username exists in any user's firstname property in the users array

      return !this.users.some(
        (user) =>
          user.firstName &&
          username &&
          user.firstName.toLowerCase() === username.toLowerCase()
      );
    },
    async toggleRow(index) {
      if (this.activeRow === index) {
        this.activeRow = null; // Collapse the row if it's already expanded
      } else {
        this.activeRow = index;
      }
    },
    async loadUser() {
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
        if (this.selectedCompetition === "Game Arena") {
          this.usersData = await axios.get(`${api.GET_ALL_USER_INFO_BASE_URL}`, {
            headers,
          });
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          this.usersData = await axios.get(`${api.GET_ALL_USER_INFO_BASE_URL}`, {
            headers,
          });
        }
        this.users = this.usersData.data.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    async addNewUser() {
      // Create a new user object and add it to the beginning of the users array
      const newUser = {
        userName: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        country: "",
        state: "",
        dateOfBirth: "",
        schoolName: "",
        yearsOfExp: 0,
        editing: true, // Set editing to true to enable editing mode for the new user
        editingUserName: "",
        editingFirstName: "",
        editingLastName: "",
        editingEmail: "",
        editingPhoneNumber: "",
        selectedCountry: "",
        selectedState: "",
        editingDateOfBirth: "",
        editingSchoolName: "",
        editingYearsOfExp: 0,
      };

      this.users.unshift(newUser);

      // Update the currentPage to 1 to ensure the newly added user appears on the first page
      this.currentPage = 1;
    },

    gotoPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },

    // Method to toggle editing mode for a user
    async editingUser(index) {
      const user = this.filteredUsers[index];

      const selectedCountryObject = countriesOptions.find(
        (country) => country.id === this.selectedCountry
      );
      let selectedCountryName = "";

      const selectedStateObject = statesOptions.find(
        (state) => state.id === this.selectedState
      );
      let selectedStateName = "";

      if (selectedCountryObject) {
        selectedCountryName = selectedCountryObject.name;
        console.log("Selected Country Name:", selectedCountryName);
      } else {
        console.log("Selected country not found in options.");
      }

      if (selectedStateObject) {
        selectedStateName = selectedStateObject.name;
      } else {
        console.log("Selected State not found in options.");
      }
      if (user.editing) {
        // Save the changes
        user.userName = user.editingUserName;
        user.firstName = user.editingFirstName;
        user.lastName = user.editingLastName;
        user.phoneNumber = user.editingPhoneNumber;
        user.email = user.editingEmail;
        user.country = this.selectedCountry;
        user.state = this.selectedState;
        user.schoolName = user.editingSchoolName;
        user.yearsOfExp = user.editingYearsOfExp;
        user.dateOfBirth = user.editingDateOfBirth;

        user.editing = false;
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
          let url = "";
          let url2 = "";
          if (this.selectedCompetition === "Game Arena") {
            url = api.UPDATE_USER_INFO_BASE_URL;
            url2 = api.CREATE_USER_INFO_BASE_URL;
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            url = api.UPDATE_USER_INFO_BASE_URL;
            url2 = api.CREATE_USER_INFO_BASE_URL;
          }
          // If the user has an ID, update the existing record using a PUT request
          if (user.id) {
            const requestBody = {
              id: user.id,
              userName: user.userName,
              firstName: user.firstName,
              lastName: user.lastName,
              email: user.email,
              phoneNumber: user.phoneNumber,
              country: selectedCountryName,
              state: selectedStateName,
              schoolName: user.schoolName,
              yearsOfExp: user.yearsOfExp,
              dateOfBirth: user.dateOfBirth,
            };
            const response = await axios.put(`${url}`, requestBody, {
              headers,
            });
          }
          // If the user doesn't have an ID, create a new record using a POST request
          else {
            const requestBody = {
              userName: user.userName,
              firstName: user.firstName,
              lastName: user.lastName,
              email: user.email,
              phoneNumber: user.phoneNumber,
              country: selectedCountryName,
              state: selectedStateName,
              schoolName: user.schoolName,
              yearsOfExp: user.yearsOfExp,
              dateOfBirth: user.dateOfBirth,
            };

            const response = await axios.post(`${url2}`, requestBody, {
              headers,
            });

            // Add the newly created user to the beginning of the users array
            this.users.unshift(response.data.data);
            url = "";
          }
          this.loadUser();
          // Optional: Perform any additional actions, such as updating the UI.
        } catch (error) {
          // Handle errors, if any
          console.error("Error saving user:", error);
        }
      } else {
        // Enter editing mode
        user.editingUserName = user.userName;
        user.editingFirstName = user.firstName;
        user.editingLastName = user.lastName;
        user.editingPhoneNumber = user.phoneNumber;
        user.editingEmail = user.email;
        this.selectedCountry = selectedCountryName;
        this.selectedState = selectedStateName;
        user.editingSchoolName = user.schoolName;
        user.editingYearsOfExp = user.yearsOfExp;
        user.editingDateOfBirth = user.dateOfBirth;
        user.editing = true;
      }
    },
    // Method to delete a user
    async deleteUser(index) {
      const user = this.filteredUsers[index];
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
          id: user.id,
        };

        try {
          const response = await axios.delete(`${api.DELETE_USER_INFO_BASE_URL}`, {
            data: requestBody,
            headers: headers,
          });

          // Show a success message
          Swal.fire({
            title: "Deleted!",
            text: "The user has been deleted.",
            icon: "success",
          });

          this.loadUser();
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

.active-row {
  background-color: rgb(218, 234, 253); /* Light blue for active parent row */
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
.editing-dropdown .dropdown-toggle::after {
  font-family: "Font Awesome"; /* Assuming you're using Font Awesome for icons */
  content: "\f107"; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}
</style>
