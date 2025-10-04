<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">Kõik tudengid</h1>

    <StudentList title="Aktiivsed deklaratsiooniga tudengid" :students="studentsWithDeclaration" />
    <StudentList title="Mitteaktiivsed tudengid" :students="inactiveStudents" />
    <StudentList title="Ilma deklaratsioonita tudengid" :students="studentsWithoutDeclaration" />
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
