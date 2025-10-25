<template>
  <div>
    <div v-if="loading">Laen hinded...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <table v-else-if="grades.length > 0">
      <thead>
      <tr>
        <th>Ülesanne</th>
        <th>Tüüp</th>
        <th>Hinne</th>
        <th>Kuupäev</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="grade in grades" :key="grade.id">
        <td>{{ grade.itemName }}</td>
        <td>{{ grade.itemType }}</td>
        <td>{{ grade.grade }}</td>
        <td>{{ new Date(grade.gradeDateGraded).toLocaleString() }}</td>
      </tr>
      </tbody>
    </table>

    <div v-else>Hinded puuduvad</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'

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

const grades = ref([])
const loading = ref(false)
const error = ref(null)

const courseId = props.courseId
const studentId = props.studentId

async function fetchGrades() {
  loading.value = true
  error.value = null
  grades.value = []
  try {
    const response = await apiClient.get(`/courses/${courseId}/grades/students/${studentId}`)
    grades.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'Viga andmete laadimisel'
  } finally {
    loading.value = false
  }
}

onMounted(fetchGrades)
watch(() => [props.courseId, props.studentId], fetchGrades)
</script>

<style scoped>
input {
  padding: 0.4rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 0.4rem 0.8rem;
  background: #e4067e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #c9056f;
}
table {
  border-collapse: collapse;
  width: 100%;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ddd;
  padding: 8px;
}
th {
  background: #f4f4f4;
  text-align: left;
}
</style>
