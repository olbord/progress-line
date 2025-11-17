<template>
  <div class="container container--medium">
    <StudentPersonalDataDisplay
        :student-id="props.studentId"
        @email-clicked="openEmailModal"
    />

    <PopupModal :visible="showEmailPopup" @close="showEmailPopup = false">
      <EmailForm
          :course-id="props.courseId"
          :student-id="props.studentId"
          @email-sent="showEmailPopup = false"
      />
    </PopupModal>

    <div v-if="loading">Laen tagasisidet...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <table v-else-if="feedbackResponses.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-medium">Küsitlus</th>
        <th class="col-wide">Küsimus</th>
        <th class="col-medium">Vastus</th>
        <th class="col-medium">Vastamise aeg</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(response, index) in feedbackResponses" :key="index">
        <td>{{ response.feedbackName }}</td>
        <td>{{ response.questionName }}</td>
        <td>{{ response.printValue }}</td>
        <td>{{ new Date(response.responseTime).toLocaleString() }}</td>
      </tr>
      </tbody>
    </table>

    <div v-else>Tagasiside puudub</div>

    <div class="container--narrow">
      <br>
      <button class="btn btn--link" @click="navigateToComments" style="margin-right: 1rem;">Loe kommentaare</button>
      <button class="btn btn--link" @click="openCommentPopup" style="margin-right: 1rem;">Lisa kommentaar</button>
      <button class="btn btn--link" @click="navigateToGrades">Vaata hindeid</button>
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
import { useRouter } from 'vue-router'
import PopupModal from '@/components/PopupModal.vue'
import CommentForm from '@/components/CommentForm.vue'
import StudentPersonalDataDisplay from '@/components/StudentPersonalDataDisplay.vue'
import EmailForm from "@/components/EmailForm.vue";

const router = useRouter()


const showEmailPopup = ref(false)
function openEmailModal() {
  console.log("Modal open function called.");
  showEmailPopup.value = true
}

const props = defineProps({
  studentId: {
    type: Number,
    required: true
  },
  courseId: {
    type: Number,
    required: true
  }
})

const feedbackResponses = ref([])
const loading = ref(false)
const error = ref(null)

const courseId = props.courseId
const studentId = props.studentId

async function fetchFeedback() {
  loading.value = true
  error.value = null
  feedbackResponses.value = [] // Tühjenda eelmised andmed
  try {
    const response = await apiClient.get(`/courses/${courseId}/students/${studentId}/feedback`)
    feedbackResponses.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'Error while loading student feedback responses'
  } finally {
    loading.value = false
  }
}

onMounted(fetchFeedback)
watch(() => [props.courseId, props.studentId], fetchFeedback)

const showCommentPopup = ref(false)

function openCommentPopup() {
  showCommentPopup.value = true
}

function closeCommentPopup() {
  showCommentPopup.value = false
}

function handleCommentAdded() {
  closeCommentPopup()
}

function navigateToComments() {
  if (props.courseId && props.studentId) {
    router.push({
      name: 'students comments',
      params: {
        courseId: props.courseId,
        studentId: props.studentId
      }
    })
  } else {
    console.error("Unable to navigate: no student ID or no course ID.", props);
  }
}

function navigateToGrades() {
  if (props.courseId && props.studentId) {
    router.push(`/courses/${props.courseId}/students/${props.studentId}/grades`)
  } else {
    console.error("Unable to navigate: no student ID or no course ID.", props)
  }
}
</script>
