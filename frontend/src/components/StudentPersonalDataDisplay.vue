<template>
  <div >
    <div v-if="loading" class="message">Laen tudengi andmeid...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>
    <div v-else-if="studentInfo" style="margin-bottom: 2rem;">

      <h2>
        Tudeng: {{ studentInfo.firstName }} {{ studentInfo.lastName }}
      </h2>

      <p>
        Email: <a :href="'mailto:' + studentInfo.email">{{ studentInfo.email }}</a>
      </p>

      <slot :student="studentInfo"></slot>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'

const props = defineProps({
  studentId: {
    type: [Number, String],
    required: true
  },
})

const studentInfo = ref(null)
const loading = ref(false)
const error = ref(null)

async function fetchStudentInfo() {
  if (!props.studentId || props.studentId <= 0) {
    console.warn("Skipping API call: Student ID is invalid/missing.", props.studentId);
    return;
  }

  loading.value = true
  error.value = null
  studentInfo.value = null

  try {
    const response = await apiClient.get(`/students/${props.studentId}`)
    studentInfo.value = response.data
  } catch (err) {
    console.error("Error fetching student info:", err)
    error.value = 'Unable to load student data'
  } finally {
    loading.value = false
  }
}

onMounted(fetchStudentInfo)
watch(() => props.studentId, fetchStudentInfo)
</script>