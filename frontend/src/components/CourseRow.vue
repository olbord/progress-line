<script setup>
defineProps({
  course: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <li>
    <strong>{{ course.shortName }}</strong> — {{ course.fullName }}
  </li>
</template>
