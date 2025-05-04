import { createRouter, createWebHistory } from "vue-router";
import { basicRoutes, menuRoutes } from "./routes";
import store from "@/store";
import { ROLE_ENUM } from "@/access/roleEnum";

const router = createRouter({
  history: createWebHistory(),
  routes: [...basicRoutes, ...menuRoutes],
});

router.beforeEach((to, from, next) => {
  if (
    !store.state.user.loginUser?.userRole.includes(
      to.meta?.role ?? ROLE_ENUM.GUEST
    )
  ) {
    next("/noAuth");
    return;
  }
  next();
  return;
});

export default router;
