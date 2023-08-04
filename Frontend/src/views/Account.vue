<template>
  <div id="account">
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
import EditableTable from "@/components/UserEditableTable.vue";
import axios from "axios";
import { IDC_TEAM_BASE_URL, USER_INFO_BASE_URL } from '@/api';

export default {
  name: "account",
  components: {
    EditableTable,
  },
  data() {
    return {
      fields: [
        //{ key: "selectRow", label: "" },
        { key: "firstName", label: "First Name", type: "text", required: true },
        { key: "lastName", label: "Last Name", type: "text", required: true },
        { key: "email", label: "Email", type: "email", required: true, pattern: ".+@bootstrapvue\\.com" },
        { key: "country", label: "Country", type: "select", required: true },
        { key: "state", label: "State", type: "select", required: true },
        { key: "dateOfBirth", label: "Birthday", type: "date", required: true },
        { key: "schoolName", label: "School Name", type: "text" },
        { key: "yearsOfExp", label: "Experience (Year)", type: "number" },
        { key: "edit", label: "", type: "edit" },
      ],
      users:[],
    };
  },
  async mounted() {
    try {
      this.usersData = await axios.get(`${USER_INFO_BASE_URL}userinfo/users`);
      this.users = this.usersData.data.data;
    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    async handleUpdateUser(user) {
      this.removeExtraProperty();


      if (user.id) {
        // User has an ID, so it already exists in the backend, update the user
        try {
          await axios.put(`${USER_INFO_BASE_URL}userinfo/user`, user);
          // Update the user in the users array after successful API update
          const index = this.users.findIndex(u => u.id === user.id);
          if (index !== -1) {
            this.$set(this.users, index, user);
          }
        } catch (error) {
          console.error("Error updating user:", error);
        }
      } else {
        // User doesn't have an ID, so it is a new user, add the user
        try {
          const response = await axios.post(`${USER_INFO_BASE_URL}userinfo/user`, user);
          // Add the new user to the users array after successful API add
          this.users.push(response.data.data);
        } catch (error) {
          console.error("Error adding user:", error);
        }
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
    },
    removeExtraProperty() {
      this.users = this.users.map(user => {
        // Create a new object without the isEdit property
        const { isSelected,isEdit,edit, ...rest } = user;
        return rest;
      });
    },
  },

};
</script>
<style scoped>
#account {
  margin: 20px;
}
</style>