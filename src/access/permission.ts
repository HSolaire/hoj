import router from "@/router";
import store from "@/store";
import roleCheck from "@/access/accessControl";
import { Message } from "@arco-design/web-vue";

router.beforeEach(async (to, from, next) => {
  // 1. 加载用户
  if (!store.state.user.isLoginUserLoaded) {
    await store.dispatch("user/resetLoginUser");
  }

  if (store.state.user.isLoginUserLoaded) {
    // 2. 有用户
    if (!roleCheck(store.state.user.loginUser, to.meta.role as string)) {
      Message.error("无权限！");
      next("/noAuth");
    } else {
      next();
    }
  } else {
    // 3. 无用户
    if ((to.meta.role || "guest") === "guest") {
      next();
    } else {
      Message.error("请先登录！");
      next(`/login?redirect=${to.fullPath}`);
    }
  }
});

// 设置标题，不过一般会卸载meta里
router.beforeResolve((to, from, next) => {
  window.document.title = to.name as string;
  next();
});
