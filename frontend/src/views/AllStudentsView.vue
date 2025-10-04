<template>
  <div class="container container--medium">
    <h1>Kõik tudengid</h1>

    <StudentList title="Aktiivsed" :students="studentsWithDeclaration" />
    <StudentList title="Mitteaktiivsed" :students="inactiveStudents" />
    <StudentList title="Ilma deklaratsioonita" :students="studentsWithoutDeclaration" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import StudentList from "@/components/StudentList.vue";
import { fetchAllStudentsData } from "@/services/studentService";

const studentsWithDeclaration = ref([]);
const inactiveStudents = ref([]);
const studentsWithoutDeclaration = ref([]);

onMounted(async () => {
  try {
    const { withDecl, inactive, withoutDecl } = await fetchAllStudentsData();
    studentsWithDeclaration.value = withDecl;
    inactiveStudents.value = inactive;
    studentsWithoutDeclaration.value = withoutDecl;
  } catch (err) {
    console.error("Error fetching students:", err);
  }
});
</script>
