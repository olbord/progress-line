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
        <RouterLink to="/students">Kõik tudengid</RouterLink>
        <RouterLink to="/grades">Vaata tudengi hindeid</RouterLink>
        <RouterLink to="/comment/students/1">Lisa kommentaar</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>