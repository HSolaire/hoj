<template>
  <a-card
    :body-style="{
      borderRadius: '40',
      padding: 0,
      minHeight: '600px',
    }"
  >
    <a-tabs default-active-key="1" size="mini" :animation="true" type="text">
      <a-tab-pane
        key="1"
        title="题目描述"
        style="padding: 0 20px; box-sizing: border-box"
      >
        <template #title>
          <icon-questionnaire style="vertical-align: -2px" />
          题目描述
        </template>
        <slot name="describe"> </slot>
      </a-tab-pane>
      <a-tab-pane key="2" title="题解">
        <template #title>
          <icon-code-square />
          题解
        </template>
        <slot name="resolve" />
      </a-tab-pane>
      <a-tab-pane key="3" title="提交记录">
        <template #title>
          <icon-history />
          提交记录
        </template>
        <slot name="record" />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script setup lang="ts">
import { IconCodeSquare, IconHistory } from "@arco-design/web-vue/es/icon";
import MdEditor from "@/components/MdEditor.vue";
import IconQuestionnaire from "@arco-iconbox/vue-hsola-arco-icon/src/IconQuestionnaire/index.vue";
</script>

<style scoped></style>
