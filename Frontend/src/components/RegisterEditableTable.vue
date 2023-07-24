<template>
  <article>
    <b-modal
      id="modal-1"
      title="Confirm"
      v-model="openDialog"
      ok-title="Remove"
      @ok="removeRowsHandler"
    >
      <p class="my-4">Are you sure you want to remove the selected rows?</p>
    </b-modal>
    <div class="action-container">
      <table align="right">
        <tr>
          <td>
            <b-button @click="addRowHandler" variant="outline-primary"><b-icon icon="person-plus"></b-icon></b-button>
          </td>
        </tr>
      </table>
    </div>

    <br><br>
    <b-table class="b-table"
      id="table-1"
      :items="tableItems"
      :fields="fields" fixed
      :filter="filter"
      >

      <template v-for="(field, index) in fields" #[`cell(${field.key})`]="data">
        <b-form-datepicker
          v-if="field.type === 'date' && tableItems[data.index].isEdit"
          placeholder=""
          :key="index"
          :type="field.type"
          :value="tableItems[data.index][field.key]"
          :date-format-options="{ year: 'numeric', month: 'numeric', day: 'numeric' }"
          :max="new Date()"
          @input="(value) => inputHandler(value, data.index, field.key)"
        ></b-form-datepicker>

        <b-checkbox
          v-else-if="field.key === 'selectRow'"
          :checked="tableItems[data.index].isSelected"
          :key="index"
          @change="selectRowHandler(data)"
        ></b-checkbox>

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
      tableItems: this.mapItems(this.value),
      openDialog: false,
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
    selectRowHandler(data) {
      this.tableItems[data.index].isSelected =
        !this.tableItems[data.index].isSelected;
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

    mapItems(data) {
      return data.map((item, index) => ({
        ...item,
        isEdit: this.tableItems[index] ? this.tableItems[index].isEdit : false,
        isSelected: this.tableItems[index]
          ? this.tableItems[index].isSelected
          : false,
      }));
    },
  },
};
</script>
<style>
.action-container {
  margin-bottom: 10px;
}
.action-container button {
  margin-right: 25px;
}
.delete-button {
  margin-left: 5px;
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

</style>