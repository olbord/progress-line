<template>
  <section class="mb-6">
    <h2 class="text-xl font-semibold mb-2">{{ title }}</h2>
    <ul class="list-disc pl-6">
      <li v-for="student in students" :key="student.id">
        {{ student.firstName }} {{ student.lastName }}
      </li>
    </ul>
  </section>
</template>

<script setup>
defineProps({
  title: String,
  students: {
    type: Array,
    default: () => []
  }
});
</script>
