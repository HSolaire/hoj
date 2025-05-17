<template>
  <div>
    <Editor
      v-if="!props.viewFlag"
      style="width: 600px"
      :value="props.value"
      :plugins="plugins"
      @change="handleChange"
    />
    <Viewer v-else :value="props.value" />
  </div>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import math from "@bytemd/plugin-math";
import { Editor, Viewer } from "@bytemd/vue-next";
import { defineProps, defineEmits, ref } from "vue";

const emit = defineEmits(["update:value", "change"]);
const props = defineProps({
  value: {
    type: String,
    default: "// 请编辑 ...\n",
  },
  viewFlag: {
    type: Boolean,
    default: false,
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
