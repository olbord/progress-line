<template>
  <div >
    <div v-if="loading" class="message">Laen tudengi andmeid...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>
    <div v-else-if="studentInfo" style="margin-bottom: 2rem;">

      <h2>
        Tudeng: {{ studentInfo.firstName }} {{ studentInfo.lastName }}
      </h2>

      <p>
        Email:
        <a href="#" @click="handleEmailClick">
          {{ studentInfo.email }}
        </a>
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
  courseId: {
    type: [Number, String],
    required: true
  }
})

const studentInfo = ref(null)
const loading = ref(false)
const error = ref(null)

const emit = defineEmits(['emailClicked']);

function handleEmailClick(event) {
  // Do not allow browser mailto
  event.preventDefault();
  console.log("Email clicked! Emitting event.");
  emit('emailClicked', props.studentId);
}

async function fetchStudentInfo() {
  if (!props.studentId || props.studentId <= 0|| !props.courseId || props.courseId <= 0) {
    console.warn("Skipping API call: Student or course  ID is invalid/missing.", props.studentId, props.courseId);
    return;
  }

  loading.value = true
  error.value = null
  studentInfo.value = null

  try {
    const response = await apiClient.get(`/courses/${props.courseId}/students/${props.studentId}`)
    studentInfo.value = response.data
  } catch (err) {
    console.error("Error fetching student info:", err)
    error.value = 'Unable to load student data'
  } finally {
    loading.value = false
  }
}

onMounted(fetchStudentInfo)
watch([() => props.courseId, () => props.studentId], fetchStudentInfo, {immediate: true})

</script>