<template>
  <div class="grades">
    <h2>Tudengi hinded</h2>

    <div class="search-box">
      <input
          v-model="studentIdInput"
          type="number"
          placeholder="Sisesta tudengi ID"
      />
      <button @click="fetchGrades">Otsi hinded</button>
    </div>

    <div v-if="loading">Laen hinded...</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <table v-else-if="grades.length > 0">
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
    const res = await fetch(`http://localhost:5173/api/grades/students/${studentIdInput.value}`)
    if (!res.ok) throw new Error('Viga andmete laadimisel')
    grades.value = await res.json()
  } catch (err) {
    error.value = err.message
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
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #45a049;
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
