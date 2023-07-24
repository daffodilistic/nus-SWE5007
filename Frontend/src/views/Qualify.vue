<template>
  <div id="account">
    <EditableTable
      v-model="users"
      :fields="fields"
      @submit="handleUpdateUser($event)"
      @remove="handleRemoveUser($event)"
    ></EditableTable>

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
      users:[],
    };
  },
  async mounted() {
    this.users = await axios.get(`http://localhost:8081/userinfo/users/`);
  },
  methods: {
    async handleUpdateUser(user) {
      if (user.id) {
        await updateUser(user);
      } else {
        await addUser(user);
      }
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