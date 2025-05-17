<template>
  <!--  <a-form-->
  <!--    :model="topicQuery"-->
  <!--    layout="horizontal"-->
  <!--    ref="formRef"-->
  <!--    label-align="left"-->
  <!--    :label-col-props="{ span: 3, offset: 0 }"-->
  <!--    :wrapper-col-props="{ span: 21, offset: 0 }"-->
  <!--  >-->
  <!--    <a-row :gutter="24">-->
  <!--      <a-col :span="6">-->
  <!--        <a-form-item field="title" label="标题">-->
  <!--          <a-input v-model="topicQuery.title" placeholder="请输入题目标题..." />-->
  <!--        </a-form-item>-->
  <!--      </a-col>-->
  <!--      <a-col :span="6">-->
  <!--        <a-form-item field="tag" label="标签">-->
  <!--          <a-input-tag-->
  <!--            v-model="topicQuery.tags"-->
  <!--            placeholder="请输入题目标签..."-->
  <!--            @change="loadTopicData"-->
  <!--            allow-clear-->
  <!--          />-->
  <!--        </a-form-item>-->
  <!--      </a-col>-->
  <!--      <a-col :span="2">-->
  <!--        <a-button type="primary" @click="searchHandle">搜索</a-button>-->
  <!--      </a-col>-->
  <!--    </a-row>-->
  <!--  </a-form>-->

  <a-table
    :columns="columns"
    :data="topics"
    page-position="bottom"
    :pagination="paginationConfig"
    @page-change="pageChangeHandle"
    @page-size-change="pageSizeChangeHandle"
    :bordered="borderedConfig"
  >
    <template #tags="{ record: topic }">
      <a-space>
        <a-tag
          v-for="(item, idx) in JSON.parse(topic.tags)"
          :key="idx"
          :color="colors[idx]"
          bordered
          >{{ item }}
        </a-tag>
      </a-space>
    </template>

    <template #optional="{ record: topic }">
      <a-space>
        <a-button status="normal" @click="editClickHandle(topic.id)"
          >编辑
        </a-button>
        <a-button
          status="danger"
          @click="
            $modal.open({
              title: '温馨提示',
              content: `确定要删除《${topic.title}》嘛？`,
              okButtonProps: {
                status: 'danger',
              },
              onOk: (e) => deleteTopicHandle(e, topic.id),
            })
          "
          >删除
        </a-button>
      </a-space>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, getCurrentInstance } from "vue";
import { TopicControllerService, TopicQueryRequest } from "../../generated";
import { Message, PaginationProps, TableBorder } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

const router = useRouter();

const colors = [
  "green",
  "cyan",
  "orange",
  "gold",
  "lime",
  "blue",
  "arcoblue",
  "purple",
  "pinkpurple",
  "magenta",
  "gray",
];
const columns = [
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
    slotName: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

const topics = ref([]);
const paginationConfig = ref<boolean | PaginationProps>({
  total: 0,
  current: 1,
  pageSize: 10,
  defaultPageSize: 10,
  showTotal: true,
  showPageSize: true,
});
const borderedConfig = ref<boolean | TableBorder>({
  wrapper: true,
  cell: true,
  headerCell: true,
  bodyCell: true,
});
const topicQuery = ref({
  current: 1,
  pageSize: 10,
  title: "",
  tags: [],
});

onBeforeMount(() => {
  // 加载数据
  loadTopicData();
});
const loadTopicData = async () => {
  const res = await TopicControllerService.listTopicByPageUsingPost(
    topicQuery.value
  );
  if (res.code === 0) {
    // 判空处理 重要
    topics.value = res.data?.records;

    paginationConfig.value.total = parseInt(res.data.total);
    paginationConfig.value.current = parseInt(res.data.current);
    paginationConfig.value.pageSize = parseInt(res.data.size);
  }
};

const pageChangeHandle = (curPage: number) => {
  paginationConfig.value.current = curPage;
  topicQuery.value.current = curPage;
  loadTopicData();
};
const pageSizeChangeHandle = (pageSize: number) => {
  paginationConfig.value.pageSize = pageSize;
  topicQuery.value.pageSize = pageSize;
  loadTopicData();
};

const editClickHandle = (id: number) => {
  router.push(`/topic/update/${id}`);
};
const deleteTopicHandle = async (e?: Event, topicId?: number) => {
  const res = await TopicControllerService.deleteTopicUsingPost({
    id: topicId,
  });
  if (res.code === 0) {
    Message.success("删除成功！");
  }
  await loadTopicData();
};
</script>
