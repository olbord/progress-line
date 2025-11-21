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

  <RouterView />

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
import { RouterLink, RouterView } from 'vue-router'
import LoginButton from "@/components/LoginButton.vue";
import PopupModal from "@/components/PopupModal.vue";
import LogoutButton from "@/components/LogoutButton.vue";
import { useAuth } from "@/composables/useAuth";
import { computed } from "vue";

const auth = useAuth();

const isLoggedIn = computed(() => !!auth.account.value);
const modalVisible = auth.showForcedLoginModal;

</script>