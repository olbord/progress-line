<template>
  <div class="comment-form">
    <StudentPersonalDataDisplay
        :student-id="props.studentId"
    />
    <form @submit.prevent="submitComment">
      <div class="comment-form__group">
        <label for="comment-type">Kontakteerumise viis</label>
        <select id="comment-type" v-model="newComment.type">
          <option v-for="type in commentTypes" :key="type" :value="type">
            {{ type }}
          </option>
        </select>
      </div>

      <div class="comment-form__group">
        <label for="comment-text">Kommentaar</label>
        <textarea
            id="comment-text"
            v-model="newComment.commentText"
            placeholder="Täpsusta, mis põhjusel tudengiga kontakteerusid."
            rows="6"
            required
        ></textarea>
      </div>

      <button type="submit" class="btn btn--primary">Salvesta kommentaar</button>
    </form>

    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>
    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import apiClient from '@/services/api.js';
import StudentPersonalDataDisplay from '@/components/StudentPersonalDataDisplay.vue'

const props = defineProps({
  studentId: {
    type: Number,
    required: true
  },
  courseId: {
    type: Number,
    required: true
  }
});

const commentTypes = ['EMAIL', 'DISCORD', 'STUDY_GROUP', 'CONSULTATION', 'OTHER'];
const successMessage = ref('');
const errorMessage = ref('');
const newComment = ref({
  commentText: '',
  type: 'EMAIL',
  studentId: props.studentId,
  teacherId: 1,  // TODO: pass teacherId from JWT token?
  courseId: props.courseId
});

async function submitComment() {
  successMessage.value = '';
  errorMessage.value = '';

  if (!newComment.value.commentText.trim()) {
    errorMessage.value = 'Kommentaar ei saa olla tühi.';
    return;
  }
  try {
    await apiClient.post("/courses/${courseId}/students/${studentId}/comments", newComment.value);
    successMessage.value = 'Kommentaar on edukalt salvestatud!';
    newComment.value.commentText = '';
  } catch (error) {
    console.error('Error submitting comment:', error);
    errorMessage.value = 'Kommentaari salvestamine ebaõnnestus. Palun proovi uuesti.';
  }
}
</script>
