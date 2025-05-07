<template>
  <div>
    <Editor :value="props.value" :plugins="plugins" @change="handleChange" />
  </div>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import math from "@bytemd/plugin-math";
import { Editor, Viewer } from "@bytemd/vue-next";
import { defineProps, defineEmits } from "vue";

const emit = defineEmits(["update:value"]);
const props = defineProps({
  value: {
    type: String,
    default: "// Start edit...\n",
  },
});

const plugins = [
  gfm(),
  highlight(),
  math(),
  // Add more plugins here
];

const handleChange = (v: string) => {
  emit("update:value", v);
};
</script>

<style scoped></style>
