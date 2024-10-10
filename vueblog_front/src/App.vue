<template>
  <div id="app">
<img src="./assets/logo2.png" id="logo"> 
    <router-view :key="$route.fullPath"></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #160c17;
  margin-top: 10px;
  text-align: middle;
  background-color: rgb(255, 255, 255);
 

  background-size: cover;
}

#logo{
  width: 100%;
  height: 100px;
  object-fit: cover;
}


</style>
