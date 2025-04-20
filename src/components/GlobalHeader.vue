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
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="title-bar">
              <img class="logo" src="../assets/logo.png" alt="logo" />
              <div class="title">航 OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item :key="item.path" v-for="item in visibleRoutes">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col class="user" flex="100px"
        >{{ store.state.user.loginUser?.userName ?? "未登录" }}
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import { ROLE_ENUM } from "@/access/roleEnum";
import roleCheck from "@/access/accessControl";

setTimeout(() => {
  store.dispatch("user/getLoginUser", {
    userName: "航",
    userRole: [ROLE_ENUM.USER, ROLE_ENUM.GUEST, ROLE_ENUM.ADMIN],
  });
}, 3000);

const router = useRouter();
const store = useStore();

const visibleRoutes = computed(() => {
  return routes.filter((item, idx) => {
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

const selectKeys = ref(["/"]);
// 默认高亮 fixme 需要改成 afterEach，目前不清楚原因
router.beforeEach((to) => {
  selectKeys.value = [to.path];
  console.log(to);
});

// 菜单绑定跳转
const doMenuClick = (key: string) => {
  router.push({
    path: key,
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
  line-height: 64px;
}
</style>
