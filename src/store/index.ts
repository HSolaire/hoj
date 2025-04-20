import { createStore } from "vuex";
import user from "@/store/user";

export default createStore({
  // 需要保存的全局状态
  modules: { user },
});
