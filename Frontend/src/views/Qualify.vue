<template>
  <div id="account">
    <EditableTable
      v-model="users"
      :fields="fields"
      @submit="handleUpdateUser($event)"
      @remove="handleRemoveUser($event)"
    ></EditableTable>
    {{ users }}

  </div>

</template>
<script>
import EditableTable from "@/components/EditableTable.vue";
import { addUser, deleteUser, getUsers, updateUser } from "@/services/user";
import axios from "axios";

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
        { key: "edit", label: "", type: "edit" },
      ],
      users: [],
    };
  },
  async mounted() {
    try {
      this.users = await axios.get(`http://localhost:8080/idcteam/team`);
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    // Your other methods here...
  },
};
</script>
<style>
#account {
  margin: 20px;
}
</style>