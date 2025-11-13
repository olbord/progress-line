<template>
  <div class="login-container">
    <div v-if="!account">
      <button @click="login" class="login-btn">Login with Microsoft</button>
    </div>
    <div v-else>
      <p>Welcome, {{ account.username }}</p>
      <button @click="logout" class="logout-btn">Logout</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { msalInstance, initializeMsal } from "/msal/msalInstance";
import { loginRequest } from "/msal/msalConfig";
import axios from "axios";

const account = ref(null);

const login = async () => {
  try {
    await initializeMsal();
    await msalInstance.loginRedirect(loginRequest);
  } catch (error) {
    console.error("Login error:", error);
  }
};

const logout = async () => {
  try {
    const activeAccount = msalInstance.getActiveAccount();
    if (activeAccount) {
      await msalInstance.logoutRedirect({account: activeAccount});
      account.value = null;
    }
  } catch (error) {
    console.error("Logout error:", error);
  }
};

const getToken = async () => {
  try {
    const activeAccount = msalInstance.getActiveAccount();
    if (!activeAccount) throw new Error("No active account");

    const tokenResponse = await msalInstance.acquireTokenSilent({
      ...loginRequest,
      account: activeAccount,
    });
    return tokenResponse.accessToken;
  } catch (error) {
    console.warn("Silent token acquisition failed, falling back to redirect", error);
    await msalInstance.acquireTokenRedirect(loginRequest);
  }
};

// for testing
const callApi = async () => {
  try {
    const token = await getToken();
    const response = await axios.get("/api/courses/35065/inactive", {
      headers: { Authorization: `Bearer ${token}` },
    });
    console.log("API response:", response.data);
  } catch (error) {
    console.error("API call failed:", error);
  }
};

onMounted(async () => {
  await initializeMsal();
  const response = await msalInstance.handleRedirectPromise();
  if (response) {
    msalInstance.setActiveAccount(response.account);
    account.value = response.account;
  } else {
    const active = msalInstance.getActiveAccount();
    if (active) account.value = active;
  }
});
</script>
