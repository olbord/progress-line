<template>
  <div class="student-table">
    <h2>{{ title }}</h2>

    <table v-if="students.length > 0" class="data-table">
      <thead>
      <tr>
        <th>Eesnimi / Perekonnanimi</th>
        <th>Email</th>
        <th>Punktide summa</th>
        <th>Tegevus</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="student in students" :key="student.id">
        <td>{{ student.firstName }} {{ student.lastName }}</td>
        <td>{{ student.email }}</td>
        <td>{{ (student.totalPoints ?? 0).toFixed(2) }}</td>
        <td>
          <button class="btn btn--link" @click="goToGrades(student.id)">
            Vaata hindeid
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-else>Tudengeid ei leitud</div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  title: { type: String, required: true },
  students: { type: Array, default: () => [] },
  courseId: { type: Number, required: true },
})

const router = useRouter()

function goToGrades(studentId) {
  router.push(`/grades/${props.courseId}/${studentId}`)
}
</script>

<style scoped>
.data-table {
  border-collapse: collapse;
  width: 100%;
  margin-top: 1rem;
  table-layout: fixed;
  min-width: 900px;
}

.data-table th,
.data-table td {
  border: 1px solid #ddd;
  padding: 8px;
  white-space: normal;
  overflow-wrap: break-word;
  vertical-align: top;
}

.data-table th {
  background: #f4f4f4;
  text-align: left;
}

.data-table th:nth-child(1),
.data-table td:nth-child(1) { width: 27%; }

.data-table th:nth-child(2),
.data-table td:nth-child(2) { width: 43%; }

.data-table th:nth-child(3),
.data-table td:nth-child(3) { width: 15%; }

.data-table th:nth-child(4),
.data-table td:nth-child(4) { width: 15%; }

.btn--link {
  background: #e4067e;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
  white-space: nowrap;
}

.btn--link:hover {
  background: #c9056f;
}
</style>
