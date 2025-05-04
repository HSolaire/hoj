<template>
  <md-editor :value="mdValue" @md-edit-change="mdEditChangeHandle" />
</template>

<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import { ref } from "vue";

const mdValue = ref("");
const mdEditChangeHandle = (v: string) => {
  mdValue.value = v;
};
</script>

<style scoped></style>
