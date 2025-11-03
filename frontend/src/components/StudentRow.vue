<template>
  <div>
    <button class="btn--link" @click="openPopup">Vaata hindeid</button>

    <PopupModal :visible="showPopup" @close="closePopup">
      <h2>{{ student.firstName }} {{ student.lastName }}</h2>
      <p>{{ student.email }}</p>

      <hr />
      <h3>Hinded</h3>
      <GradesView :courseId="Number(courseId)" :studentId="Number(student.id)" />

      <hr />

      <h3>Kommentaarid</h3>
      <CommentsView :courseId="Number(courseId)" :studentId="Number(student.id)" />
    </PopupModal>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PopupModal from '@/components/PopupModal.vue'
import CommentsView from '@/components/CommentsView.vue'
import GradesView from '@/components/GradesView.vue'

const props = defineProps({
  student: { type: Object, required: true },
  courseId: { type: Number, required: true }
})

const showPopup = ref(false)

function openPopup() {
  showPopup.value = true
}

function closePopup() {
  showPopup.value = false
}
</script>

