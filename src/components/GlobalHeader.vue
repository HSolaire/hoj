<template>
  <div id="header">
    <a-row>
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectKeys"
          @menu-item-click="doMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px', cursor: 'pointer' }"
            disabled
          >
            <div class="title-bar">
              <img class="logo" src="../assets/sola.png" alt="logo" />
              <div class="title">航 OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item :key="item.path" v-for="item in visibleRoutes">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>

      <a-col class="user" flex="100px">
        <a-button type="primary" icon="plus" size="small" @click="getLoginUser">
          测试获取用户接口
        </a-button>
      </a-col>

      <a-col
        v-if="(store.state.user.loginUser?.userName || '') === ''"
        class="user"
        flex="100px"
      >
        <router-link to="/login">登录</router-link>
      </a-col>
      <a-col v-else class="user" flex="100px">
        {{ store.state.user.loginUser?.userName }}
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { menuRoutes } from "@/router/routes";
import { useRouter, useRoute } from "vue-router";
import { computed, onBeforeUpdate, onMounted, onUnmounted, ref } from "vue";
import { useStore } from "vuex";
import roleCheck from "@/access/accessControl";
import { UserControllerService } from "@/../generated";
import { Message } from "@arco-design/web-vue";
import * as path from "node:path";

const router = useRouter();
const route = useRoute();
const store = useStore();

const visibleRoutes = computed(() => {
  return menuRoutes[0].children?.filter((item, idx) => {
    return (
      (item.meta?.showInMenu ?? true) &&
      roleCheck(store.state.user.loginUser, item.meta?.role as string)
    );
  });
});

const selectKeys = ref(["/describe/scan"]);

// todo 重要 仅刷新触发
router.isReady().then(() => {
  selectKeys.value = [route.matched[route.matched.length - 1].path];
});

router.afterEach((to) => {
  selectKeys.value = [to.matched[to.matched.length - 1].path];
});

// 菜单绑定跳转
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const getLoginUser = () => {
  UserControllerService.getLoginUserUsingGet().then((user) => {
    Message.info(JSON.stringify(user));
  });
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
  justify-content: center;
}

.title-bar .title {
  color: #444;
  margin-left: 15px;
}

.title-bar img {
  height: 48px;
}

.user {
  text-align: center;
  line-height: 75px;
}
</style>
