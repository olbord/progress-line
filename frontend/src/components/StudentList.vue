<template>
  <section class="mb-6">
    <h2 class="text-xl font-semibold mb-2">{{ title }}</h2>
    <ul class="list-disc pl-6">
      <li v-for="student in students" :key="student.id">
        <StudentRow :student="student" :courseId="courseId" />
      </li>
    </ul>
  </section>
</template>

<script setup>
import StudentRow from './StudentRow.vue';

defineProps({
  title: String,
  students: {
    type: Array,
    default: () => []
  },
  courseId: {
    type: Number,
    required: true
  }
});
</script>
