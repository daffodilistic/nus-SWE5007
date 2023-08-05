<template>
  <article>
    <b-modal v-if="selectedUser" v-model="openModal" title="Edit User">
      <div class="form-group">
        <label for="firstName">First Name</label>
        <b-form-input id="firstName" v-model="selectedUser.firstName" />
      </div>
      <div class="form-group">
        <label for="lastName">Last Name</label>
        <b-form-input id="lastName" v-model="selectedUser.lastName" />
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <b-form-input id="email" v-model="selectedUser.email" type="email" />
      </div>
      <!-- Add other fields for editing here -->
      <b-button variant="primary" @click="saveChanges">Save</b-button>
    </b-modal>
    <!--
    <b-modal
      id="modal-1"
      title="Confirm"
      v-model="openDialog"
      ok-title="Remove"
      @ok="removeRowsHandler"
    >
      <p class="my-4">Are you sure you want to remove the selected rows?</p>
    </b-modal>
    -->
    <div class="search-container">
      <table>
        <tr>
          <td><p class="h3 mb-2"><b-icon icon="search" style='color: rgb(65, 127, 202)'></b-icon></p></td>&nbsp;
          <td><input type="text" v-model="filter" placeholder="Search.." class="search-box"></td>
        </tr>
        <tr>&nbsp;</tr>
        <tr>&nbsp;</tr>
      </table>
    </div>
    <div class="add-button">
      <b-button variant="outline-primary" size="lg" @click="addRowHandler"><b-icon icon="person-plus" ></b-icon>
      </b-button><br>

    </div>
          <!--
          <td>
            <b-button variant="danger" @click="openDialog = true">Remove Rows</b-button>
          </td>
          -->
          <p class="mt-3">{{positionText}}</p><br>
    <b-table class="b-table"
      id="table-1"
      :items="tableItems"
      :fields="fields" fixed
      :per-page="perPage"
      :current-page="currentPage"
      :filter="filter"
      >
      <template #[`cell(${field.key})`]="data">
        <div @click="openEditModal(data.item)">
          {{ data.value }}
        </div>
      </template>

      <template v-for="(field, index) in fields" #[`cell(${field.key})`]="data">
        <b-form-datepicker
          v-if="field.type === 'date' && tableItems[data.index].isEdit"
          :key="index"
          :type="field.type"
          :value="tableItems[data.index][field.key]"
          @input="(value) => inputHandler(value, data.index, field.key)"
        ></b-form-datepicker>

        <b-form-select
        v-else-if="field.type === 'select' && field.key === 'country' && tableItems[data.index].isEdit"
        :key="index"
        :options="countriesOptions.map(item => item.name)"
        v-model="tableItems[data.index][field.key]"
        class="country-select"
        ref="countrySelect"
        @input="handleCountrySelect(data.index)"
        >
      <template v-slot:button-content="{ text }">
          <div class="country-dropdown">
            {{ text }}
          </div>
        </template>
      </b-form-select>

      <b-form-select
        v-else-if="field.type === 'select' && field.key === 'state' && tableItems[data.index].isEdit"
        :key="index"
        :options="selectedCountryStates.map((state) => ({
          text: state.name,
          value: state.name,
        }))"
        v-model="tableItems[data.index][field.key]"
        class="state-select"
        ref="stateSelect"
        :disabled="!tableItems[data.index].country"
        :class="{ 'disabled-state-select': !tableItems[data.index].country }"
        @input="handleStateSelect(data.index)"
      >
        <template v-slot:button-content="{ text }">
          <div class="state-dropdown">
            {{ text }}
          </div>
        </template>
      </b-form-select>

      <b-form-input
    v-else-if="field.type === 'number' && field.key === 'yearsOfExp'"
    :key="index"
    :type="field.type"
    :value="tableItems[data.index][field.key]"
    @blur="(e) => inputHandler(e.target.value, data.index, field.key)"
    :min="0"
  ></b-form-input>

        <b-form-select
          v-else-if="field.type === 'select' && tableItems[data.index].isEdit"
          :key="index"
          :value="tableItems[data.index][field.key]"
          @input="(value) => inputHandler(value, data.index, field.key)"
          :options="field.options"
        ></b-form-select>
        <!--
        <b-checkbox
          v-else-if="field.key === 'selectRow'"
          :checked="tableItems[data.index].isSelected"
          :key="index"
          @change="selectRowHandler(data)"
        ></b-checkbox>
        -->
        <div :key="index" v-else-if="field.type === 'edit'">
          <b-button @click="editRowHandler(data)" variant="outline-primary">
            <span v-if="!tableItems[data.index].isEdit"><b-icon icon="pencil"></b-icon></span>
            <span v-else><b-icon icon="save"></b-icon></span>
          </b-button>
          <b-button
            class="delete-button"
            variant="outline-primary"
            @click="removeRowHandler(data.index)"
            ><b-icon icon="trash"></b-icon></b-button
          >
        </div>
        <b-form-input
          v-else-if="field.type && tableItems[data.index].isEdit"
          :key="index"
          :type="field.type"
          :value="tableItems[data.index][field.key]"
          @blur="(e) => inputHandler(e.target.value, data.index, field.key)"
        ></b-form-input>
        <span :key="index" v-else>{{ data.value }}</span>
      </template>
    </b-table>
    <b-pagination
      v-model="currentPage"
      pills :total-rows="rows"
      :per-page="perPage"
      aria-controls="table-1"
      align="center"
    ></b-pagination>


  </article>
</template>


