<template>
  <div class="container container--medium">
    <h1>Tudengi hinded</h1>

    <div class="horizontal-group">
      <input
          v-model="studentIdInput"
          type="number"
          placeholder="Sisesta tudengi ID"
      />
      <button @click="fetchGrades" class="btn btn--primary">Otsi hinded</button>
    </div>

    <div v-if="loading">Laen hinded...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <table v-else-if="grades.length > 0" class="data-table">
      <thead>
      <tr>
        <th>Kursuse ID</th>
        <th>Ülesanne</th>
        <th>Tüüp</th>
        <th>Hinne</th>
        <th>Kuupäev</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="grade in grades" :key="grade.id">
        <td>{{ grade.courseId }}</td>
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
import { ref } from 'vue'
import apiClient from "@/services/api.js";

const studentIdInput = ref('')
const grades = ref([])
const loading = ref(false)
const error = ref(null)

async function fetchGrades() {
  if (!studentIdInput.value) {
    error.value = 'Palun sisesta tudengi ID'
    return
  }
  loading.value = true
  error.value = null
  grades.value = []
  try {
    const response = await apiClient.get(`/grades/students/${studentIdInput.value}`)
    grades.value = response.data
  } catch (err) {
    error.value = 'Viga andmete laadimisel'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.search-box {
  margin: 1rem 0;
  display: flex;
  gap: 0.5rem;
}
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
.error {
  color: red;
  margin: 0.5rem 0;
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
