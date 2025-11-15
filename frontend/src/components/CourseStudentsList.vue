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
        title="Aktiivsed tudengid"
        type="with-declarations"
        :courseId="Number(courseId)"
    />

    <StudentTable
        title="Mitteaktiivsed tudengid"
        type="inactive"
        :courseId="Number(courseId)"
    />

    <StudentTable
        title="Ilma deklaratsioonita tudengid"
        type="no-declarations"
        :courseId="Number(courseId)"
    />

  </div>

</template>
