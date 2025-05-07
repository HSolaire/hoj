<template>
  <md-editor v-show="false" v-model:value="mdValue" />
  <code-editor v-model:value="codeValue" />
  {{ codeValue }}
</template>

<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";

const mdValue = ref("");
const codeValue = ref("");
</script>

<style scoped></style>
