<template>
  <div id="app">
    <nav class="top-menu">
      <router-link to="/" class="menu-item">Home</router-link>
      <router-link to="/registration" class="menu-item"
        >Register Team</router-link
      >
      <router-link
        v-if="
          authenticated &&
          (userRoles.includes('judge') || userRoles.includes('admin'))
        "
        to="/mainScore"
        class="menu-item"
        >Score</router-link
      >
      <router-link
        v-if="
          authenticated &&
          userRoles.includes('participant') &&
          !userRoles.includes('judge') &&
          !userRoles.includes('admin')
        "
        to="/contestantProfile"
        class="menu-item"
        >Team Profile</router-link
      >
      <router-link
        v-if="authenticated && userRoles.includes('admin')"
        to="/upload"
        class="menu-item"
        >Admin Upload</router-link
      >
      <router-link
        v-if="authenticated && userRoles.includes('admin')"
        to="/manageContestant"
        class="menu-item"
        >Manage User</router-link
      >
      <router-link
        v-if="authenticated && userRoles.includes('admin')"
        to="/manageTeam"
        class="menu-item"
        >Manage Team</router-link
      >
      <router-link
        v-if="authenticated && userRoles.includes('admin')"
        to="/manageGroup"
        class="menu-item"
        >Manage Group</router-link
      >

      <router-link
        v-if="authenticated && userRoles.includes('admin')"
        to="/manageMetric"
        class="menu-item"
        >Manage Metric</router-link
      >
      <router-link
        v-if="authenticated"
        to="/login"
        v-on:click.native="logout()"
        replace
        class="menu-item"
        >Logout</router-link
      >

      <router-link
        v-else
        to="/login"
        v-on:click.native="login()"
        class="menu-item"
        >Login</router-link
      >
    </nav>
    <div>
      <!-- Add a row for the welcome message -->
      <div v-if="authenticated" class="welcome-message">
        Hi,
        <span class="bold-text">{{ userName }}</span> &nbsp;&nbsp;&nbsp;Role:
        <span class="bold-text">{{ capitalizeFirstLetter(roleName[0]) }}</span>
      </div>
    </div>
    <router-view @authenticated="setAuthenticated" />
  </div>
</template>

<script>
import Vue from "vue";

export default {
  name: "App",
  data() {
    return {
      authenticated: false,
      userRoles: [],
      userName: "",
      roleName: [],
    };
  },
  methods: {
    filterRoles() {
      // Define the allowed roles
      const allowedRoles = ["judge", "admin", "participant"];

      // Filter the userRoles array to keep only the allowed roles
      this.roleName = this.userRoles.filter((role) =>
        allowedRoles.includes(role)
      );
    },
    capitalizeFirstLetter(str) {
      if (!str) return ""; // Handle empty or undefined string
      return str.charAt(0).toUpperCase() + str.slice(1);
    },

    setAuthenticated(status) {
      this.authenticated = status;
      if (this.authenticated) {
        this.checkAuthentication();
        this.filterRoles();
      }
    },
    checkAuthentication() {
      if (this.authenticated) {
        const tokenData = Vue.$keycloak.tokenParsed;
        this.userRoles = tokenData.realm_access.roles;
        const userName = tokenData.preferred_username;

        // Assign the user's name to a variable or use it as needed
        this.userName = userName;
      }
    },
    logout() {
      this.authenticated = false;
      Vue.$keycloak.logout();
    },
    login() {
      this.authenticated = true;
      Vue.$keycloak.login();
    },
  },
  created() {
    this.checkAuthentication();
  },
};
</script>

<style scoped>
.bold-text {
  font-weight: bold; /* Make the text bold */
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  padding: 20px;
}

.top-menu {
  display: flex;
  justify-content: center;
  padding: 15px 0;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  font-size: 16px;
  margin-bottom: 15px;
}

.menu-item {
  font-weight: bold;
  color: #2c3e50;
  padding: 10px 20px;
  text-decoration: none;
  transition: color 0.3s;
}

.menu-item:not(:first-child) {
  border-left: 1px solid #dcdcdc;
}

.menu-item.router-link-exact-active {
  color: rgb(65, 127, 202);
}

.menu-item:hover {
  color: #120d92;
  background-color: #f0f0f0;
}

/* Responsive styles for smaller screens */
@media screen and (max-width: 768px) {
  .top-menu {
    flex-wrap: wrap;
  }
  .menu-item:not(:first-child) {
    border-left: none;
    border-top: 1px solid #dcdcdc;
  }
}

.welcome-message {
  padding: 5x; /* Padding around the message */
  text-align: right; /* Center-align the text */
  font-size: 15px; /* Font size */
  color: #002c72; /* Text color */
  margin-right: 10px;
  margin-bottom: 10px;
  margin-right: 10px;
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
