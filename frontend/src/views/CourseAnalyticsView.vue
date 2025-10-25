<template>
  <div class="container container--wide">
    <h1>{{ courseName }}</h1>

    <div class="container container--wide">
      <main>
        <ProgressLineChart />
      </main>

      <button @click="goToStudents">Vaata tudengeid</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import ProgressLineChart from '@/components/ProgressLineChart.vue'

const router = useRouter()
const route = useRoute()

const courseId = route.params.courseId
const courseName = route.query.courseName || 'Kursus'

const goToStudents = () => {
  router.push({
    name: 'course students',
    params: { courseId },
    query: { courseName }
  })
}
</script>
