import { RouteRecordRaw } from "vue-router";
import BasicLayout from "@/layouts/BasicLayout.vue";
import { ROLE_ENUM } from "@/access/roleEnum";

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
        path: "/addTopic",
        name: "创建题目",
        component: () => import("@/views/topic/AddTopicView.vue"),
        meta: {
          role: ROLE_ENUM.ADMIN,
        },
      },
      {
        path: "/home",
        name: "浏览题目",
        component: () => import("../views/HomeView.vue"),
      },
      {
        path: "/about",
        name: "关于我的",
        component: () => import("../views/AboutView.vue"),
        meta: {
          role: ROLE_ENUM.USER,
        },
      },
      {
        path: "/admin",
        name: "管理员页",
        component: () => import("../views/AdminView.vue"),
        meta: {
          role: ROLE_ENUM.ADMIN,
        },
      },
    ],
  },
];
