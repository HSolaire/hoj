import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import "@/access/permission.ts";
import "bytemd/dist/index.css";
import "highlight.js/styles/default.css";
import "@/global.css";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api.js";
import "@/utils/requests";
import ArcoVueIcon from "@arco-design/web-vue/es/icon";

createApp(App).use(store).use(router).use(ArcoVue).mount("#app");
