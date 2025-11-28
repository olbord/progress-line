<template>
  <nav class="course-tree-nav">
    <div class="tree-item">
      <RouterLink
          :to="{
          name: 'course',
          params: { courseId },
          query: { courseName }
        }"
          class="tree-link"
      >
        {{ courseName || 'Kursuse analüütika' }}
      </RouterLink>
    </div>

    <div class="tree-item indent-1">
      <RouterLink
          :to="{
          name: 'course students',
          params: { courseId },
          query: { courseName }
        }"
          class="tree-link"
      >
        Tudengid
      </RouterLink>
    </div>

    <div v-if="selectedStudentId" class="tree-section">
      <div class="tree-item indent-2">
        <RouterLink
            :to="{
            name: 'students grades',
            params: { courseId, studentId: selectedStudentId },
            query: { studentName: selectedStudentName }
          }"
            class="tree-link"
        >
          {{ selectedStudentName || `Tudeng #${selectedStudentId}` }}
        </RouterLink>
      </div>

      <div class="tree-item indent-3">
        <RouterLink
            :to="{
            name: 'students feedback',
            params: { courseId, studentId: selectedStudentId },
            query: { studentName: selectedStudentName }
          }"
            class="tree-link"
        >
          Tagasiside
        </RouterLink>
      </div>

      <div class="tree-item indent-3">
        <RouterLink
            :to="{
            name: 'students comments',
            params: { courseId, studentId: selectedStudentId },
            query: { studentName: selectedStudentName }
          }"
            class="tree-link"
        >
          Kommentaarid
        </RouterLink>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterLink } from 'vue-router'

const route = useRoute()

const courseId = computed(() => Number(route.params.courseId))
const courseName = computed(() => route.query.courseName)

const selectedStudentId = computed(() => {
  return route.params.studentId ? Number(route.params.studentId) : null
})

const selectedStudentName = computed(() => {
  return route.query.studentName || null
})
</script>

<style scoped>
.course-tree-nav {
  padding: 1rem;
  min-width: 250px;
}

.tree-item {
  margin: 0.25rem 0;
}

.tree-link {
  display: block;
  padding: 0.5rem;
  text-decoration: none;
  color: inherit;
  border-radius: 4px;
}

.tree-link:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.tree-link.router-link-active {
  background-color: rgba(0, 0, 0, 0.1);
  font-weight: bold;
}

.indent-1 {
  margin-left: 1rem;
}

.indent-2 {
  margin-left: 2rem;
}

.indent-3 {
  margin-left: 3rem;
}

.tree-section {
  border-left: 2px solid rgba(0, 0, 0, 0.1);
  margin-left: 2rem;
}
</style>
