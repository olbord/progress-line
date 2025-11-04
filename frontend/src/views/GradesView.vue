<template>
  <div>
    <div v-if="loading">Laen hinded...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <table v-else-if="grades.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-wide">Ülesanne</th>
        <th class="col-narrow">Tüüp</th>
        <th class="col-narrow">Hinne</th>
        <th class="col-medium">Kuupäev</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="grade in grades" :key="grade.id">
        <td>{{ grade.itemName }}</td>
        <td>{{ grade.itemType }}</td>
        <td>{{ grade.grade }}</td>
        <td>{{ new Date(grade.gradeDateGraded).toLocaleString() }}</td>
      </tr>
      </tbody>
    </table>

    <div v-else>Hinded puuduvad</div>



    <div class="container--narrow">
      <br>
      <button class="btn btn--link" @click="openCommentPopup">Lisa kommentaar</button>
    </div>

    <PopupModal :visible="showCommentPopup" @close="closeCommentPopup">
      <h2>Lisa uus kommentaar</h2>
      <p>Täpsusta, mis põhjusel tudengiga kontakteerusid.</p>
      <hr />

      <!-- See on ainus komponent, mida me siin kasutame -->
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
import PopupModal from '@/components/PopupModal.vue'
import CommentForm from '@/components/CommentForm.vue'

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

const grades = ref([])
const loading = ref(false)
const error = ref(null)

const courseId = props.courseId
const studentId = props.studentId

async function fetchGrades() {
  loading.value = true
  error.value = null
  grades.value = []
  try {
    const response = await apiClient.get(`/courses/${courseId}/students/${studentId}/grades`)
    grades.value = response.data
  } catch (err) {
    console.error(err)
    error.value = 'Viga andmete laadimisel'
  } finally {
    loading.value = false
  }
}

onMounted(fetchGrades)
watch(() => [props.courseId, props.studentId], fetchGrades)

const showCommentPopup = ref(false)

// Open pop-up
function openCommentPopup() {
  showCommentPopup.value = true
}

// Close pop-up
function closeCommentPopup() {
  showCommentPopup.value = false
}

// New comment
function handleCommentAdded() {
  closeCommentPopup()
}
</script>

