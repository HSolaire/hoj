import router from "@/router";
import store from "@/store";
import roleCheck from "@/access/accessControl";
import { getUser } from "@/utils/auth";
import { looseIndexOf } from "@vue/shared";
import { ROLE_ENUM } from "@/access/roleEnum";

router.beforeEach(async (to, from, next) => {
  if (!getUser()) {
    if ((to.meta.role || "guest") === "guest") {
      next();
    } else {
      // todo 带上 redirect
      next("/login");
    }
  } else {
    if (store.state.user.loginUser.userName === "") {
      await store.dispatch("user/resetLoginUser");
    }
    if (!roleCheck(store.state.user.loginUser, to.meta.role as string)) {
      next("/noAuth");
    }
  }
  next();
});
