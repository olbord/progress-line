<template>
  <div class="student-table">
    <h2>{{ title }}</h2>

    <div v-if="isLoading" class="loading">
      Laadimine...
    </div>

    <table v-if="!isLoading && displayedStudents.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-medium">Eesnimi / Perekonnanimi</th>
        <th class="col-wide">Email</th>
        <th class="col-narrow">Punktide summa</th>
        <th class="col-narrow">Tegevus</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="student in displayedStudents" :key="student.id">
        <td>{{ student.firstName }} {{ student.lastName }}</td>
        <td>{{ student.email }}</td>
        <td>{{ (student.totalPoints ?? 0).toFixed(2) }}</td>
        <td>
          <StudentRow
              :student="student"
              :courseId="courseId"
              :type="type"
              @updated="onRowUpdated"
          />
        </td>
      </tr>
      </tbody>
    </table>

    <div v-else-if="!isLoading && displayedStudents.length === 0">
      {{ isSearchMode ? "Otsingu tulemused puuduvad" : "Tudengeid ei leitud" }}
    </div>

    <Pagination
        v-if="!isSearchMode && !isLoading && internalStudents.length > 0"
        v-model="page"
        :totalPages="totalPages"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import StudentRow from "@/components/StudentRow.vue";
import Pagination from "@/components/Pagination.vue";

import {
  fetchStudentsWithDeclaration,
  fetchInactiveStudents,
  fetchStudentsWithoutDeclaration
} from "@/services/studentService.js";

const route = useRoute();
const router = useRouter();

const emit = defineEmits(["updated"]);

const props = defineProps({
  title: { type: String, required: true },
  courseId: { type: Number, required: true },
  type: {
    type: String,
    required: true,
    validator: (v) =>
        ["with-declarations", "inactive", "no-declarations"].includes(v)
  },
  searchResults: {
    type: Array,
    default: null
  }
});

// Loading + internal pagination state
const isLoading = ref(false);
const internalStudents = ref([]);
const size = ref(10);
const totalPages = ref(0);

// Search mode flag
const isSearchMode = computed(() => props.searchResults !== null);

// Students to display
const displayedStudents = computed(() => {
  return isSearchMode.value ? props.searchResults : internalStudents.value;
});

// Determine query param name
const pageParam = computed(() => {
  switch (props.type) {
    case "with-declarations": return "withDeclPage";
    case "inactive": return "inactivePage";
    case "no-declarations": return "noDeclPage";
  }
});

// Initial page from query
const rawPage = Number(route.query[pageParam.value]);
const page = ref(!isNaN(rawPage) && rawPage > 0 ? rawPage - 1 : 0);

// Service loader
function loadFromService(courseId, page, size) {
  switch (props.type) {
    case "with-declarations": return fetchStudentsWithDeclaration(courseId, page, size);
    case "inactive": return fetchInactiveStudents(courseId, page, size);
    case "no-declarations": return fetchStudentsWithoutDeclaration(courseId, page, size);
  }
}

// Main paginated load
async function loadStudents() {
  if (isSearchMode.value) return; // disable during search

  try {
    isLoading.value = true;

    const data = await loadFromService(props.courseId, page.value, size.value);

    // Ensure page never exceeds last page (URL fixes)
    if (data.content.length === 0 && data.totalPages > 0 && page.value >= data.totalPages) {
      page.value = data.totalPages - 1;

      await router.replace({
        query: { ...route.query, [pageParam.value]: page.value + 1 }
      });

      const fixedData = await loadFromService(props.courseId, page.value, size.value);
      internalStudents.value = fixedData.content;
      totalPages.value = fixedData.totalPages;
    } else {
      internalStudents.value = data.content;
      totalPages.value = data.totalPages;
    }

  } catch (err) {
    console.error("Viga tudengite laadimisel:", err);
    internalStudents.value = [];
    totalPages.value = 0;
  } finally {
    isLoading.value = false;
  }
}

// Expose method for parent refreshAll()
defineExpose({ loadStudents });

// When row updates (declaration changed etc.)
function onRowUpdated() {
  loadStudents();
  emit("updated");
}

// Pagination change watcher
watch(page, async (newPage, oldPage) => {
  if (isSearchMode.value) return;

  if (isNaN(newPage) || newPage < 0) {
    page.value = oldPage;
    return;
  }

  await router.replace({
    query: { ...route.query, [pageParam.value]: newPage + 1 }
  });

  loadStudents();
});

// Reload on type/course change
watch(() => [props.courseId, props.type], () => {
  page.value = 0;

  router.replace({
    query: { ...route.query, [pageParam.value]: 1 }
  });

  loadStudents();
});

// Initial load
onMounted(loadStudents);
</script>

<style scoped>
.student-table {
  margin: 20px auto;
  width: 100%;
  max-width: 900px;
}
.data-table {
  width: 100%;
  border-collapse: collapse;
}
.data-table th,
.data-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}
.loading {
  text-align: center;
  padding: 15px;
  font-size: 18px;
  color: #555;
}
</style>
