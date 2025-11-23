<script setup>
import { ref, watch, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { fetchAllStudentsData } from "@/services/studentService.js";
import StudentTable from "@/components/StudentTable.vue";

const route = useRoute();
const router = useRouter();

const courseId = Number(route.params.courseId);
const courseName = route.query.courseName ?? "";

// initial from URL
const initialSearch = route.query.search ?? "";
const searchQuery = ref(initialSearch);

// update URL when input changes
watch(searchQuery, (val) => {
  router.replace({
    query: {
      ...route.query,
      search: val || undefined // remove if empty
    }
  });
});

const withDeclRef = ref(null);
const inactiveRef = ref(null);
const withoutDeclRef = ref(null);

const handleSearch = () => {
  router.replace({
    query: {
      ...route.query,
      search: searchQuery.value || undefined
    }
  });

  withDeclRef.value?.loadStudents();
};

function refreshAll() {
  withDeclRef.value?.loadStudents();
  inactiveRef.value?.loadStudents();
  withoutDeclRef.value?.loadStudents();
}

onMounted(async () => {
  try {
    await fetchAllStudentsData(courseId);
  } catch (err) {
    console.error("Error fetching students:", err);
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

    <StudentTable
        ref="withDeclRef"
        title="Aktiivsed tudengid"
        type="with-declarations"
        :courseId="courseId"
        :search="searchQuery"
        @updated="refreshAll"
    />

    <StudentTable
        ref="inactiveRef"
        title="Mitteaktiivsed tudengid"
        type="inactive"
        :courseId="courseId"
        :search="searchQuery"
        @updated="refreshAll"
    />

    <StudentTable
        ref="withoutDeclRef"
        title="Ilma deklaratsioonita tudengid"
        type="no-declarations"
        :courseId="courseId"
        :search="searchQuery"
        @updated="refreshAll"
    />
  </div>
</template>
