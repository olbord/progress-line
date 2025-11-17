<template>
  <transition name="fade">
    <div v-if="visible" class="modal-overlay" @click.self="tryClose">
      <div class="modal-content">
        <slot />
      </div>
    </div>
  </transition>
</template>

<script setup>
const props = defineProps({
  visible: Boolean,
  blockClose: Boolean
})
const emit = defineEmits(['close'])

function tryClose() {
  if (props.blockClose) return
  emit('close')
}
</script>

<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

</style>
