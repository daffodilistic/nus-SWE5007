<template>
  <b-tabs>
    <b-tab
      v-for="option in filteredCompetitionChoices"
      :key="option.id"
      :title="option.text"
      @click="
        selectedCompetition = option.text;
        loadMetric();
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
                  placeholder="Search Metric Name"
                  class="search-box"
                />
              </td>
            </tr>
          </table>
        </div>
        <div v-if="metrics && metrics.length > 0">
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
              variant="outline-primary"
              id="addNewMetric"
              @click="addNewMetric"
              ><b-icon
                icon="file-earmark-plus"
                v-b-tooltip.hover="'Click to create metric'"
              ></b-icon> </b-button
            ><br />
          </div>
          <br />
          <table class="main-table">
            <thead>
              <tr>
                <th></th>
                <th>S/No</th>
                <th>Stage Name</th>
                <th>Metric Name</th>
                <th>Metric Weight</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody v-for="(metric, index) in paginatedMetrics" :key="index">
              <tr
                :class="{
                  'parent-row': true,
                  'active-row': activeRow === index,
                }"
                @click="toggleRow(index)"
              >
                <td></td>
                <td>{{ startIndex + index }}</td>
                <td v-if="!metric.editing">
                  {{ metric.stageName }}
                </td>
                <td v-else>
                  <div class="form-group select">
                    <select
                      v-model="stageName"
                      id="category"
                      class="editing-dropdown"
                    >
                      <option value="" disabled selected>
                        Select Qualification Status
                      </option>
                      <option
                        v-for="option in filteredStageNameOptions"
                        :value="option.value"
                        :key="option.value"
                      >
                        {{ option.text }}
                      </option>
                    </select>
                  </div>
                </td>
                <td v-if="!metric.editing">
                  {{ metric.metricName }}
                </td>
                <td v-else>
                  <input
                    type="text"
                    v-model="metric.editingMetricName"
                    @keydown.enter="saveMetric(metric)"
                    @keydown.esc="cancelEditing(metric)"
                    class="form-control editing-textbox"
                  />
                </td>
                <td v-if="!metric.editing">
                  {{ metric.metricWeight * 100 + "%" }}
                </td>
                <td v-else>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    max="1"
                    v-model="metric.editingMetricWeight"
                    @keydown.enter="saveMetric(metric)"
                    @keydown.esc="cancelEditing(metric)"
                    class="form-control editing-textbox"
                  />
                </td>

                <td>
                  <!-- Edit Icon -->
                  <b-button
                    id="edit-button"
                    @click="editMetric(startIndex + index - 1)"
                    variant="outline-primary"
                    class="delete-button"
                    v-b-tooltip.hover="'Click to edit metric'"
                  >
                    <span v-if="!metric.editing"
                      ><b-icon icon="pencil"></b-icon
                    ></span>
                    <span v-else><b-icon icon="save"></b-icon></span>
                  </b-button>
                  <!-- Delete Icon -->
                  <b-button
                    class="delete-button"
                    id="delete-button"
                    variant="outline-primary"
                    @click="deleteMetric(startIndex + index - 1)"
                    v-b-tooltip.hover="'Click to delete metric'"
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
import { api } from "../api";
import { competitionChoiceOptions, stageNameOptions } from "../dropdownOptions";
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
      stageName: "",
      currentMetricId: "",
      currentMetricName: "",
      searchQuery: "",
      metrics: [],
      activeRow: null,
      itemsPerPage: 10, // Number of metrics per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      userList: [],
      selectedCompetition: "Innovation Design Challenge",
    };
  },
  computed: {
    stageNameOptions() {
      return stageNameOptions;
    },
    filteredStageNameOptions() {
      if (this.selectedCompetition === "Game Arena") {
        return this.stageNameOptions.filter(
          (option) => option.competitionId === "2"
        );
      } else if (this.selectedCompetition === "Innovation Design Challenge") {
        return this.stageNameOptions.filter(
          (option) =>
            option.competitionId === "1" && option.value !== "Not Qualified"
        );
      }
      return [];
    },
    filteredMetrics() {
      // If the metrics data is not available yet, return an empty array
      if (!this.metrics || this.metrics.length === 0) {
        return [];
      }

      // If the search query is empty, show all metrics
      if (this.searchQuery.trim() === "") {
        // Sort the metrics by "Stage Name" in ascending order (A to Z)
        return this.metrics.slice().sort((a, b) => {
          const stageA = a.stageName || "";
          const stageB = b.stageName || "";
          return stageA.localeCompare(stageB);
        });
      }

      // Otherwise, filter metrics based on the search query
      const query = this.searchQuery.trim().toLowerCase();
      return this.metrics.filter((metric) =>
        metric.metricName.toLowerCase().includes(query)
      );
    },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    filteredMetricNameChoices() {
      return metricNameOptions;
    },
    filteredStatusChoices() {
      return QualificationOptions;
    },
    totalPages() {
      return Math.ceil(this.filteredMetrics.length / this.itemsPerPage);
    },
    paginatedMetrics() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredMetrics.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredMetrics.length;
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
      this.metricsData = await axios.get(`${api.GET_ALL_IDC_METRIC_BASE_URL}`, {
        headers,
      });
      this.metrics = this.metricsData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching metrics:", error);
    }
  },
  methods: {
    async toggleRow(index) {
      if (this.activeRow === index) {
        this.activeRow = null; // Collapse the row if it's already expanded
      } else {
        this.activeRow = index;
      }
    },
    async loadMetric() {
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
          this.metricsData = await axios.get(
            `${api.GET_ALL_GAME_METRIC_BASE_URL}`,
            { headers }
          );
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          this.metricsData = await axios.get(
            `${api.GET_ALL_IDC_METRIC_BASE_URL}`,
            {
              headers,
            }
          );
        }
        this.metrics = this.metricsData.data.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    async addNewMetric() {
      // Create a new metric object and add it to the beginning of the metrics array
      const newMetric = {
        stageName: "",
        metricName: "",
        metricWeight: 0,
        editing: true, // Set editing to true to enable editing mode for the new metric
        editingStageName: "",
        editingMetricName: "",
        editingMetricWeight: 0,
      };

      this.metrics.unshift(newMetric);

      // Update the currentPage to 1 to ensure the newly added metric appears on the first page
      this.currentPage = 1;
    },

    gotoPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },

    // Method to toggle editing mode for a metric
    async editMetric(index) {
      const metric = this.filteredMetrics[index];

      if (metric.editing) {
        // Save the changes
        metric.metricName = metric.editingMetricName;
        metric.stageName = metric.editingStageName;
        metric.metricWeight = parseFloat(metric.editingMetricWeight);

        metric.editing = false;

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
            url = api.UPDATE_GAME_METRIC_BASE_URL;
            url2 = api.CREATE_GAME_METRIC_BASE_URL;
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            url = api.UPDATE_IDC_METRIC_BASE_URL;
            url2 = api.CREATE_IDC_METRIC_BASE_URL;
          }

          if (metric.id) {
            const requestBody = {
              id: metric.id,
              stageName: metric.stageName,
              metricName: metric.metricName,
              metricWeight: metric.metricWeight,
            };
            // If the metric has an ID, update the existing record using a PUT request
            const response = await axios.put(`${url}`, requestBody, {
              headers,
            });
          } else {
            const requestBody = {
              stageName: this.stageName,
              metricName: metric.metricName,
              metricWeight: metric.metricWeight,
            };

            // If the metric doesn't have an ID, create a new record using a POST request
            const response = await axios.post(`${url2}`, requestBody, {
              headers,
            });

            // Add the newly created metric to the beginning of the metrics array
            this.metrics.unshift(response.data.data);
            url = "";
          }
          this.loadMetric();
          // Optional: Perform any additional actions, such as updating the UI.
        } catch (error) {
          // Handle errors, if any
          console.error("Error saving metric:", error);
        }
      } else {
        // Enter editing mode
        metric.editingMetricName = metric.metricName;
        metric.editingStageName = metric.stageName;
        metric.editingMetricWeight = metric.metricWeight;
        metric.editing = true;
      }
    },
    // Method to delete a metric
    async deleteMetric(index) {
      const metric = this.filteredMetrics[index];
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
          id: metric.id,
        };

        try {
          if (this.selectedCompetition === "Game Arena") {
            response = await axios.delete(
              `${api.DELETE_GAME_METRIC_BASE_URL}`,
              {
                data: requestBody,
                headers: headers,
              }
            );
          } else if (
            this.selectedCompetition === "Innovation Design Challenge"
          ) {
            response = await axios.delete(`${api.DELETE_IDC_METRIC_BASE_URL}`, {
              data: requestBody,
              headers: headers,
            });
          }

          // Show a success message
          Swal.fire({
            title: "Deleted!",
            text: "The metric has been deleted.",
            icon: "success",
          });

          loadMetric();
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }
    },
    // Method to add a new child row
    addChildRow(metricIndex) {
      const metric = this.filteredMetrics[metricIndex];
      metric.userResponses.push({ ...this.newChildRow });
      // Set the initial country and state values for the new child row
      const newUserIndex = metric.userResponses.length - 1;
      const newUser = metric.userResponses[newUserIndex];
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
