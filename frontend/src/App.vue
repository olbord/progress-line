<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <header class="header-bar">


    <div class="header-content">
      <h1>ProgressLine</h1>

      <nav>
        <RouterLink to="/">Algusesse</RouterLink>
        <RouterLink to="/about">Info</RouterLink>
        <RouterLink to="/courses">Kursused</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>