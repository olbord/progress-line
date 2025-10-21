<template>
  <div class="container container--medium">
    <h1>Konkreetse kursuse tudengid</h1>

    <StudentList title="Aktiivsed tudengid" :students="studentsWithDeclaration" />
    <StudentList title="Mitteaktiivsed tudengid" :students="inactiveStudents" />
    <StudentList title="Ilma deklaratsioonita tudengid" :students="studentsWithoutDeclaration" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import StudentList from "@/components/StudentList.vue";
import { fetchAllStudentsData } from "@/services/studentService";

const route = useRoute();
const courseId = route.params.courseId;

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
