<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import CourseRow from '@/components/CourseRow.vue'

const courses = ref([])
const searchQuery = ref('')
const loading = ref(false)
const error = ref(null)

const fetchCourses = async (query = '') => {
  loading.value = true
  error.value = null
  try {
    const payload = query ? { query } : {}
    const response = await axios.post('/api/courses', payload)
    courses.value = response.data
  } catch (err) {
    error.value = 'Failed to load courses.'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchCourses(searchQuery.value.trim())
}

onMounted(() => {
  fetchCourses()
})
</script>

<template>
  <div>
    <h2>All Courses</h2>

    <div>
      <input
          type="text"
          v-model="searchQuery"
          placeholder="Search by short or full name"
      />
      <button @click="handleSearch">Search</button>
    </div>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <ul v-if="courses.length > 0">
        <CourseRow
            v-for="course in courses"
            :key="course.id"
            :course="course"
        />
      </ul>
      <p v-else>No courses found.</p>
    </div>
  </div>
</template>
