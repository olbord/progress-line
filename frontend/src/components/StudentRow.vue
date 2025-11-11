<template>
  <div>
    <button class="btn--link" @click="navigateToGrades">Vaata hindeid</button>
  </div>
</template>


<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  student: { type: Object, required: true },
  courseId: { type: Number, required: true }
})

function navigateToGrades() {
  if (props.courseId && props.student && props.student.id) {
    router.push(`/courses/${props.courseId}/students/${props.student.id}/grades`)
  } else {
    console.error("Unable to navigate: no student ID or no course ID.", props)
  }
}
</script>

