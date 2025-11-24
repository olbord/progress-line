<template>
  <span v-if="isActive">
    {{ direction === "asc" ? "↑" : "↓" }}
  </span>
</template>

<script setup>
const props = defineProps({
  field: String,
  sort: String
});

const isActive = computed(() => props.sort.split(",")[0] === props.field);
const direction = computed(() => props.sort.split(",")[1]);
</script>
