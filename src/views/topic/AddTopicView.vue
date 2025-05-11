<template>
  <div id="add-topic">
    <h2>题目新增</h2>
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

      <!--  todo field 用来校验规则    -->
      <a-form-item field="answer" label="题目答案">
        <md-editor
          v-model:value="form.answer"
          @change="formRef.validateField('answer')"
        />
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" fill>
          <a-form-item field="judgeConfig.timeLimit" label="时间限制">
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

      <a-form-item
        field="judgeCase"
        label="判题示例"
        :content-flex="false"
        :merge-props="false"
        style="margin-bottom: 10px"
      >
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
      </a-form-item>
      <a-form-item>
        <a-button @click="handleAdd" type="primary" status="success"
          >新增示例
        </a-button>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { FormInstance, Message } from "@arco-design/web-vue";
import { TopicControllerService } from "../../../generated";
import { looseIndexOf } from "@vue/shared";

const formRef = ref<FormInstance>();

const form = ref({
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

const rules = ref({
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
  "judgeConfig.memoryLimit": [{ min: 0, message: "内存限制不可为负值！" }],
  "judgeConfig.timeLimit": [{ min: 0, message: "时间限制不可未负值！" }],
  "judgeConfig.stackLimit": [{ min: 0, message: "堆栈大小不可为负值！" }],
  judgeCase: [
    {
      validator: (value, cb) => {
        return new Promise((resolve) => {
          const errJudgeCaseInputList = {};
          console.log(formRef.value);
          value.forEach((item, idx) => {
            if (item.input === "") {
              errJudgeCaseInputList[`judgeCase[${idx}].input`] = {
                status: "error",
                message: "输入示例不可为空!",
              };
            }
            if (item.output === "") {
              errJudgeCaseInputList[`judgeCase[${idx}].output`] = {
                status: "error",
                message: "输出示例不可为空!",
              };
            }
          });
          formRef.value.setFields({ ...errJudgeCaseInputList });
          console.log(formRef.value);
          resolve();
        });
      },
    },
  ],
});

const doSubmit = async () => {
  const vaild = await formRef.value?.validate();
  if (vaild !== undefined) {
    Message.warning("含有必填项未填！");
    return;
  }
  const res = await TopicControllerService.addTopicUsingPost(
    form.value as TopicControllerService
  );
  if (res.code === 0) {
    Message.info("新增题目成功！");
  }
};

const handleAdd = () => {
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
  form.value.judgeCase.splice(index, 1);
};
</script>

<style scoped>
#add-topic {
}
</style>
