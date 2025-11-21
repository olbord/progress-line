<template>
  <button @click="login" class="login-btn">
    Logi sisse oma @taltech.ee kontoga
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
