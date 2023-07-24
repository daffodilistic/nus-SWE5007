<template>
  <div id="account">
    <b-modal v-if="selectedUser" @hide="selectedUser = null">
      <h5>Selected User's Country: {{ selectedUser.country }}</h5>
    </b-modal>
    <EditableTable
      v-model="users"
      :fields="fields"
      @submit="handleUpdateUser($event)"
      @remove="handleRemoveUser($event)"
      @row-clicked="handleRowClick($event)"
    ></EditableTable>
  </div>

</template>
<script>
import EditableTable from "@/components/TeamEditableTable.vue";
import { addUser, deleteUser, getUsers, updateUser } from "@/services/user";

export default {
  name: "account",
  components: {
    EditableTable,
  },
  data() {
    return {
      fields: [
        { key: "selectRow", label: "" },
        { key: "firstName", label: "First Name", type: "text", required: true },
        { key: "lastName", label: "Last Name", type: "text", required: true },
        { key: "email", label: "Email", type: "email", required: true, pattern: ".+@bootstrapvue\\.com" },
        { key: "country", label: "Country", type: "text", required: true },
        { key: "state", label: "State", type: "text", required: true },
        { key: "dateOfBirth", label: "Birthday", type: "date", required: true },
        { key: "schoolName", label: "School Name", type: "text" },
        { key: "yearsOfExp", label: "Experience", type: "number" },
        { key: "edit", label: "", type: "edit" },
      ],
      users:[],

    };
  },
  async mounted() {
    this.users = await getUsers();
  },
  methods: {
    async handleUpdateUser(user) {
      if (user.id) {
        await updateUser(user);
      } else {
        await addUser(user);
      }
    },
    handleRowClick(user) {
      this.selectedUser = user;
    },
    async handleRemoveUser(user) {
      if (user.length > 0) {
        await user.map(async (item) => {
          await deleteUser(item.id);
        })
      } else {
        await deleteUser(user.id);
      }
    }
  },

};
</script>
<style>
#account {
  margin: 20px;
}
</style>