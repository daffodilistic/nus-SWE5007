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
      <b-button class="add-button" variant="success" @click="addRowHandler"
        >Add Row</b-button
      >
      <b-button variant="danger" @click="openDialog = true"
        >Remove Rows</b-button
      >
    </div>
    <b-table class="b-table" 
      id="table-1"
      :items="tableItems" 
      :fields="fields" fixed 
      :per-page="perPage"
      :current-page="currentPage"
      >
      <template v-for="(field, index) in fields" #[`cell(${field.key})`]="data">
        <b-form-datepicker
          v-if="field.type === 'date' && tableItems[data.index].isEdit"
          :key="index"
          :type="field.type"
          :value="tableItems[data.index][field.key]"
          @input="(value) => inputHandler(value, data.index, field.key)"
        ></b-form-datepicker>
        <b-form-select
          v-else-if="field.type === 'select' && tableItems[data.index].isEdit"
          :key="index"
          :value="tableItems[data.index][field.key]"
          @input="(value) => inputHandler(value, data.index, field.key)"
          :options="field.options"
        ></b-form-select>
        <b-checkbox
          v-else-if="field.key === 'selectRow'"
          :checked="tableItems[data.index].isSelected"
          :key="index"
          @change="selectRowHandler(data)"
        ></b-checkbox>
        <div :key="index" v-else-if="field.type === 'edit'">
          <b-button @click="editRowHandler(data)">
            <span v-if="!tableItems[data.index].isEdit">Edit</span>
            <span v-else>Done</span>
          </b-button>
          <b-button
            class="delete-button"
            variant="danger"
            @click="removeRowHandler(data.index)"
            >Remove</b-button
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
    <p class="mt-3"><b>Current Page: {{ currentPage }}</b></p>
    <p class="mt-3">Showing 1 to 10 of {{rows}} entries</p>

  </article>
</template>
<script>
export default {
  name: "EditableTable",
  components: {},
  props: {
    value: Array,
    fields: Array,
  },
  data() {
    return {
      perPage: 7,
      currentPage: 1,
      tableItems: this.mapItems(this.value),
      openDialog: false,
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
    selectRowHandler(data) {
      this.tableItems[data.index].isSelected =
        !this.tableItems[data.index].isSelected;
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
  margin-right: 5px;
}
.delete-button {
  margin-left: 5px;
}
</style>