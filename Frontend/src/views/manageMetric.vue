<template>
  <div>
    <br><br>
    <div class="search-container">
      <table>
        <tr>
          <td><p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
          <td><input type="text" v-model="searchQuery" placeholder="Search Metric Name" class="search-box"></td>
        </tr>
      </table>
    </div>
    <div v-if="metrics && metrics.length > 0">
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

    <div class="add-button">
      <b-button variant="outline-primary" size="lg" @click="addNewMetric"><b-icon icon="file-earmark-plus" ></b-icon>
      </b-button><br>

    </div>
    <table class="main-table">
      <thead>
        <tr>
          <th></th>
          <th>Stage Name</th>
          <th>Metric Name</th>
          <th>Metric Weight</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody v-for="(metric, index) in paginatedMetrics" :key="index">
        <tr :class="{'parent-row': true, 'active-row': activeRow === index}" @click="toggleRow(index)">
          <td>
            <i :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'" class="expand-icon" @click="toggleRow(index)"></i>

          </td>
          <td v-if="!metric.editing">
            {{ metric.stageName }}
          </td>
          <td v-else>
            <input
              type="text"
              v-model="metric.editingStageName"
              @keydown.enter="saveMetric(metric)"
              @keydown.esc="cancelEditing(metric)"
              class="form-control editing-textbox"
            />
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
            {{ (metric.metricWeight * 100) + "%" }}
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
            <b-button @click="editMetric(index)" variant="outline-primary" class="delete-button">
              <span v-if="!metric.editing"><b-icon icon="pencil"></b-icon></span>
              <span v-else><b-icon icon="save"></b-icon></span>
            </b-button>
            <!-- Delete Icon -->
            <b-button
            class="delete-button"
            variant="outline-primary"
            @click="deleteMetric(index)"
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
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, userIndex) in metric.userResponses" :key="userIndex" class="child-row">
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <td> {{ userIndex + 1 }} </td>
                  <td> {{ user.firstName }} </td>
                  <td> {{ user.lastName }} </td>
                  <td> {{ user.email }} </td>
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
      currentMetricId: "",
      currentMetricName: "",
      searchQuery: '',
      metrics:[],
      activeRow: null,
      itemsPerPage: 10, // Number of metrics per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      userList: [],
    };
  },
  computed: {

    filteredMetrics() {
    // If the metrics data is not available yet, return an empty array
    if (!this.metrics || this.metrics.length === 0) {
      return [];
    }

    // If the search query is empty, show all metrics
    if (this.searchQuery.trim() === '') {
      return this.metrics;
    }

    // Otherwise, filter metrics based on the search query
    const query = this.searchQuery.trim().toLowerCase();
    return this.metrics.filter((metric) => metric.metricName.toLowerCase().includes(query));
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
    try {
      this.metricsData = await axios.get(`${IDC_TEAM_BASE_URL}idcmetrics/metrics`);
      this.metrics = this.metricsData.data.data;
      console.log('Response from server:', metrics.data);
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    addNewMetric() {
      // Create a new metric object and add it to the beginning of the metrics array
      const newMetric = {
        stageName: "",
        metricName: "",
        metricWeight: 0,
        editing: true, // Set editing to true to enable editing mode for the new metric
        editingStageName: "",
        editingMetricName: "",
        editingMetricWeight: 0,
        userResponses: [],
      };

      this.metrics.unshift(newMetric);

      // Update the currentPage to 1 to ensure the newly added metric appears on the first page
      this.currentPage = 1;

      // Scroll to the newly added metric (optional)
      this.$nextTick(() => {
        const metricRowElement = document.getElementById(`metric-row-0`);
        if (metricRowElement) {
          metricRowElement.scrollIntoView({ behavior: "smooth", block: "center" });
        }
      });
    },

    async toggleRow(index) {
    if (this.activeRow === index) {
      this.activeRow = null; // Collapse the row if it's already expanded
    } else {
      this.activeRow = index;
      const metric = this.filteredMetrics[index];

      //try {
        // Make the API call here using the metric ID as the request body
       //const requestBody = {
          //id: metric.id,
        //};

        // Make the HTTP PUT request to the API endpoint
        //const requestBodyJson = JSON.stringify(requestBody);
       // console.log('Request Body:', requestBodyJson);

        //const response = await axios.get(`${IDC_TEAM_BASE_URL}idcmetric/metric`, requestBodyJson);

        // Handle the response, if needed
        //console.log('Response from server:', response.data);

        // Optional: Perform any additional actions, such as updating the UI.
     // } catch (error) {
        // Handle errors, if any
        //console.error('Error calling API:', error);
      //}
    }
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

          const requestBody = {
            stageName: metric.stageName,
            metricName: metric.metricName,
            metricWeight: metric.metricWeight
          };

          console.log('Request Payload:', requestBody);

          try {
            if (metric.id) {
              // If the metric has an ID, update the existing record using a PUT request
              const response = await axios.post(`${IDC_TEAM_BASE_URL}idcmetrics/metric`, requestBody);
              console.log('Response from server (update):', response.data);
            } else {
              // If the metric doesn't have an ID, create a new record using a POST request
              const response = await axios.post(`${IDC_TEAM_BASE_URL}idcmetrics/metric`, requestBody);
              console.log('Response from server (create):', response.data);

              // Add the newly created metric to the beginning of the metrics array
              this.metrics.unshift(response.data);
            }

            // Close the modal after the request is successful
            this.showAddMemberModal = false;

            // Optional: Perform any additional actions, such as updating the UI.
          } catch (error) {
            // Handle errors, if any
            console.error('Error saving metric:', error);
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
      deleteMetric(index) {
        if (confirm("Are you sure you want to delete this metric?")) {
          this.filteredMetrics.splice(index, 1);
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
        this.selectedUsers = this.selectedUsers.filter((selectedUserId) => selectedUserId !== userId);
      } else {
        this.selectedUsers.push(userId);
      }
      const jsonPayload = {
        userIds: this.selectedUsers,
      };
      console.log(jsonPayload)
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

.add-metric-button {
  padding: 10px 20px;
  background-color: #2ecc71;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
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



</style>