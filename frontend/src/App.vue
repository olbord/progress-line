<template>
  <header class="header-bar">
    <div class="header-content">
      <h1 class="header-logo-text">PROGRESSLINE</h1>
      <nav>
        <RouterLink to="/">Algusesse</RouterLink>
        <RouterLink to="/about">Info</RouterLink>
        <RouterLink to="/courses">Kursused</RouterLink>
        <LogoutButton v-if="isLoggedIn" />
      </nav>
    </div>
  </header>

  <div class="main-layout">
    <CourseTreeNav v-if="showCourseNav" />
    <div class="main-content">
      <RouterView />
    </div>
  </div>

  <PopupModal
      :visible="modalVisible"
      :blockClose="true"
  >
    <h2>Logi sisse</h2>
    <p>Sellele lehele pääsemiseks pead olema sisse logitud.</p>
    <LoginButton />
  </PopupModal>
</template>

<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router'
import LoginButton from "@/components/LoginButton.vue";
import PopupModal from "@/components/PopupModal.vue";
import LogoutButton from "@/components/LogoutButton.vue";
import CourseTreeNav from "@/components/CourseTreeNav.vue";
import { useAuth } from "@/composables/useAuth";
import { computed } from "vue";

const auth = useAuth();
const route = useRoute();

const isLoggedIn = computed(() => !!auth.account.value);
const modalVisible = auth.showForcedLoginModal;

const showCourseNav = computed(() => {
  return route.params.courseId !== undefined;
});
</script>