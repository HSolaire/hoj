<template>
  <div>
    <Editor
      style="width: 600px"
      :value="props.value"
      :plugins="plugins"
      @change="handleChange"
    />
  </div>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import math from "@bytemd/plugin-math";
import { Editor, Viewer } from "@bytemd/vue-next";
import { defineProps, defineEmits } from "vue";

const emit = defineEmits(["update:value", "change"]);
const props = defineProps({
  value: {
    type: String,
    default: "// 请编辑 ...\n",
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
  emit("change");
};
</script>

<style scoped></style>
