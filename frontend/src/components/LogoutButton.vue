<template>
  <button @click="logout" class="logout-btn">
    Logout
  </button>
</template>

<script setup>
import { msalInstance } from "@/msal/msalInstance";
import { useAuth } from "@/composables/useAuth";

const { setAccount } = useAuth();

const logout = async () => {
  try {
    const account = msalInstance.getActiveAccount();
    if (account) {
      await msalInstance.logoutRedirect({ account });
      setAccount(null);
    }
  } catch (err) {
    console.error("Logout error:", err);
  }
};
</script>
