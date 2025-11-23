<template>
  <div class="student-table">
    <h2>{{ title }}</h2>

    <div v-if="isLoading" class="loading">
      Laadimine...
    </div>

    <table v-if="!isLoading && internalStudents.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-medium">
      <span @click="toggleSort('firstName')" class="sortable">
        Eesnimi
        <span v-if="sort.startsWith('firstName')">({{ sortDirection }})</span>
      </span>
          /
          <span @click="toggleSort('lastName')" class="sortable">
        Perekonnanimi
        <span v-if="sort.startsWith('lastName')">({{ sortDirection }})</span>
      </span>
        </th>

        <th class="col-wide" @click="toggleSort('email')">
          Email
          <span v-if="sort.startsWith('email')">({{ sortDirection }})</span>
        </th>

        <th class="col-narrow" @click="toggleSort('totalPoints')">
          Punktide summa
          <span v-if="sort.startsWith('totalPoints')">({{ sortDirection }})</span>
        </th>

        <th class="col-narrow">Tegevus</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="student in internalStudents" :key="student.id">
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

    <div v-else-if="!isLoading && internalStudents.length === 0">
      {{ props.type === 'with-declarations' && props.search ? "Otsingu tulemused puuduvad" : "Tudengeid ei leitud" }}
    </div>

    <Pagination
        v-if="!isLoading && totalPages > 1"
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
  search: {
    type: String,
    default: ""
  }
});

// STATE
const isLoading = ref(false);
const internalStudents = ref([]);
const size = ref(10);
const totalPages = ref(0);

// SORT
const sort = ref("firstName,asc");
const sortDirection = computed(() => sort.value.endsWith("asc") ? "↑" : "↓");

// QUERY PAGE
const pageParam = computed(() => {
  switch (props.type) {
    case "with-declarations": return "withDeclPage";
    case "inactive": return "inactivePage";
    case "no-declarations": return "noDeclPage";
  }
});

const rawPage = Number(route.query[pageParam.value]);
const page = ref(!isNaN(rawPage) && rawPage > 0 ? rawPage - 1 : 0);

// SERVICE
function loadFromService(courseId, page, size, search, sort) {
  switch (props.type) {
    case "with-declarations":
      return fetchStudentsWithDeclaration(courseId, page, size, search, sort);
    case "inactive":
      return fetchInactiveStudents(courseId, page, size);
    case "no-declarations":
      return fetchStudentsWithoutDeclaration(courseId, page, size);
  }
}

// MAIN LOADER
async function loadStudents() {
  try {
    isLoading.value = true;

    const data = await loadFromService(
        props.courseId,
        page.value,
        size.value,
        props.search,
        sort.value
    );

    internalStudents.value = data.content ?? [];
    totalPages.value = data.totalPages ?? 1;

  } catch (err) {
    console.error("Viga tudengite laadimisel:", err);
    internalStudents.value = [];
    totalPages.value = 0;
  } finally {
    isLoading.value = false;
  }
}

// SORT CLICK HANDLER
function toggleSort(field) {
  const [currentField, direction] = sort.value.split(",");

  if (currentField === field) {
    sort.value = `${field},${direction === "asc" ? "desc" : "asc"}`;
  } else {
    sort.value = `${field},asc`;
  }

  page.value = 0;
  updateQuery();
  loadStudents();
}

// UPDATE URL
function updateQuery() {
  router.replace({
    query: {
      ...route.query,
      [pageParam.value]: page.value + 1
    }
  });
}

// EXPOSE FOR PARENT
defineExpose({ loadStudents });

// ROW UPDATED
function onRowUpdated() {
  loadStudents();
  emit("updated");
}

// WATCH PAGE CHANGE
watch(page, () => {
  updateQuery();
  loadStudents();
});

// WATCH SEARCH CHANGE (ONLY ACTIVE TABLE)
watch(() => props.search, () => {
  if (props.type === "with-declarations") {
    page.value = 0;
    loadStudents();
  }
});

// WATCH COURSE OR TYPE CHANGE
watch(() => [props.courseId, props.type], () => {
  page.value = 0;
  updateQuery();
  loadStudents();
});

// INITIAL LOAD
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
.data-table th {
  cursor: pointer;
  user-select: none;
}
.loading {
  text-align: center;
  padding: 15px;
  font-size: 18px;
  color: #555;
}
</style>
