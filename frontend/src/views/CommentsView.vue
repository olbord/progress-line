<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import apiClient from '@/services/api';
import CommentForm from './CommentForm.vue';

// get the dynamic route params
const route = useRoute();
const courseId = route.params.courseId;
const studentId = route.params.studentId;

const student = ref(null);

const fetchStudent = async () => {
  try {
    const response = await apiClient.get(`/courses/${courseId}/students/${studentId}`);
    student.value = response.data;
  } catch (err) {
    console.error("Failed to fetch student:", err);
  }
};

onMounted(fetchStudent);
</script>

<template>
  <main class="container container--narrow">
    <div class="student-info-card">
      <h1>Lisa kommentaar</h1>

      <div v-if="student">
        <table>
          <tbody>
          <tr>
            <td>
              <h2>{{ student.firstName }} {{ student.lastName }}</h2>
            </td>
          </tr>
          <tr>
            <td>{{ student.email }}</td>
          </tr>
          </tbody>
        </table>

        <!-- Pass both courseId and studentId to the comment form -->
        <CommentForm :studentId="parseInt(student.id)" :courseId="parseInt(courseId)" />
      </div>

      <div v-else>
        <p>Laen tudengi andmeid...</p>
      </div>
    </div>
  </main>
</template>

<style scoped>
table {
  width: 100%;
  margin-bottom: 1rem;
}
</style>
