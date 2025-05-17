<template>
  <a-form
    :model="topicQuery"
    layout="horizontal"
    ref="formRef"
    label-align="left"
    :label-col-props="{ span: 3, offset: 0 }"
    :wrapper-col-props="{ span: 21, offset: 0 }"
  >
    <a-row :gutter="24">
      <a-col :span="6">
        <a-form-item field="title" label="标题">
          <a-input v-model="topicQuery.title" placeholder="请输入题目标题..." />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item field="tag" label="标签">
          <a-input-tag
            v-model="topicQuery.tags"
            placeholder="请输入题目标签..."
            @change="loadTopicData"
            allow-clear
          />
        </a-form-item>
      </a-col>
      <a-col :span="2">
        <a-button type="primary" @click="searchHandle">搜索</a-button>
      </a-col>
    </a-row>
  </a-form>

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
          v-for="(item, idx) in topic.tags"
          :key="idx"
          :color="colors[idx]"
          bordered
          >{{ item }}
        </a-tag>
      </a-space>
    </template>

    <template #optional="{ record: topic }">
      <a-button type="primary" @click="doTopic(topic.id)">做题</a-button>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, watch } from "vue";
import {
  TopicControllerService,
  TopicQueryRequest,
  TopicVO,
} from "../../generated";
import { Message, PaginationProps, TableBorder } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();

const formRef = ref(null);
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
  // {
  //   title: "序号",
  //   render: (data: any) => data.rowIndex + 1,
  // },
  {
    title: "题目ID",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    dataIndex: "tags",
    slotName: "tags",
  },
  {
    title: "通过率",
    align: "center",
    render: (data: any) => {
      const record: TopicVO = data.record;
      if (!record.totalCount) {
        return "0% (0/0)";
      }
      const accPer = (((record.acceptedCount as number) * 100) /
        record.totalCount) as number;
      return `${accPer}% (${record.acceptedCount}/${record.totalCount})`;
    },
  },
  {
    title: "创建时间",
    render: (data: any) => {
      return moment((data.record as TopicVO).createTime).format(
        "YYYY-MM-DD HH:mm:ss"
      );
    },
  },
  {
    title: "操作",
    align: "center",
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

watch(
  () => topicQuery.value.tags,
  () => searchHandle()
);

onBeforeMount(() => {
  // 加载数据
  loadTopicData();
});
const loadTopicData = async () => {
  const res = await TopicControllerService.listTopicVoByPageUsingPost(
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

const searchHandle = () => {
  resetPageConfig();
  loadTopicData();
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

const resetPageConfig = () => {
  topicQuery.value.current = 1;
  topicQuery.value.pageSize = 10;
  paginationConfig.value.current = 10;
  paginationConfig.value.pageSize = 10;
};

const doTopic = (id: number) => {
  router.push(`/topic/do/${id}`);
};
</script>
