<script setup>
import { ref, onMounted} from 'vue';
import {useRoute} from 'vue-router';
import apiClient from '@/services/api'
import CommentForm from "./CommentForm.vue";

const user = ref({})
const route = useRoute();
const studentId = route.params.id

const fetchStudent = async () => {
  const response = await apiClient.get(`students/${studentId}`)
  user.value = response.data
}
onMounted(() => {fetchStudent()})
</script>

<template>
  <main class="container container--narrow">
  <div class="student-info-card">
    <h1>Lisa kommentaar</h1>

    <div v-if="user.userId">
      <table>
        <tbody>
        <tr>
          <td><h2>{{ user.firstName }} {{ user.lastName }}</h2></td>
        </tr>
        <tr>
          <td>{{ user.email }}</td>
        </tr>
        </tbody>
      </table>
      <CommentForm :studentId="parseInt(user.userId)" />
    </div>
    <div v-else>
      <p>Laen tudengi andmeid...</p>
    </div>
  </div>
  </main>
</template>

<style scoped>

table {
  width: 100%;
  margin-bottom: 1rem;
}
</style>

