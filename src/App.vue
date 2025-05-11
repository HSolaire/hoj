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

const debounce = (callback: (...args: any[]) => void, delay: number) => {
  let tid: any;
  return function (...args: any[]) {
    const ctx = self;
    tid && clearTimeout(tid);
    tid = setTimeout(() => {
      callback.apply(ctx, args);
    }, delay);
  };
};

const _ = (window as any).ResizeObserver;
(window as any).ResizeObserver = class ResizeObserver extends _ {
  constructor(callback: (...args: any[]) => void) {
    callback = debounce(callback, 20);
    super(callback);
  }
};

const doInit = () => {
  console.log("欢迎！");
};

onMounted(() => {
  doInit();
});
</script>
