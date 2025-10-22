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
        <RouterLink to="/grades">Vaata tudengi hindeid</RouterLink>
        <RouterLink to="/courses/35065/students/1">Lisa kommentaar</RouterLink>
        <RouterLink to="/course-analytics">Kursuse analüüs</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>