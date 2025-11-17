<template>
  <div class="container container--wide">
    <h1>Kursuse {{ courseName }} ülevaade </h1>

    <h2>Kursuse tudengid</h2>

      <main>
        <ProgressLineChart />
      </main>

    <button class="btn btn--link" @click="goToStudents">Vaata tudengite tabelit</button>

    <h2 style="margin-top: 2rem;">Kursuse õppejõud</h2>

    <div v-if="loadingTeachers">Laen õppejõude...</div>
    <div v-else-if="errorTeachers" class="message message--error">{{ errorTeachers }}</div>

    <ul v-else-if="teachers.length > 0" style="list-style: none; padding: 0;">
      <li v-for="teacher in teachers" :key="teacher.id" style="margin-bottom: 0.5rem;">
        <strong>{{ teacher.firstName }} {{ teacher.lastName }}</strong>
        <span v-if="teacher.email">({{ teacher.email }})</span>
      </li>
    </ul>
    <div v-else>
      <p>Sellel kursusel ei ole õppejõude määratud.</p>
    </div>
    <br>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import ProgressLineChart from '@/components/ProgressLineChart.vue'
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'

const router = useRouter()
const route = useRoute()

const teachers = ref([]);
const loadingTeachers = ref(false);
const errorTeachers = ref(null);

const courseId = route.params.courseId
const courseName = route.query.courseName || 'Kursus'

async function fetchTeachers() {
  if (!courseId) return;

  loadingTeachers.value = true;
  errorTeachers.value = null;

  try {
    const response = await apiClient.get(`/courses/${courseId}/teachers`);
    teachers.value = response.data;
  } catch (err) {
    console.error("Failed to fetch teachers:", err);
    errorTeachers.value = 'Õpetajate nimekirja laadimine ebaõnnestus.';
  } finally {
    loadingTeachers.value = false;
  }
}

onMounted(fetchTeachers);

watch(() => route.params.courseId, fetchTeachers);

const goToStudents = () => {
  router.push({
    name: 'course students',
    params: { courseId },
    query: { courseName }
  })
}
</script>
