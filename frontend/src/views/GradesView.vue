<template>
  <div class="container container--medium">
    <StudentPersonalDataDisplay
        :student-id="studentId"
        :course-id="courseId"
        @email-clicked="openEmailModal"
    />
    <br>
    <h2>Hinded</h2>

    <PopupModal :visible="showEmailPopup" @close="showEmailPopup = false">
      <EmailForm
          :course-id="courseId"
          :student-id="studentId"
          @email-sent="showEmailPopup = false"
      />
    </PopupModal>
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
    <br>
    <div class="container--narrow">

      <h2>Tudengi tagasiside</h2>
      <br>
      <button class="btn btn--link" @click="navigateToComments" style="margin-right: 1rem;">Loe kommentaare</button>
      <button class="btn btn--link" @click="navigateToFeedback">Vaata tagasisidet</button>
    </div>

    <PopupModal :visible="showCommentPopup" @close="closeCommentPopup">

      <CommentForm
          :course-id="courseId"
          :student-id="studentId"
          @comment-added="handleCommentAdded"
      />
    </PopupModal>

    <br>

    <h2>Tudengi deklaratsioon</h2>

    <p v-if="studentStatus">
      <strong>Tudengi kategooria:</strong>
      {{ studentStatus.active ? 'aktiivne' : 'inaktiivne' }}
      {{ studentStatus.declared ? 'deklaratsiooniga' : 'ilma deklaratsioonita' }}
    </p>

    <div class="actions" v-if="studentStatus">
      <!-- Declaration buttons -->
      <button
          v-if="studentStatus.declared"
          class="btn btn--link"
          @click="handleRemoveDeclaration"
      >
        Eemalda deklaratsioon
      </button>

      <button
          v-else
          class="btn btn--link"
          @click="handleAddDeclaration"
      >
        Lisa deklaratsioon
      </button>
      <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>
      <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
      <br>
      <br>

    </div>


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
import {getToken} from "@/msal/msalInstance.js";

const router = useRouter()

const successMessage = ref('');
const errorMessage = ref('');

// Must be used with StudentPersonalDataDisplay component to make email clickable
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

const grades = ref([])
const loading = ref(false)
const error = ref(null)

const courseId = props.courseId
const studentId = props.studentId

async function fetchGrades() {
  loading.value = true;
  error.value = null;

  try {
    const token = await getToken();
    const response = await apiClient.get(
        `/courses/${courseId}/students/${studentId}/grades`,
        { headers: { Authorization: `Bearer ${token}` } }
    );
    grades.value = response.data;
  } catch (err) {
    console.error("Error while loading students grades:", err);
    error.value = 'Tudengi hinnete laadimine ebaõnnestus';
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  if (props.courseId && props.studentId) {
    fetchGrades();
    fetchStudentStatus();
  } else {
    console.warn("Props not ready yet", props.courseId, props.studentId)
  }
})
watch(() => [props.courseId, props.studentId], () => {
  fetchGrades()
  fetchStudentStatus()
})

const showCommentPopup = ref(false)

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
      },
      query: {
        studentName: router.currentRoute.value.query.studentName,
        courseName: router.currentRoute.value.query.courseName
      }
    })
  } else {
    console.error("Unable to navigate: no student ID or no course ID.", props);
  }
}

function navigateToFeedback() {
  if (props.courseId && props.studentId) {
    router.push({
      name: 'students feedback',
      params: {
        courseId: props.courseId,
        studentId: props.studentId
      },
      query: {
        studentName: router.currentRoute.value.query.studentName,
        courseName: router.currentRoute.value.query.courseName
      }
    })
  } else {
    console.error("Unable to navigate: no student ID or no course ID.", props)
  }
}

const studentStatus = ref(null)

async function fetchStudentStatus() {
  try {
    const token = await getToken()
    const response = await apiClient.get(
        `/courses/${courseId}/students/${studentId}/status`,
        { headers: { Authorization: `Bearer ${token}` } }
    )
    studentStatus.value = response.data
  } catch (err) {
    console.error('Error fetching student status:', err)
  }
}

const emit = defineEmits(["updated"]);

async function handleRemoveDeclaration() {
  successMessage.value = '';
  errorMessage.value = '';
  try {
    const token = await getToken();
    await apiClient.delete(
        `/courses/${courseId}/declarations/student/${studentId}/no-declaration`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          },
          responseType: 'text' // tell Axios not to expect JSON
        }
    );
    successMessage.value = 'Deklaratsioon on eemaldatud!';
    await fetchStudentStatus();
    emit("updated");

    setTimeout(() => successMessage.value = '', 3000);
  } catch (e) {
    console.error("Failed to remove declaration:", e);
    errorMessage.value = 'Deklaratsiooni eemaldamine ebaõnnestus.';
    setTimeout(() => errorMessage.value = '', 4000);
  }
}

async function handleAddDeclaration() {
  successMessage.value = '';
  errorMessage.value = '';
  try {
    const token = await getToken();
    await apiClient.post(
        `/courses/${courseId}/declarations/student/${studentId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` }, responseType: 'text' }
    );
    successMessage.value = 'Deklaratsioon on lisatud!';
    await fetchStudentStatus();
    emit("updated");

    setTimeout(() => successMessage.value = '', 3000);
  } catch (e) {
    console.error("Failed to add declaration:", e);
    errorMessage.value = 'Deklaratsiooni lisamine ebaõnnestus.';
    setTimeout(() => errorMessage.value = '', 4000);
  }
}
</script>
