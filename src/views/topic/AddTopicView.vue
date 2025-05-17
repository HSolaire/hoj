<template>
  <div id="add-topic">
    <h2>{{ pageConfig.title }}</h2>
    <a-form
      :model="form"
      :style="{ width: '800px' }"
      label-align="left"
      :rules="rules"
      ref="formRef"
    >
      <a-form-item field="title" label="题目标题">
        <a-input v-model="form.title" placeholder="请输入标题..." />
      </a-form-item>

      <a-form-item field="tags" label="题目标签">
        <a-input-tag v-model="form.tags" placeholder="请输入标签..." />
      </a-form-item>

      <a-form-item field="level" label="题目难度">
        <a-select
          v-model="form.level"
          placeholder="Please select ..."
          allow-clear
        >
          <a-option :value="1">简单</a-option>
          <a-option :value="2">中等</a-option>
          <a-option :value="3">困难</a-option>
        </a-select>
      </a-form-item>

      <a-form-item field="content" label="题目内容">
        <md-editor
          v-model:value="form.content"
          @change="formRef.validateField('content')"
        />
      </a-form-item>

      <a-form-item field="answer" label="题目答案">
        <md-editor
          v-model:value="form.answer"
          @change="formRef.validateField('answer')"
        />
      </a-form-item>

      <!--   判题配置 todo 校验规则失败   -->
      <a-form-item
        label="判题配置"
        :content-flex="false"
        :merge-props="false"
        field="judgeConfig"
      >
        <a-space direction="vertical" fill>
          <a-form-item
            field="judgeConfig.timeLimit"
            label="时间限制"
            :rules="rules['judgeConfig.timeLimit']"
          >
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              :min="0"
              mode="button"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              :min="0"
              mode="button"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              :min="0"
              mode="button"
            />
          </a-form-item>
        </a-space>
      </a-form-item>
      <!--判题用例-->
      <a-form-item
        field="judgeCase"
        label="判题示例"
        :content-flex="false"
        :merge-props="false"
        style="margin-bottom: 10px"
      >
        <!--   每一对用例     -->
        <a-space
          direction="vertical"
          v-for="(item, index) of form.judgeCase"
          :key="index"
          fill
        >
          <a-form-item
            :field="`judgeCase[${index}].input`"
            :label="`输入示例-${index}`"
          >
            <a-input
              v-model="item.input"
              :placeholder="`请输入 输入示例-${index}`"
            />
          </a-form-item>
          <a-form-item
            :field="`judgeCase[${index}].output`"
            :label="`输出示例-${index}`"
          >
            <a-input
              v-model="item.output"
              :placeholder="`请输入 输出示例-${index}`"
            />
          </a-form-item>
          <a-button
            @click="handleDelete(index)"
            status="danger"
            style="margin-bottom: 10px"
          >
            删除示例
          </a-button>
        </a-space>
        <a-button @click="handleAdd" type="primary" status="success"
          >新增示例
        </a-button>
      </a-form-item>

      <!--  提交-->
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { FormInstance, Message } from "@arco-design/web-vue";
import { TopicControllerService } from "../../../generated";
import { useRoute } from "vue-router";

var route = useRoute();

const formRef = ref<FormInstance>();

const pageConfig = ref({
  title: "",
  updateFlag: false,
  updateId: -1,
});

onMounted(() => {
  if (route.path.includes("/update")) {
    pageConfig.value.updateFlag = true;
    pageConfig.value.title = "题目更新";
    pageConfig.value.updateId = route.params.id as number;
    loadData();
  } else {
    pageConfig.value.updateFlag = false;
    pageConfig.value.title = "题目新增";
  }
});

const form = ref({
  id: -1,
  title: "",
  content: "",
  answer: "",
  described: "",
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  level: 1,
  tags: [],
});

const baseRule = {
  title: [
    {
      required: true,
      message: "标题不可为空！",
    },
  ],
  content: [
    {
      required: true,
      message: "内容不可为空！",
    },
  ],
  answer: [
    {
      required: true,
      message: "答案不可为空！",
    },
  ],
  level: [
    {
      required: true,
      message: "难度不可为空！",
    },
  ],
  tags: [
    {
      required: true,
      message: "标签不可为空！",
    },
  ],
  "judgeConfig['memoryLimit']": [
    { min: 0, message: "内存限制不可为负值！" },
    {
      require: true,
      message: "内存限制不可为空值！",
      trigger: "blur",
    },
  ],
  "judgeConfig.timeLimit": [
    { min: 0, message: "时间限制不可未负值！" },
    {
      require: true,
      message: "内存限制不可为空值！",
    },
  ],
  "judgeConfig.stackLimit": [
    { min: 0, message: "堆栈大小不可为负值！" },
    {
      require: true,
      message: "内存限制不可为空值！",
    },
  ],
};

const loadJudgeCaseRules = () => {
  const errJudgeCaseInputList = {};
  form.value.judgeCase.forEach((_, idx) => {
    errJudgeCaseInputList[`judgeCase[${idx}].input`] = [
      {
        required: true,
        message: "输出示例不可为空！",
      },
    ];
    errJudgeCaseInputList[`judgeCase[${idx}].output`] = [
      {
        required: true,
        message: "输出示例不可为空！",
      },
    ];
  });
  return errJudgeCaseInputList;
};

const rules = ref({ ...baseRule, ...loadJudgeCaseRules() });

const doSubmit = async () => {
  const vaild = await formRef.value?.validate();

  if (vaild !== undefined) {
    Message.warning("含有必填项未填！");
    return;
  }
  if (pageConfig.value.updateFlag) {
    const res = await TopicControllerService.updateTopicUsingPost(
      form.value as TopicControllerService
    );
    if (res.code === 0) {
      Message.info("更新题目成功！");
    }
  } else {
    const res = await TopicControllerService.addTopicUsingPost(
      form.value as TopicControllerService
    );
    if (res.code === 0) {
      Message.info("新增题目成功！");
    }
  }
};

const handleAdd = () => {
  rules.value = { ...baseRule, ...loadJudgeCaseRules() };
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index: number) => {
  if (form.value.judgeCase.length <= 1) {
    Message.warning("至少保留一个判题用例！");
    return;
  }
  rules.value = { ...baseRule, ...loadJudgeCaseRules() };
  form.value.judgeCase.splice(index, 1);
};

const loadData = async () => {
  const res = await TopicControllerService.getTopicByIdUsingGet(
    route.params.id as any
  );
  if (res.code === 0) {
    const data = res.data;
    form.value = data as any;
    form.value.tags = JSON.parse(data?.tags as string);
    if (!data?.judgeCase) {
      console.log("aaa");
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(data?.judgeCase as string);
    }
    if (!data?.judgeConfig || data?.judgeConfig === "{}") {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      form.value.judgeConfig = JSON.parse(data?.judgeConfig as string);
    }
  }
};
</script>

<style scoped>
#add-topic {
}
</style>
