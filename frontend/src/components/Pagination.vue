<template>
  <div class="pagination" v-if="totalPages > 1">

    <button
        class="page-btn"
        :disabled="modelValue === 0"
        @click="change(modelValue - 1)"
    >
      «
    </button>

    <button
        v-for="p in visible"
        :key="p"
        class="page-btn"
        :disabled="p === '...'"
        :class="{ active: p === modelValue + 1 }"
        @click="p !== '...' && change(p - 1)"
    >
      {{ p }}
    </button>

    <button
        class="page-btn"
        :disabled="modelValue >= totalPages - 1"
        @click="change(modelValue + 1)"
    >
      »
    </button>

  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  modelValue: { type: Number, required: true },  // текущая страница
  totalPages: { type: Number, required: true },
});

const emit = defineEmits(["update:modelValue"]);

function change(p) {
  if (p >= 0 && p < props.totalPages) {
    emit("update:modelValue", p);
  }
}

const visible = computed(() => {
  const total = props.totalPages;
  const current = props.modelValue + 1;
  const delta = 2;
  let pages = [];

  for (let i = Math.max(1, current - delta); i <= Math.min(total, current + delta); i++) {
    pages.push(i);
  }

  if (pages[0] > 1) {
    if (pages[0] > 2) pages.unshift("...");
    pages.unshift(1);
  }

  if (pages[pages.length - 1] < total) {
    if (pages[pages.length - 1] < total - 1) pages.push("...");
    pages.push(total);
  }

  return pages;
});
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  margin-top: 16px;
  flex-wrap: wrap;
}

.page-btn {
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 6px 12px;
  min-width: 34px;
  cursor: pointer;
  transition: 0.25s;
}

.page-btn:hover:not(:disabled) {
  background-color: #eee;
}

.page-btn.active {
  background-color: #2e236c;
  color: white;
  border-color: #2e236c;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
