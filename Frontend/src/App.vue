<template>
  <div id="app">
    <nav class="top-menu">
      <router-link to="/" class="menu-item">Home</router-link>
      <router-link to="/registration" class="menu-item">Register Team</router-link>
      <router-link v-if="authenticated && userRoles.includes('participant')" to="/contestantProfile" class="menu-item">Team Profile</router-link>
      <router-link v-if="authenticated && userRoles.includes('admin')" to="/upload" class="menu-item">Upload</router-link>
      <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageContestant" class="menu-item">Manage Contestant</router-link>
      <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageGroup" class="menu-item">Manage Group</router-link>
      <router-link v-if="authenticated && userRoles.includes('judge')" to="/score" class="menu-item">Score IDC</router-link>
      <router-link v-if="authenticated && userRoles.includes('judge')" to="/scoreGA" class="menu-item">Score GA</router-link>
      <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageTeam" class="menu-item">Manage Team</router-link>
      <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageMetric" class="menu-item">Manage Metric</router-link>
      <router-link v-if="authenticated" to="/login" v-on:click.native="logout()" replace class="menu-item">Logout</router-link>

      <router-link v-else to="/login" v-on:click.native="login()" class="menu-item">Login</router-link>
    </nav>
    <router-view @authenticated="setAuthenticated" />
  </div>
</template>

<script>
import Vue from 'vue';

export default {
  name: 'App',
  data() {
    return {
      authenticated: false,
      userRoles: []
    };
  },
  methods: {
    setAuthenticated(status) {
      this.authenticated = status;
      if (this.authenticated) {
        this.checkAuthentication();
      }
    },
    checkAuthentication() {
      if (this.authenticated) {
        const tokenData = Vue.$keycloak.tokenParsed;
        this.userRoles = tokenData.realm_access.roles;
      }
    },
    logout() {
      this.authenticated = false;
      Vue.$keycloak.logout();
    },
    login() {
      this.authenticated = true;
      Vue.$keycloak.login();
    }
  },
  created() {
    this.checkAuthentication();
  }
};
</script>

<style scoped>
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
  margin-bottom: 70px;
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
</style>
