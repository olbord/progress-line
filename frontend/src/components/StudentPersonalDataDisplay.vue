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
import {getToken} from "@/msal/msalInstance.js";

const props = defineProps({
  studentId: {
    type: [Number, String],
    required: true
  },
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
  if (!props.studentId || props.studentId <= 0) {
    console.warn("Skipping API call: Student ID is invalid/missing.", props.studentId);
    return;
  }

  loading.value = true
  error.value = null
  studentInfo.value = null

  try {
    const token = await getToken();
    const response = await apiClient.get(
        `/students/${props.studentId}`,
        { headers: { Authorization: `Bearer ${token}` } }
    );

    studentInfo.value = response.data;

  } catch (err) {
    error.value = 'Unable to load student data';
  } finally {
    loading.value = false;
  }
}

onMounted(fetchStudentInfo)
watch(() => props.studentId, fetchStudentInfo)

</script>