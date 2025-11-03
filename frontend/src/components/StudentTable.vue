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
          <StudentRow :student="student" :courseId="courseId" />
        </td>
      </tr>
      </tbody>
    </table>

    <div v-else>Tudengeid ei leitud</div>
  </div>
</template>

<script setup>
import StudentRow from '@/components/StudentRow.vue'

const props = defineProps({
  title: { type: String, required: true },
  students: { type: Array, default: () => [] },
  courseId: { type: Number, required: true },
})
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

</style>
