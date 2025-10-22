<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  course: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const goToCourse = () => {
  router.push({ name: 'course', params: { courseId: props.course.id } })
}
</script>

<template>
  <li @click="goToCourse" style="cursor: pointer;">
    <strong>{{ course.shortName }}</strong> — {{ course.fullName }}
  </li>
</template>

