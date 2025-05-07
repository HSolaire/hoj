<template>
  <div>这是题目增加页面</div>
  <input type="text" v-model="form.loginUser.userName" />
  {{ form.loginUser.userName }}
</template>

<script setup lang="ts">
import { ref } from "vue";

const form = ref({
  loginUser: {
    userName: "",
  },
});
</script>