<script>
import { countriesOptions,statesOptions } from '../dropdownOptions';
export default {
  name: "EditableTable",
  components: {},
  props: {
    value: Array,
    fields: Array,
  },
  data() {
    return {
      filter: '',
      perPage: 10,
      currentPage: 1,
      tableItems: this.mapItems(this.value),
      openDialog: false,
      openModal: false,
      selectedUser: null,
      selectedCountry: null,
      countriesOptions: countriesOptions,
      selectedCountryStates: [],
    };
  },
  watch: {
    value(newVal) {
      this.tableItems = this.mapItems(newVal);
    },
  },
  computed: {
      rows() {
        return this.tableItems.length
      },
      positionText() {
      var endIndex = this.currentPage * this.perPage,
        startIndex = ((this.currentPage - 1) * this.perPage) + 1;

      return "Showing "+startIndex+ " to "+ (endIndex>this.tableItems.length? this.tableItems.length :endIndex)  + " of " + this.tableItems.length + " records";
    }
    },
  methods: {
    editRowHandler(data) {
      if (this.tableItems[data.index].isEdit) {
        this.$emit("submit", this.tableItems[data.index]);
      }
      this.tableItems[data.index].isEdit = !this.tableItems[data.index].isEdit;
    },
    inputHandler(value, index, key) {
      this.tableItems[index][key] = value;
      this.$set(this.tableItems, index, this.tableItems[index]);
      this.$emit("input", this.tableItems);
    },
    addRowHandler() {
      const newRow = this.fields.reduce(
        (a, c) => ({ ...a, [c.key]: null }),
        {}
      );
      newRow.isEdit = true;
      this.tableItems.unshift(newRow);
      this.$emit("input", this.tableItems);
    },
    removeRowHandler(index) {
      this.tableItems = this.tableItems.filter((item, i) => i !== index);
      this.$emit("input", this.tableItems);
      this.$emit("remove", this.tableItems[index]);
    },
    removeRowsHandler() {
      const selectedItems = this.tableItems.filter(item => item.isSelected);
      this.tableItems = this.tableItems.filter(item => !item.isSelected);
      this.$emit("input", this.tableItems);
      this.$emit("remove", selectedItems);
    },
    /*
    selectRowHandler(data) {
      this.tableItems[data.index].isSelected =
        !this.tableItems[data.index].isSelected;
    },
    */
    openEditModal(user) {
      this.selectedUser = { ...user }; // Create a copy of the user object to avoid modifying the original data directly
      this.openModal = true;
    },
    saveChanges() {
      // Implement the logic to save the changes to the backend or update the user data in the table
      // For demonstration purposes, let's just update the tableItems directly (without API call)
      const index = this.tableItems.findIndex(user => user.id === this.selectedUser.id);
      if (index !== -1) {
        this.tableItems[index] = { ...this.selectedUser }; // Update the user in tableItems
      }
      this.openModal = false; // Close the modal after saving changes
    },
    mapItems(data) {
      return data.map((item, index) => ({
        ...item,
        isEdit: this.tableItems[index] ? this.tableItems[index].isEdit : false,
        isSelected: this.tableItems[index]
          ? this.tableItems[index].isSelected
          : false,
      }));
    },
    handleCountrySelect(index) {
      const selectedCountryName = this.tableItems[index].country;
      this.selectedCountry = this.countriesOptions.find(
        (country) => country.name === selectedCountryName
      );
      console.log("Selected Country:", selectedCountryName, this.selectedCountry);

      this.tableItems[index].state = null; // Reset the selected state value to null
      // Emit the changes back to the parent component
      this.$emit("input", this.tableItems);

      // Update the state dropdown options based on the selected country
      this.updateStateDropdown();
    },
    handleStateSelect(index) {
      const selectedStateName = this.tableItems[index].state;
      this.tableItems[index].state = selectedStateName; // Update the state value in the users array

      // Emit the changes back to the parent component
      this.$emit("input", this.tableItems);
    },
    updateStateDropdown() {
      if (this.selectedCountry) {
        const selectedCountryId = this.selectedCountry.id;
        this.selectedCountryStates = statesOptions.filter(
          (state) => state.countryId === selectedCountryId
        );
      } else {
        this.selectedCountryStates = [];
      }
    },
  },
};
</script>
<style scoped>

.delete-button {
  margin-left: 5px;
}

.search-container {
  display: flex;
  justify-content: flex-end; /* Aligns the search box to the right */
  margin-bottom: 20px;
  margin-right:30px;
}


/* Search Box Styles */
.search-box {
  padding: 8px 16px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* Adjust the width as needed */
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

.country-dropdown {
  max-width: 150px; /* Set your desired maximum width here */
  white-space: nowrap; /* Prevent text from wrapping */
  overflow: hidden; /* Hide any overflowing text */
  text-overflow: ellipsis; /* Show ellipsis (...) for truncated text */
}

.country-select {
  width: 140px; /* Set your desired width here */
  height: 36px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.state-dropdown {
  max-width: 150px; /* Set your desired maximum width here */
  white-space: nowrap; /* Prevent text from wrapping */
  overflow: hidden; /* Hide any overflowing text */
  text-overflow: ellipsis; /* Show ellipsis (...) for truncated text */
}

.state-select {
  width: 140px; /* Set your desired width here */
  height: 36px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.state-select[disabled] {
    background-color: #f5f5f5; /* Set your desired disabled background color */
    cursor: not-allowed;
    pointer-events: none;
  }
/* Add the following styles for the table cells */
.b-table .table td {
  max-width: 200px; /* Set your desired maximum width here */
  white-space: nowrap; /* Prevent text from wrapping */
  overflow: hidden; /* Hide any overflowing text */
  text-overflow: ellipsis; /* Show ellipsis (...) for truncated text */
}

/* Additional styles for the edit and delete buttons to keep them on the same line */
.b-table .table .btn {
  margin-right: 5px; /* Add some space between the buttons */
}
</style>