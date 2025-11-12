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

