<template>

    <div class="form-container">
    <form>
      <div class="form-row">
        <div class="form-row">
          <label for="username"><i class="fas fa-user" style='color: rgb(65, 127, 202)'></i> &nbsp;&nbsp;&nbsp;Username</label>
          <input type="text" id="username" name="username" v-model="input.username" placeholder="" />
        </div>
        <br>
        <div class="form-row">
          <label for="password"><i class="fas fa-lock" style='color: rgb(65, 127, 202)'></i>&nbsp;&nbsp;&nbsp;Password</label>
          <input type="password" id="password" name="password" v-model="input.password" placeholder="" />
        </div>
        <br><br>
        <input type="submit" v-on:click="login()" value="Login">
      </div>
    </form>
    </div>
  </template>

<script>
    export default {
        name: 'Login',
        data() {
            return {
                input: {
                    username: "",
                    password: ""
                }
            }
        },
        methods: {
            login() {
                if(this.input.username != "" && this.input.password != "") {
                    // This should actually be an api call not a check against this.$parent.mockAccount
                    if(this.input.username == this.$parent.mockAccount.username && this.input.password == this.$parent.mockAccount.password) {
                        this.$emit("authenticated", true);
                        this.$router.replace({ name: "Secure" });
                    } else {
                        console.log("The username and / or password is incorrect");
                    }
                } else {
                    console.log("A username and password must be present");
                }
            }
        }
    }
</script>
<style scoped>

body {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
}
#login .form-inputs {
    padding-bottom: 10px;
    width: 100%;
}

#login .form-inputs label {
    padding-right: 10px;
}

form {
    margin-top: 100px;
    background: linear-gradient(to bottom right, #fff);
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    text-align: left;
    width: 30%;
}

label {
    display: block;
    margin-bottom: 10px;
    font-weight: bold;
    color: #676767;
    width: 100%;
}
.form-row {
    margin-bottom: 20px;
    width: 100%;
}
.form-row label {
    display: block;
    margin-bottom: 5px;
    font-size: 20px;
}

input[type="submit"] {
    /* Updated styles for the login button */
    padding: 10px 0; /* Remove fixed padding and use vertical padding */
    width: 100%; /* Set the button width to 100% of its parent container */
    background: linear-gradient(to bottom right, #5DADE2);
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    font-size: 20px;
  }

  input[type="text"],
  input[type="password"] {
    width: 100%;
    height: 40px;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 22px;
  }

  .container {
  display: flex;
  /* Create a row layout */
  height: 100vh;
  /* Set the container height to 100% of the viewport height */
  margin: 0;
  /* Remove default margin */
  padding: 0;
  /* Remove default padding */
}



.form-container {
  flex: 1;
  /* Take up the right portion of the screen */
  display: flex;
  /* Create a column layout for the form container */
  justify-content: center;
  /* Center horizontally */
  align-items: center;
  /* Center vertically */
}

</style>