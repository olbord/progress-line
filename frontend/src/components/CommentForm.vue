<template>
  <div class="comment-form">
    <StudentPersonalDataDisplay
        :student-id="props.studentId"
        :course-id="props.courseId"
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

      <div v-if="newComment.type === 'EMAIL'" style="margin-top: 1rem;">
        <button type="button"
                class="btn btn--primary"
                @click="openEmailPopup">
          Saada e-mail
        </button>
      </div>
      <br>
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


      <div style="margin-top: 1rem;">
        <button type="submit"
                class="btn btn--primary">
          Salvesta kommentaar
        </button>
      </div>

    </form>

    <PopupModal :visible="showEmailPopup" @close="closeEmailPopupAndRefresh">
      <EmailForm
          :course-id="props.courseId"
          :student-id="props.studentId"
          @email-sent="closeEmailPopupAndRefresh"
      />
    </PopupModal>

    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>
    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import apiClient from '@/services/api.js';
import StudentPersonalDataDisplay from '@/components/StudentPersonalDataDisplay.vue'
import PopupModal from '@/components/PopupModal.vue'
import EmailForm from '@/components/EmailForm.vue'

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
  teacherId: 1,  // Educational mistake: pass teacherId from JWT token
  courseId: props.courseId
});

const showEmailPopup = ref(false);
const emit = defineEmits(['comment-added']); // Needed to close modal

async function submitComment() {
  successMessage.value = '';
  errorMessage.value = '';

  if (newComment.value.type === 'EMAIL') {
    openEmailPopup();
    return; // Comment is not saved
  }

  if (!newComment.value.commentText.trim()) {
    errorMessage.value = 'Kommentaar ei saa olla tühi.';
    return;
  }
  try {
    await apiClient.post(
        `/courses/${props.courseId}/students/${props.studentId}/comments`,
        newComment.value
    );
    successMessage.value = 'Kommentaar on edukalt salvestatud!';
    newComment.value.commentText = '';
    setTimeout(() => {
      // Leave some time to see success message
      emit('comment-added');
    }, 1500);
  } catch (error) {
    console.error('Error submitting comment:', error);
    errorMessage.value = 'Kommentaari salvestamine ebaõnnestus. Palun proovi uuesti.';
  }
}

function openEmailPopup() {
  showEmailPopup.value = true;
}

function closeEmailPopupAndRefresh() {
  showEmailPopup.value = false;
  emit('comment-added');
}

</script>
