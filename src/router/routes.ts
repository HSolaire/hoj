import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import BasicLayout from "@/layouts/BasicLayout.vue";

export const basicRoutes: Array<RouteRecordRaw> = [
  {
    path: "/noAuth",
    name: "无权限",
    component: () => import("@/views/NoAuth.vue"),
    meta: {
      showInMenu: false,
    },
  },
  {
    path: "/login",
    name: "登录页",
    component: () => import("@/views/LoginView.vue"),
    meta: {
      showInMenu: false,
    },
  },
];

export const menuRoutes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "主页",
    component: BasicLayout,
    redirect: "home",
    children: [
      {
        path: "/home",
        name: "浏览题目",
        component: () => import("../views/HomeView.vue"),
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
    ],
  },
];
