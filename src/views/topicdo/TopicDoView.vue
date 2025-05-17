<template>
  <a-split
    ref="splictRef"
    :style="{
      width: '100%',
      height: '600px',
      border: '1px solid var(--color-border)',
    }"
    v-model:size="sizeLR"
  >
    <template #first>
      <a-typography-paragraph style="min-height: 100%">
        <detail-nav>
          <template #describe>
            <md-editor :view-flag="true" :value="topic.content" />
          </template>
          <template #resolve>
            <resolve-index />
          </template>
          <template #record>
            <record-index />
          </template>
        </detail-nav>
      </a-typography-paragraph>
    </template>
    <template #second>
      <div>
        <a-split
          direction="vertical"
          :style="{ height: '600px' }"
          :default-size="0.8"
        >
          <template #first>
            <a-typography-paragraph>
              <a-tabs default-active-key="1" size="mini" type="text">
                <a-tab-pane
                  key="1"
                  title="代码"
                  style="padding: 0 5px; box-sizing: border-box"
                >
                  <template #title>
                    <icon-code />
                    代码
                  </template>
                  <a-space>
                    <a-select
                      :trigger-props="{ autoFitPopupMinWidth: true }"
                      size="mini"
                      v-model="codeLanguage"
                    >
                      <a-option>java</a-option>
                      <a-option>python</a-option>
                      <a-option>golang</a-option>
                      <a-option>cpp</a-option>
                    </a-select>
                  </a-space>
                  <a-divider :margin="5" />
                  <code-editor :language="codeLanguage" theme="vs-light" />
                </a-tab-pane>
              </a-tabs>
            </a-typography-paragraph>
          </template>
          <template #second>
            <a-typography-paragraph>Bottom</a-typography-paragraph>
          </template>
        </a-split>
      </div>
    </template>
  </a-split>
</template>

<script setup lang="ts">
// import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import {
  IconHistory,
  IconCodeSquare,
  IconCode,
} from "@arco-design/web-vue/es/icon";

import IconQuestionnaire from "@arco-iconbox/vue-hsola-arco-icon/src/IconQuestionnaire/index.vue";
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { onBeforeMount, onMounted, reactive, ref } from "vue";
import { TopicControllerService, TopicVO } from "../../../generated";
import { useRoute } from "vue-router";
import DetailNavView from "@/views/topicdo/detail/DetailNav.vue";
import DetailNav from "@/views/topicdo/detail/DetailNav.vue";
import RecordIndex from "@/views/topicdo/detail/record/RecordIndex.vue";
import ResolveIndex from "@/views/topicdo/detail/resolve/ResolveIndex.vue";

const route = useRoute();
let topic = reactive<TopicVO>({});
const sizeLR = ref(0.5);

const codeLanguage = ref<string>("java");

const loadTopic = async () => {
  console.log(route.params.id);
  const res = await TopicControllerService.getTopicVoByIdUsingGet(
    route.params.id as number | any
  );
  console.log(res);
  if (res.code === 0) {
    // describe = reactive(res.data as TopicVO);l
    const data = res.data;
    topic.content = data?.content;
    console.log(topic);
  }
};

// const movingHandle = (e: Event) => {};

onMounted(async () => {
  await loadTopic();
});
</script>

<style scoped>
::v-deep(.arco-tabs-nav) {
  position: sticky !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  background-color: #fff;
}

::v-deep(.arco-tabs-content) {
  margin-top: 0px;
  padding-top: 5px;
}

::v-deep(.arco-typography) {
  margin-bottom: 0;
}
</style>
