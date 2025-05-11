import axios from "axios";
import { Message } from "@arco-design/web-vue";
import router from "@/router";
import store from "@/store";
import * as timers from "node:timers";
// axios.defaults.withCredentials = true;
const excludePaths = ["/api/user/get/login"];

axios.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (
      !excludePaths.includes(new URL(response.config.url as string).pathname)
    ) {
      if (res.code === 40100) {
        store.commit("user/CLEAN_USER");
        router.replace("/login").then(() => {
          Message.error("请先登录！");
        });

        // 会强行盖掉 提示
        /*        Message.error("请先登录！");
                setTimeout(() => {
                  window.location.href = "/login";
                }, 200);*/
      } else if (res.code !== 0) {
        Message.error(res.message);
      }
    }
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);
