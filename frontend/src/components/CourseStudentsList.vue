<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import StudentList from "@/components/StudentList.vue";
import { fetchAllStudentsData } from "@/services/studentService.js";

const route = useRoute();
const courseId = route.params.courseId;
const courseName = route.query.courseName

const studentsWithDeclaration = ref([]);
const inactiveStudents = ref([]);
const studentsWithoutDeclaration = ref([]);

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

    <StudentList title="Aktiivsed tudengid" :students="studentsWithDeclaration" />
    <StudentList title="Mitteaktiivsed tudengid" :students="inactiveStudents" />
    <StudentList title="Ilma deklaratsioonita tudengid" :students="studentsWithoutDeclaration" />
  </div>
</template>
