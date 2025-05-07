<template>
  <div ref="editorContainer" class="monaco-editor"></div>
</template>

<script lang="ts" setup>
import {
  ref,
  watch,
  onMounted,
  onBeforeUnmount,
  toRefs,
  defineProps,
  defineEmits,
} from "vue";
import { editor } from "monaco-editor";

const props = defineProps({
  value: {
    type: String,
    default: "// Start coding...\n",
  },
  language: {
    type: String,
    default: "java",
  },
  theme: {
    type: String,
    default: "vs-dark",
  },
});
const emit = defineEmits(["update:modelValue"]);

const editorContainer = ref<HTMLElement | null>(null);
let monacoEditor: editor.IStandaloneCodeEditor | null = null;

// 初始化配置
const { language, theme } = toRefs(props);

// 初始化 Monaco Editor
const initEditor = async () => {
  if (editorContainer.value) {
    monacoEditor = editor.create(editorContainer.value, {
      value: props.value,
      language: language.value,
      theme: theme.value,
      minimap: { enabled: false },
      automaticLayout: true,
    });

    // 监听编辑器内容变化
    monacoEditor.onDidChangeModelContent(() => {
      const value = monacoEditor?.getValue() || "";
      emit("update:modelValue", value);
    });
  }
};

// 监听语言变化
watch(language, (newLang) => {
  if (monacoEditor) {
    const model = monacoEditor.getModel();
    if (model) {
      editor.setModelLanguage(model, newLang);
    }
  }
});

// 监听主题变化
watch(theme, (newTheme) => {
  if (monacoEditor) {
    editor.setTheme(newTheme);
  }
});

onMounted(() => {
  initEditor();
});

onBeforeUnmount(() => {
  if (monacoEditor) {
    monacoEditor.dispose();
  }
});
</script>

<style scoped>
.monaco-editor {
  width: 100%;
  height: 600px;
  border: 1px solid #434343;
  border-radius: 4px;
}
</style>
