<template>
  <div class="student-table">
    <h2>{{ title }}</h2>

    <table v-if="students.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-medium">Eesnimi / Perekonnanimi</th>
        <th class="col-wide">Email</th>
        <th class="col-narrow">Punktide summa</th>
        <th class="col-narrow">Tegevus</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="student in students" :key="student.id">
        <td>{{ student.firstName }} {{ student.lastName }}</td>
        <td>{{ student.email }}</td>
        <td>{{ (student.totalPoints ?? 0).toFixed(2) }}</td>
        <td><StudentRow :student="student" :courseId="courseId" /></td>
      </tr>
      </tbody>
    </table>

    <div v-else>Tudengeid ei leitud</div>

    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="page === 0">← Tagasi</button>
      <span>Leht {{ page + 1 }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="page >= totalPages - 1">Edasi →</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import StudentRow from '@/components/StudentRow.vue'
import axios from 'axios'

const props = defineProps({
  title: { type: String, required: true },
  courseId: { type: Number, required: true },
  type: {
    type: String,
    required: true,
    validator: (value) =>
        ['with-declarations', 'no-declarations', 'inactive'].includes(value),
  },
})

const students = ref([])
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)

async function loadStudents() {
  try {
    const response = await axios.get(
        `/api/courses/${props.courseId}/students/with-declarations`,
        {
          params: { page: page.value, size: size.value },
        }
    )

    students.value = response.data.content
    totalPages.value = response.data.totalPages
  } catch (error) {
    console.error('Viga tudengite laadimisel:', error)
    students.value = []
    totalPages.value = 0
  }
}

function nextPage() {
  if (page.value < totalPages.value - 1) {
    page.value++
    loadStudents()
  }
}

function prevPage() {
  if (page.value > 0) {
    page.value--
    loadStudents()
  }
}

onMounted(loadStudents)
</script>

<style scoped>
.student-table {
  margin: 20px auto;
  width: 100%;
  max-width: 900px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.pagination {
  margin-top: 1rem;
  display: flex;
  justify-content: center;
  gap: 10px;
}

button {
  padding: 6px 12px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  border-radius: 4px;
}

button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}
</style>

