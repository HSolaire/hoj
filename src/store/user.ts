import { StoreOptions } from "vuex";
import { UserControllerService } from "@/../generated";
import { roleGetAll } from "@/access/accessControl";
import store from "@/store/index";
import { Message } from "@arco-design/web-vue";
import { updateScrollOffset } from "@arco-design/web-vue/es/tabs/utils";
// getters

export default {
  namespaced: true,
  // initial state
  state: () => ({
    loginUser: {
      userName: "",
      userRole: ["guest"],
    },
    isLoginUserLoaded: false,
    all: [],
  }),
  // 执行异步操作，并且触发 mutations 的更改，支持异步
  actions: {
    async loginHandle({ commit, state }, payload) {
      const res = await UserControllerService.userLoginUsingPost(payload);
      return new Promise((resolve, reject) => {
        const data = res.data;
        if (res.code == 0) {
          commit("UPDATE_USER", {
            userName: data.userName,
            userRole: roleGetAll(data.userRole),
          });
          resolve(res);
        } else {
          reject(res);
        }
      });
    },
    async resetLoginUser({ commit, state }, payload) {
      const res = await UserControllerService.getLoginUserUsingGet();
      return new Promise((resolve) => {
        if (res.code === 0) {
          commit("UPDATE_USER", {
            userName: res.data?.userName,
            userRole: roleGetAll(res.data?.userRole as string),
          });
        }
        resolve(res.code);
      });
    },
  },
  // 修改状态变量，尽量同步
  mutations: {
    UPDATE_USER(state, payload) {
      state.loginUser = payload;
      state.isLoginUserLoaded = true;
    },
    CLEAN_USER(state) {
      state.loginUser = {
        userName: "",
        userRole: ["guest"],
      };
      state.isLoginUserLoaded = false;
    },
  },
} as StoreOptions<any>;
