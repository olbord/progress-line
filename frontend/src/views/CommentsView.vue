<template>
  <div class="container container--medium">
    <StudentPersonalDataDisplay
        :student-id="props.studentId"
        :course-id="props.courseId"
        @email-clicked="openEmailModal"
    />

    <PopupModal :visible="showEmailPopup" @close="showEmailPopup = false">
      <EmailForm
          :course-id="props.courseId"
          :student-id="props.studentId"
          @email-sent="showEmailPopup = false"
      />
    </PopupModal>

    <h2>Kommentaarid</h2>
    <hr />

    <div v-if="loadingComments">
      <p class="message">Laen kommentaare...</p>
    </div>

    <div v-else-if="errorComments" class="message message--error">
      {{ errorComments }}
    </div>

    <div v-else-if="comments.length > 0">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <p><strong>{{ comment.type }}</strong> - {{ new Date(comment.dateTime).toLocaleDateString() }}</p>
        <p>{{ comment.commentText }}</p>
        <hr />
      </div>
    </div>

    <div v-else>
      <p class="message">Kommentaare ei ole veel lisatud.</p>
    </div>

    <div class="container--narrow">
      <br>
      <button class="btn btn--link" @click="openCommentPopup">Lisa kommentaar</button>
    </div>

    <PopupModal :visible="showCommentPopup" @close="closeCommentPopup">

      <CommentForm
          :course-id="props.courseId"
          :student-id="props.studentId"
          @comment-added="handleCommentAdded"
      />
    </PopupModal>

  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'
import PopupModal from "@/components/PopupModal.vue";
import CommentForm from '@/components/CommentForm.vue'
import StudentPersonalDataDisplay from '@/components/StudentPersonalDataDisplay.vue'
import EmailForm from '@/components/EmailForm.vue'

const comments = ref([]);
const loadingComments = ref(false)
const errorComments = ref(null)
const showCommentPopup = ref(false)

// Must be used with StudentPersonalDataDisplay component to make email clickable
const showEmailPopup = ref(false)
function openEmailModal() {
  console.log("Modal open function called.");
  showEmailPopup.value = true
}

const props = defineProps({
  studentId: {
    type: String,
    required: true
  },
  courseId: {
    type: String,
    required: true
  }
})

const fetchComments = async () => {
  const courseId = Number(props.courseId);
  const studentId = Number(props.studentId);

  if (!courseId || !studentId) {
    console.warn("Skipping API call: Course ID or Student ID is missing.");
    return;
  }

  loadingComments.value = true
  errorComments.value = null

  try {
    const response = await apiClient.get(`/courses/${courseId}/students/${studentId}/comments`)
    comments.value = response.data
  } catch (err) {
    console.error("Failed to fetch comments:", err)
    errorComments.value = 'Kommentaaride laadimine ebaõnnestus.'
  } finally {
    loadingComments.value = false
  }
}

onMounted(fetchComments)

watch(() => [props.courseId, props.studentId], fetchComments)

function openCommentPopup() {
  showCommentPopup.value = true
}

function closeCommentPopup() {
  showCommentPopup.value = false
  fetchComments()
}

function handleCommentAdded() {
  closeCommentPopup()
}

</script>

