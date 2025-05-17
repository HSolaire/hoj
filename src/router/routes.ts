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
    component: () => import("@/views/LoginIndex.vue"),
    meta: {
      showInMenu: false,
    },
  },
];

export const menuRoutes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: BasicLayout,
    redirect: "topic/scan",
    children: [
      {
        path: "/topic/scan",
        name: "浏览题目",
        component: () => import("../views/TopicView.vue"),
      },
      {
        path: "/topic/subRecord",
        name: "提交记录",
        component: () => import("../views/HomeView.vue"),
      },
      {
        path: "/topic/add",
        name: "创建题目",
        component: () => import("@/views/topic/AddTopicView.vue"),
        meta: {
          role: ROLE_ENUM.ADMIN,
        },
      },
      {
        path: "/topic/update/:id",
        name: "更新题目",
        component: () => import("@/views/topic/AddTopicView.vue"),
        meta: {
          role: ROLE_ENUM.USER,
          showInMenu: false,
        },
      },
      {
        path: "/topic/do/:id",
        name: "在线做题",
        component: () => import("@/views/topicdo/TopicDoView.vue"),
        meta: {
          role: ROLE_ENUM.USER,
          showInMenu: false,
        },
      },
      {
        path: "/main",
        name: "主页",
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
