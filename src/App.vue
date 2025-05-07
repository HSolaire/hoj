<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
#app {
}
</style>

<script setup lang="ts">
import { onMounted } from "vue";
import store from "@/store";

const doInit = () => {
  console.log("欢迎！");
};

onMounted(() => {
  doInit();
});
</script>
