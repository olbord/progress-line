<template>
  <div class="comment-form">
    <StudentPersonalDataDisplay
        :student-id="props.studentId"
        :course-id="props.courseId"
    />

    <div v-if="loading" class="message">Laen andmeid...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <form v-else @submit.prevent="sendEmail">
      <div class="comment-form__group">
        <h2><label for="email-content">Meili mustand (saate muuta):</label></h2>
        <textarea id="email-content" v-model="draft" rows="15" required></textarea>
      </div>

      <button type="submit" class="btn btn--primary"
              :disabled="isSending">
        <span v-if="isSending">Saadan...</span>
        <span v-else>Saada e-mail</span>
      </button>
    </form>

    <p v-if="successMessage" class="message message--success">E-mail on edukalt saadetud ja kommentaar lisatud!</p>
    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import apiClient from '@/services/api.js';
import StudentPersonalDataDisplay from '@/components/StudentPersonalDataDisplay.vue'
import {getToken} from "@/msal/msalInstance.js";

const props = defineProps({
  studentId: { type: [Number, String], required: true },
  courseId: { type: [Number, String], required: true }
});

const emit = defineEmits(['email-sent']);

const loading = ref(true);
const isSending = ref(false);
const error = ref(null);
const draft = ref('');
const successMessage = ref('');
const errorMessage = ref('');

async function fetchEmailDraft() {
  loading.value = true;
  error.value = null;

  try {
    const token = await getToken();

    const studentResp = await apiClient.get(
        `/students/${props.studentId}`,
        { headers: { Authorization: `Bearer ${token}` } }
    );

    const gradeResp = await apiClient.get(
        `/courses/${props.courseId}/students/${props.studentId}/latest-grade`,
        { headers: { Authorization: `Bearer ${token}` } }
    );

    draft.value = generateEmailDraft(studentResp.data, gradeResp.data);

  } catch (e) {
    console.error('Error fetching email draft data:', e);
    error.value = "Viga andmete laadimisel mustandi koostamiseks.";
  } finally {
    loading.value = false;
  }
}

function generateEmailDraft(student, grade) {

  const gradeInfo = grade && grade.itemName
      ? `Märkasin, et viimati said punkte üsna ammu, ${new Date(grade.gradeDateGraded).toLocaleDateString()} ülesande "${grade.itemName}" eest.`
      : `Märkasin, et sul ei ole sellel kursusel veel hindeid.`;

  return `Tere ${student.firstName}!

Uurin, kuidas sul kursusel läheb? ${gradeInfo} Usun, et saame sind aidata!

Parimate soovidega,
abiõppejõud`;
}

async function sendEmail() {
  try {
    const token = await getToken();
    isSending.value = true;

    await apiClient.post(
        `/courses/${props.courseId}/students/${props.studentId}/send-email`,
        { emailBody: draft.value },
        { headers: { Authorization: `Bearer ${token}` } }
    );

    successMessage.value = "E-mail edukalt saadetud! Sulgen akna...";
    setTimeout(() => emit('email-sent'), 1500);

  } catch (e) {
    console.error('Error sending email:', e);
    errorMessage.value = "Meili saatmine ebaõnnestus.";
  }
}

onMounted(fetchEmailDraft);
</script>