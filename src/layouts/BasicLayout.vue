<template>
  <div id="layout">
    <a-layout style="min-height: inherit">
      <a-layout-header class="header">
        <global-header />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer"> Create by HSolaire</a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
import GlobalHeader from "@/components/GlobalHeader.vue";

export default {
  components: { GlobalHeader: GlobalHeader },
};
</script>

<style scoped>
#layout {
  min-height: 100vh;
}

#layout .header {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

#layout .content {
  padding: 24px 72px;
  background-color: #ccc;
}

#layout .footer {
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  padding: 10px;
}
</style>
