<script setup>
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'
import CommentForm from './CommentForm.vue'

const props = defineProps({
  studentId: {
    type: Number,
    required: true
  },
  courseId: {
    type: Number,
    required: true
  }
})

const student = ref(null)
const fetchStudent = async () => {
  try {
    const response = await apiClient.get(`/courses/${props.courseId}/students/${props.studentId}/comments`)
    student.value = response.data
  } catch (err) {
    console.error("Failed to fetch student:", err)
  }
}

onMounted(fetchStudent)
watch(() => [props.courseId, props.studentId], fetchStudent)
</script>

<template>
  <div>
    <div v-if="student">
      <h4>{{ student.firstName }} {{ student.lastName }}</h4>
      <p>{{ student.email }}</p>
      <CommentForm :studentId="props.studentId" :courseId="props.courseId" />
    </div>
    <div v-else>
      <p>Laen tudengi andmeid...</p>
    </div>
  </div>
</template>
