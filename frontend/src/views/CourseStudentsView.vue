<script setup>
import { ref, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import { searchActiveStudents, fetchAllStudentsData } from "@/services/studentService.js";
import StudentTable from "@/components/StudentTable.vue";

// Route data
const route = useRoute();
const courseId = Number(route.params.courseId);
const courseName = route.query.courseName ?? "";

// Search state
const searchQuery = ref("");
const filteredActiveStudents = ref(null);

// Table refs for refresh
const withDeclRef = ref(null);
const inactiveRef = ref(null);
const withoutDeclRef = ref(null);

function refreshAll() {
  withDeclRef.value?.loadStudents();
  inactiveRef.value?.loadStudents();
  withoutDeclRef.value?.loadStudents();
}

// Initial data loading (if needed elsewhere)
onMounted(async () => {
  try {
    await fetchAllStudentsData(courseId);
  } catch (err) {
    console.error("Error fetching students:", err);
  }
});

// Search logic
const handleSearch = async () => {
  if (!searchQuery.value || searchQuery.value.trim() === "") {
    filteredActiveStudents.value = null;
    return;
  }

  try {
    const response = await searchActiveStudents(courseId, searchQuery.value);

    if (Array.isArray(response)) {
      filteredActiveStudents.value = response;
    } else if (response && response.content) {
      filteredActiveStudents.value = response.content;
    } else {
      filteredActiveStudents.value = [];
    }
  } catch (err) {
    console.error("Viga otsingul:", err);
    filteredActiveStudents.value = [];
  }
};

// Reset search results when searchQuery empties
watch(searchQuery, (val) => {
  if (!val) filteredActiveStudents.value = null;
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

    <!-- Active students (searchable) -->
    <StudentTable
        ref="withDeclRef"
        title="Aktiivsed tudengid"
        type="with-declarations"
        :courseId="courseId"
        :searchResults="filteredActiveStudents"
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
