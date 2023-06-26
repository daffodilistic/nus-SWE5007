<template>
  <div class="home">
    <!--<img alt="Vue logo" src="../assets/logo.png">-->
    
    <HelloWorld msg="Welcome to Robotics Innovation Challenge (RIC) Singapore Portal"/>

    This page is still under construction
  </div>
</template>

<style>

</style>


<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'Home',
  components: {
    HelloWorld
  },
  computed: {
 BGImage() {
    return {
      backgroundImage: `url(${require('../assets/BG.jpg')})`
    };
  }
}
}
</script>
