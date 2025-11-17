<template>
  <div class="student-table">
    <h2>{{ title }}</h2>

    <div v-if="isLoading" class="loading">
      Laadimine...
    </div>

    <table v-if="!isLoading && students.length > 0" class="data-table">
      <thead>
      <tr>
        <th class="col-medium">Eesnimi / Perekonnanimi</th>
        <th class="col-wide">Email</th>
        <th class="col-narrow">Punktide summa</th>
        <th class="col-narrow">Tegevus</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="student in students" :key="student.id">
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

    <div v-else-if="!isLoading && students.length === 0">
    Tudengeid ei leitud
    </div>

    <Pagination
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

const isLoading = ref(false);
const students = ref([]);
const size = ref(10);
const totalPages = ref(0);

const emit = defineEmits(["updated"]);

function onRowUpdated() {
  loadStudents();
  emit("updated");
}

const props = defineProps({
  title: { type: String, required: true },
  courseId: { type: Number, required: true },
  type: {
    type: String,
    required: true,
    validator: (v) =>
        ["with-declarations", "inactive", "no-declarations"].includes(v)
  }
});

const pageParam = computed(() => {
  switch (props.type) {
    case "with-declarations":
      return "withDeclPage";
    case "inactive":
      return "inactivePage";
    case "no-declarations":
      return "noDeclPage";
  }
});

const page = ref(
    route.query[pageParam.value]
        ? Number(route.query[pageParam.value]) - 1
        : 0
);

function loadFromService(courseId, page, size) {
  switch (props.type) {
    case "with-declarations":
      return fetchStudentsWithDeclaration(courseId, page, size);
    case "inactive":
      return fetchInactiveStudents(courseId, page, size);
    case "no-declarations":
      return fetchStudentsWithoutDeclaration(courseId, page, size);
  }
}

async function loadStudents() {
  try {
    isLoading.value = true;

    const data = await loadFromService(
        props.courseId,
        page.value,
        size.value
    );

    students.value = data.content;
    totalPages.value = data.totalPages;
  } catch (err) {
    console.error("Viga tudengite laadimisel:", err);
    students.value = [];
    totalPages.value = 0;
  } finally {
    isLoading.value = false;
  }
}

defineExpose({ loadStudents });

watch(page, (newPage) => {
  router.replace({
    query: {
      ...route.query,
      [pageParam.value]: newPage + 1
    }
  });

  loadStudents();
});

watch(() => [props.courseId, props.type], () => {

  page.value = 0;

  router.replace({
    query: {
      ...route.query,
      [pageParam.value]: 1
    }
  });

  loadStudents();
});

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
