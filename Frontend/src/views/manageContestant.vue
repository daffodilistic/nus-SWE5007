<template>
  <div id="account">
    <b-tabs >
        <b-tab v-for="option in competitionChoiceOptions" :key="option.id" :title="option.text" @click="selectedCompetition = option.text; loadUsers()">
        <EditableTable
          v-model="users"
          :fields="fields"
          @submit="handleUpdateUser($event)"
          @remove="handleRemoveUser($event)"
          @row-clicked="handleRowClick($event)"
        ></EditableTable>
      </b-tab>
    </b-tabs>
  </div>
</template>
<script>
import EditableTable from "@/components/UserEditableTable.vue";
import axios from "axios";
import { GET_ALL_USER_INFO_BASE_URL,UPDATE_USER_INFO_BASE_URL,CREATE_USER_INFO_BASE_URL} from '@/api';
import { competitionChoiceOptions } from "../dropdownOptions";
import token from '/config'
export default {
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
      selectedCompetition: "Innovation Design Challenge",
    };
  },

  async mounted() {
    try {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };

    const response = await axios.get(`${GET_ALL_USER_INFO_BASE_URL}`, { headers });
    this.usersData = response.data.data;
    this.users = this.usersData;
  } catch (error) {
    // Handle any errors that might occur during the request
    console.error("Error fetching users:", error);
  }
  },
  computed: {
    competitionChoiceOptions() {
      return competitionChoiceOptions;
    },
  },
  methods: {

  async loadUsers() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
      try {
        if (this.selectedCompetition === "Game Arena") {
          this.usersData = await axios.get("http://localhost:3000/data", { headers });
        console.log('ga called')
        } else if (this.selectedCompetition === "Innovation Design Challenge") {
          this.usersData = await axios.get(`${GET_ALL_USER_INFO_BASE_URL}`, { headers });
          console.log('IDC called')
        }
        this.users = this.usersData.data.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    async handleUpdateUser(user) {
      this.removeExtraProperty();
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };

      if (user.id) {
        // User has an ID, so it already exists in the backend, update the user
        try {
          await axios.put(`${UPDATE_USER_INFO_BASE_URL}`, user, { headers });
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
        console.log('create block')
        try {
          const response = await axios.post(`${CREATE_USER_INFO_BASE_URL}`, user, { headers });
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

#account .table td {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

</style>