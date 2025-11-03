<template>
  <div class="comments-list">
    <div v-if="loading">Laen kommentaare...</div>
    <div v-else-if="error" class="message message--error">{{ error }}</div>

    <ul v-else-if="comments.length > 0" class="comment-list">
      <li v-for="comment in comments" :key="comment.id" class="comment-item">
        <p class="comment-text">{{ comment.commentText }}</p>
        <small class="comment-meta">
          Lisatud: {{ new Date(comment.createdAt).toLocaleString() }} | Viis: {{ comment.type }}
        </small>
      </li>
    </ul>

    <div v-else>Kommentaarid puuduvad.</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import apiClient from '@/services/api.js'

const props = defineProps({
  studentId: { type: Number, required: true },
  courseId: { type: Number, required: true }
})

const comments = ref([])
const loading = ref(true)
const error = ref(null)

const fetchComments = async () => {
  loading.value = true
  error.value = null
  try {
    const response = await apiClient.get(`/courses/${props.courseId}/students/${props.studentId}/comments`)
    comments.value = response.data
  } catch (err) {
    console.error("Failed to fetch comments:", err)
    error.value = "Kommentaaride laadimine ebaõnnestus."
  } finally {
    loading.value = false
  }
}

// Funktsioon, mis võimaldab vanem-komponendil sundida uuesti laadimist
function refresh() {
  fetchComments()
}
// See teeb 'refresh' funktsiooni kättesaadavaks vanem-komponendile
defineExpose({ refresh })

onMounted(fetchComments)
watch(() => [props.courseId, props.studentId], fetchComments)
</script>

<style scoped>
.comment-list {
  list-style: none;
  padding: 0;
  margin-top: 1rem;
}
.comment-item {
  border: 1px solid #eee;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 0.75rem;
  background: #fafafa;
}
.comment-text {
  margin: 0 0 0.5rem 0;
  white-space: pre-wrap; /* Säilitab reavahetused */
}
.comment-meta {
  color: #666;
  font-size: 0.8rem;
}
</style>