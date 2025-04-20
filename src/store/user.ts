import { StoreOptions } from "vuex";

// getters

export default {
  namespaced: true,
  // initial state
  state: () => ({
    token: localStorage.getItem("token"),
    loginUser: {
      userName: "未登录",
      userRole: ["guest"],
    },
    all: [],
  }),
  // 执行异步操作，并且触发 mutations 的更改，支持异步
  actions: {
    async getLoginUser({ commit, state }, pyload) {
      commit("updateUser", pyload);
    },
  },
  // 修改状态变量，尽量同步
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
