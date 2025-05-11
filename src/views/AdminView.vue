<template>
  <a-table
    :columns="columns"
    :data="topics"
    page-position="bottom"
    :pagination="paginationConfig"
    @page-change="pageChangeHandle"
    @page-size-change="pageSizeChangeHandle"
    :bordered="borderedConfig"
  >
    <template #optional="{ record: topic }">
      <a-space>
        <a-button
          status="normal"
          @click="
            $modal.info({
              title: 'Name',
              content: topic.content,
              okText: '确定',
              cancelText: '取消',
            })
          "
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
});

onBeforeMount(() => {
  console.log(getCurrentInstance()?.proxy?.$modal);
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
