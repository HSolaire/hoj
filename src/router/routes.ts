import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "浏览题目",
    component: HomeView,
  },
  {
    path: "/about",
    name: "关于我的",
    component: () => import("../views/AboutView.vue"),
  },
  {
    path: "/admin",
    name: "管理员页",
    component: () => import("../views/AdminView.vue"),
    meta: {
      role: "admin",
    },
  },
  {
    path: "/noAuth",
    name: "无权限",
    component: () => import("../views/NoAuth.vue"),
    meta: {
      showInMenu: false,
    },
  },
];



