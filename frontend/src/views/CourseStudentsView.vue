<script setup>
import { ref, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import { fetchAllStudentsData } from "@/services/studentService.js";
import StudentTable from "@/components/StudentTable.vue";

// Route data
const route = useRoute();
const courseId = Number(route.params.courseId);
const courseName = route.query.courseName ?? "";

// Search (single source of truth)
const searchQuery = ref("");

// Refs to tables
const withDeclRef = ref(null);
const inactiveRef = ref(null);
const withoutDeclRef = ref(null);

// refresh all tables after updates
function refreshAll() {
  withDeclRef.value?.loadStudents();
  inactiveRef.value?.loadStudents();
  withoutDeclRef.value?.loadStudents();
}

// Preload data if needed
onMounted(async () => {
  try {
    await fetchAllStudentsData(courseId);
  } catch (err) {
    console.error("Error fetching students:", err);
  }
});

// Called on click OR Enter
const handleSearch = () => {
  // only active students use search
  withDeclRef.value?.loadStudents();
};

// Auto-refresh on clearing input
watch(searchQuery, (val) => {
  if (!val) {
    withDeclRef.value?.loadStudents();
  }
});
</script>

<template>
  <div class="container container--medium">
    <h1>{{ courseName }}</h1>

    <!-- Search bar -->
    <div class="search-block">
      <input
          type="text"
          v-model="searchQuery"
          placeholder="Otsi tudengit..."
          class="search-input"
          @keyup.enter="handleSearch"
      />
      <button class="search-button" @click="handleSearch">Otsi</button>
    </div>

    <!-- Active students (with search + sort + pagination) -->
    <StudentTable
        ref="withDeclRef"
        title="Aktiivsed tudengid"
        type="with-declarations"
        :courseId="courseId"
        :search="searchQuery"
        @updated="refreshAll"
    />

    <!-- Inactive students -->
    <StudentTable
        ref="inactiveRef"
        title="Mitteaktiivsed tudengid"
        type="inactive"
        :courseId="courseId"
        @updated="refreshAll"
    />

    <!-- Without declaration students -->
    <StudentTable
        ref="withoutDeclRef"
        title="Ilma deklaratsioonita tudengid"
        type="no-declarations"
        :courseId="courseId"
        @updated="refreshAll"
    />
  </div>
</template>
