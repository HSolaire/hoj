<template>
  <div id="login-page">
    <a-form
      :model="form"
      label-align="left"
      :style="{ width: '500px' }"
      @submit="handleSubmit"
    >
      <div class="form-title">登录页</div>
      <a-form-item field="name" label="用户名">
        <a-input v-model="form.userAccount" placeholder="请输入用户名" />
      </a-form-item>
      <a-form-item field="post" tooltip="密码长度不小于6位" label="密码">
        <a-input
          v-model="form.userPassword"
          type="password"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item field="isRead">
        <a-checkbox v-model="form.rememberMe">记住密码</a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import store from "@/store";
import { Message } from "@arco-design/web-vue";
import router from "@/router";

interface FormData {
  userAccount: string;
  userPassword: string;
  rememberMe: boolean;
}

const form = reactive<FormData>({
  userAccount: "hsola",
  userPassword: "12345678",
  rememberMe: false,
});

const handleSubmit = (data: Event) => {
  store
    .dispatch("user/loginHandle", form)
    .then((res) => {
      Message.info(JSON.stringify(res));
      router.push("/home");
    })
    .catch((res) => {
      Message.error(res);
    });
};
</script>

<style scoped>
#login-page {
  height: 100vh;
  display: flex;
  justify-self: center;
  align-items: center;
}

.form-title {
  text-align: center;
  font-size: 1.5rem;
  padding-bottom: 25px;
}
</style>
