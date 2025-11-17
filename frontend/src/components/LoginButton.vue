<template>
  <button @click="login" class="login-btn">
    Login with Microsoft
  </button>
</template>

<script setup>
import { msalInstance } from "@/msal/msalInstance";

const login = async () => {
  try {
    await msalInstance.loginRedirect();
  } catch (error) {
    console.error("Login error:", error);
  }
};
</script>

<style scoped>

</style>
