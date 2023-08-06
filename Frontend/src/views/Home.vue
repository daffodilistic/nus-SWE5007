<template>
  <div class="home">
    <div class="content">
      <h1>Welcome to <br><br>Robotics Innovation Challenge (RIC) <br><br>Singapore Portal</h1>
     <!-- <p class="construction-text">This page is still under construction</p>-->
    </div>
  </div>
</template>

<script>
import Vue from 'vue'

export default {
  data() {
    if (Vue.$keycloak.token.length>0){
      console.log(Vue.$keycloak.token)
      this.$emit("authenticated", true);
      // this.$router.replace({ name: "Secure" });
    }
    return {};
  },
  name: 'Home',
  methods: {
    // Your methods here
  },
};
</script>

<style>
/* Set the background image and adjust the background properties */
.home {
  background-image: url('../assets/BG.jpg'); /* Replace with your image path */
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  min-height: 100vh; /* Set the minimum height to cover the viewport */
  display: flex;
  justify-content: center;
  align-items: center;
}

/* Style the content inside the home section */
.content {
  text-align: center;
  color: #2c3e50;
  padding: 20px;
}

/* Style the logo image */
.logo {
  max-width: 200px;
  margin-bottom: 20px;
}

/* Style the construction text */
.construction-text {
  font-size: 18px;
  font-weight: bold;
}

/* Hide the bullet points in the HelloWorld component */
li {
  list-style: none;
}
</style>
