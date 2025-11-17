<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { fetchAllStudentsData } from "@/services/studentService.js";
import StudentTable from "@/components/StudentTable.vue";

const route = useRoute();
const courseId = route.params.courseId;
const courseName = route.query.courseName

const studentsWithDeclaration = ref([]);
const inactiveStudents = ref([]);
const studentsWithoutDeclaration = ref([]);

const withDeclRef = ref(null);
const inactiveRef = ref(null);
const withoutDeclRef = ref(null);

function refreshAll() {
  withDeclRef.value?.loadStudents();
  inactiveRef.value?.loadStudents();
  withoutDeclRef.value?.loadStudents();
}

onMounted(async () => {
  try {
    const { withDecl, inactive, withoutDecl } = await fetchAllStudentsData(courseId);
    studentsWithDeclaration.value = withDecl;
    inactiveStudents.value = inactive;
    studentsWithoutDeclaration.value = withoutDecl;
  } catch (err) {
    console.error("Error fetching students:", err);
  }
});
</script>

<template>
  <div class="container container--medium">
    <h1>{{ courseName }}</h1>

    <StudentTable
        ref="withDeclRef"
        title="Aktiivsed tudengid"
        type="with-declarations"
        :courseId="Number(courseId)"
        @updated="refreshAll"
    />

    <StudentTable
        ref="inactiveRef"
        title="Mitteaktiivsed tudengid"
        type="inactive"
        :courseId="Number(courseId)"
        @updated="refreshAll"
    />

    <StudentTable
        ref="withoutDeclRef"
        title="Ilma deklaratsioonita tudengid"
        type="no-declarations"
        :courseId="Number(courseId)"
        @updated="refreshAll"
    />

  </div>
</template>
