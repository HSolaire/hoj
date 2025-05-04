import { StoreOptions } from "vuex";
import { UserControllerService } from "@/../generated";
import { Message } from "@arco-design/web-vue";
import { roleGetAll } from "@/access/accessControl";
import { setUser, getUser } from "@/utils/auth";
// getters

export default {
  namespaced: true,
  // initial state
  state: () => ({
    token: localStorage.getItem("token"),
    loginUser: {
      userName: getUser(),
      userRole: ["guest"],
    },
    all: [],
  }),
  // 执行异步操作，并且触发 mutations 的更改，支持异步
  actions: {
    async loginHandle({ commit, state }, pyload) {
      const res = await UserControllerService.userLoginUsingPost(pyload);
      return new Promise((resolve, reject) => {
        const data = res.data;
        setUser(data.userName as string);
        if (res.code == 0) {
          commit("updateUser", {
            userName: data.userName,
            userRole: roleGetAll(data.userRole),
          });
          resolve(res);
        } else {
          reject(res);
        }
      });
    },
  },
  // 修改状态变量，尽量同步
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
