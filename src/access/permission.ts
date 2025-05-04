import router from "@/router";
import store from "@/store";
import roleCheck from "@/access/accessControl";

router.beforeEach((to, from, next) => {
  if (!(to.meta.role === undefined || to.meta.role === "guest")) {
    // if (store.state.user.token == "") {
    //   next("/login");
    //   return;
    // }

    if (!roleCheck(store.state.user.loginUser, to.meta.role as string)) {
      next("/noAuth");
      return;
    }
  }

  next();
  return;
});
