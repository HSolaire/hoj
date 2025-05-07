import { createRouter, createWebHistory } from "vue-router";
import { basicRoutes, menuRoutes } from "./routes";
import store from "@/store";
import { ROLE_ENUM } from "@/access/roleEnum";

const router = createRouter({
  history: createWebHistory(),
  routes: [...basicRoutes, ...menuRoutes],
});

export default router;
