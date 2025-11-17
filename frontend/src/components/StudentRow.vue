<template>
  <div class="actions">
    <button class="btn--link" @click="navigateToGrades">Vaata hindeid</button>

    <button
        v-if="type === 'with-declarations'"
        class="btn--link"
        @click="setInactive"
    >
      Märgi inaktiivseks
    </button>

    <button
        v-else-if="type === 'inactive'"
        class="btn--link"
        @click="setActive"
    >
      Märgi aktiivseks
    </button>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { markStudentActive, markStudentInactive } from "@/services/studentService.js";

const props = defineProps({
  student: { type: Object, required: true },
  courseId: { type: Number, required: true },
  type: String
});

const emit = defineEmits(["updated"]);
const router = useRouter();

function navigateToGrades() {
  if (props.courseId && props.student?.id) {
    router.push(`/courses/${props.courseId}/students/${props.student.id}/grades`);
  } else {
    console.error("Unable to navigate: missing course or student ID", props);
  }
}

async function setInactive() {
  try {
    await markStudentInactive(props.courseId, props.student.id);
    emit("updated");
  } catch (e) {
    console.error(e);
  }
}

async function setActive() {
  try {
    await markStudentActive(props.courseId, props.student.id);
    emit("updated");
  } catch (e) {
    console.error(e);
  }
}
</script>

<style scoped>
.actions {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
</style>