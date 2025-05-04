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
        <a-button
          type="primary"
          shape="circle"
          icon="plus"
          size="small"
          @click="getLoginUser"
        >
          测试获取用户
        </a-button>
      </a-col>

      <a-col
        v-if="store.state.user.loginUser?.userName === undefined"
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
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import roleCheck from "@/access/accessControl";
import { UserControllerService } from "../../generated";
import { Message } from "@arco-design/web-vue";

const router = useRouter();
const store = useStore();

const visibleRoutes = computed(() => {
  return menuRoutes[0].children?.filter((item, idx) => {
    return (
      (item.meta?.showInMenu ?? true) &&
      roleCheck(store.state.user.loginUser, item.meta?.role as string)
    );
  });
});

// const visibleRoutes = routes.filter((item, idx) => {
//   return (
//     (item.meta?.showInMenu ?? true) &&
//     roleCheck(store.state.user.loginUser, item.meta?.role as string)
//   );
// });

const selectKeys = ref(["/home"]);
// 默认高亮 fixme 需要改成 afterEach，目前不清楚原因
router.beforeEach((to) => {
  selectKeys.value = [to.path];
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
    console.log(user);
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
