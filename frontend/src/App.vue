<template>
  <header class="header-bar">

    <div class="header-content">

      <h1 class="header-logo-text">PROGRESSLINE</h1>

      <nav>
        <RouterLink to="/">Algusesse</RouterLink>
        <RouterLink to="/about">Info</RouterLink>
        <RouterLink to="/courses">Kursused</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>