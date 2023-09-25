<template>
  <div id="app">
    <div id="nav">
    <nav class="container navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="">RIC SG</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#testbar" aria-controls="testbar" aria-expanded="false" aria-label="Toggle navigation" @click="toggle" >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbar1" style="display:none;">
        <div class="navbar-nav mr-auto">
          <router-link to="/" class="nav-item nav-link">Home</router-link>
          <router-link to="/registration" class="nav-item nav-link">Register Team</router-link>
          <router-link v-if="authenticated && userRoles.includes('participant')" to="/contestantProfile" class="nav-item nav-link">Team Profile</router-link>
          <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageContestant" class="nav-item nav-link">Manage Contestant</router-link>
          <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageGroup" class="nav-item nav-link">Manage Group</router-link>
          <router-link v-if="authenticated && userRoles.includes('judge')" to="/score" class="nav-item nav-link">Score</router-link>
          <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageTeam" class="nav-item nav-link">Manage Team</router-link>
          <router-link v-if="authenticated && userRoles.includes('admin')" to="/manageMetric" class="nav-item nav-link">Manage Metric</router-link>
          <router-link v-if="authenticated" to="/login" v-on:click.native="logout()" replace class="nav-item nav-link">Logout</router-link>
          <router-link v-else to="/login" v-on:click.native="login()" class="nav-item nav-link">Login</router-link>
        </div>
      </div>
    </nav>
    <router-view @authenticated="setAuthenticated" />
  </div>
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
    toggle() {
      console.log("test");
      var x = document.getElementById("navbar1");
      console.log(x);
      if (x.style.display === "none") {
        x.style.display = "block";
      } else {
        x.style.display = "none";
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
  /* padding: 20px; */
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

#nav {
  /* padding: 30px; */
  background: #f8f9fa;
  text-align: center;
}

#navbar {
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: whitesmoke;
  background: #5DADE2;
  border-radius: .5rem;
}
</style>
