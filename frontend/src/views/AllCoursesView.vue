<script setup>
import { ref, onMounted } from 'vue'
import CourseRow from '@/components/CourseRow.vue'
import {getToken, msalInstance} from '@/msal/msalInstance'
import axios from "axios";

const courses = ref([])
const searchQuery = ref('')
const loading = ref(false)
const error = ref(null)

const userEmail = ref(null)

onMounted(() => {
  const account = msalInstance.getActiveAccount()
  if (account) {
    handleSearch()
    userEmail.value = account.idTokenClaims?.email || account.username
  } else {
    console.warn("No active MSAL account. User not logged in.")
  }
})

const fetchCourses = async (query = '') => {
  loading.value = true
  error.value = null
  const token = await getToken();
  try {
    const response = await axios.get('/api/courses', {
      headers: { Authorization: `Bearer ${token}` },
      params: query ? { query } : {}
    });
    courses.value = response.data
  } catch (err) {
    error.value = 'Kursuste laadimine ebaõnnestus.'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchCourses(searchQuery.value.trim())
}
</script>

<template>
  <div>
    <h2>Minu kursused</h2>

    <div class="container container--medium">
    <div>
      <p v-if="userEmail">Sisse logitud meiliga: <strong>{{ userEmail }}</strong></p>
      <input
          type="text"
          class="search-input"
          v-model="searchQuery"
          @input="handleSearch"
          placeholder="Kursuse nimi või kood"
      />
    </div>

    <div v-if="loading">Laadimine...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <ul v-if="courses.length > 0">
        <CourseRow
            v-for="course in courses"
            :key="course.id"
            :course="course"
        />
      </ul>
      <p v-else>Ei leitud sobivaid kursusi.</p>
    </div>
    </div>
  </div>
</template>
