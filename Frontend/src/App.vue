<template>
  <div id="app">
    <div id="nav" class="top-menu">
      <router-link to="/" class="menu-item">Home</router-link>
      <router-link to="/registration" class="menu-item">Register</router-link>
      <router-link v-if="authenticated" to="/upload" class="menu-item">Upload</router-link>
      <router-link to="/account" class="menu-item">Manage Contestant</router-link>
      <router-link to="/secure" class="menu-item">Manage Team</router-link>
      <!--<router-link to="/qualify" class="menu-item">TEST API</router-link>-->
      <router-link v-if="authenticated" to="/login" v-on:click.native="logout()" replace class="menu-item">Logout</router-link>
      <router-link v-else to="/login" class="menu-item">Login</router-link>
    </div>
    <router-view @authenticated="setAuthenticated" />
  </div>
</template>
<script>
    export default {
        name: 'App',
        data() {
            return {
                authenticated: false,
                // this is only for testing purposes no actual app will have this
                // an api call will validate user credentials
                mockAccount: {
                    username: "km",
                    password: "km"
                }
            }
        },
        methods: {
            setAuthenticated(status) {
                this.authenticated = status;
            },
            logout() {
                this.authenticated = false;
            }
        }
    }
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

#nav {
  /* Remove the width property, so it takes 100% width by default */
  display: flex;
  justify-content: center;
  padding: 15px 0;
}

.menu-item {
  font-weight: bold;
  color: #2c3e50;
  padding: 0 20px;
  text-decoration: none;
  transition: color 0.3s;
}

.menu-item:not(:first-child) {
  border-left: 1px solid #dcdcdc;
}

.menu-item.router-link-exact-active {
  color: rgb(65, 127, 202)
}

.top-menu {
  /* Remove the width property, so it takes 100% width by default */
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  font-size: larger;
  /* Add a new property to make the menu span the full width of the page */
  width: 100%;
}

/* Hover effect for menu items */
.menu-item:hover {
  color: #120d92;
}
</style>