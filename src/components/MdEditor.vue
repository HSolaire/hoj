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

const emit = defineEmits(["mdEditChange"]);
const props = defineProps({
  value: String,
});

const plugins = [
  gfm(),
  highlight(),
  math(),
  // Add more plugins here
];

const handleChange = (v: string) => {
  emit("mdEditChange", v);
};
</script>

<style scoped></style>
