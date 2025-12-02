<template>
  <div class="grid-container">
    <header class="header-bar">
      <div class="header-content">
        <button
            v-if="showCourseNav"
            @click="mobileNavOpen = !mobileNavOpen"
            class="mobile-menu-button"
            aria-label="Toggle navigation"
        >
          <span class="hamburger-icon">
            <span></span>
            <span></span>
            <span></span>
          </span>
        </button>

        <h1 class="header-logo-text">PROGRESSLINE</h1>
        <nav>
          <RouterLink to="/">Info</RouterLink>
          <RouterLink to="/courses">Kursused</RouterLink>
          <LogoutButton v-if="isLoggedIn" />
        </nav>
      </div>
    </header>

    <div class="main-layout">
      <div
          v-if="showCourseNav && mobileNavOpen"
          class="mobile-overlay"
          @click="mobileNavOpen = false"
      ></div>

      <CourseTreeNav
          v-if="showCourseNav"
          :class="{ 'mobile-open': mobileNavOpen }"
          @close="mobileNavOpen = false"
      />

        <RouterView />

      <PopupModal
          :visible="modalVisible"
          :blockClose="true"
      >
        <h2>Logi sisse</h2>
        <p>Sellele lehele pääsemiseks pead olema sisse logitud.</p>
        <LoginButton />
      </PopupModal>

    </div>
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router'
import LoginButton from "@/components/LoginButton.vue";
import PopupModal from "@/components/PopupModal.vue";
import LogoutButton from "@/components/LogoutButton.vue";
import CourseTreeNav from "@/components/CourseTreeNav.vue";
import { useAuth } from "@/composables/useAuth";
import { computed, ref } from "vue";

const auth = useAuth();
const route = useRoute();

const isLoggedIn = computed(() => !!auth.account.value);
const modalVisible = auth.showForcedLoginModal;

const showCourseNav = computed(() => {
  return route.params.courseId !== undefined;
});

const mobileNavOpen = ref(false);
</script>