<template>
  <transition name="fade">
    <div v-if="visible" class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <button class="modal-close" @click="close">✕</button>
        <slot />
      </div>
    </div>
  </transition>
</template>

<script setup>
const props = defineProps({
  visible: { type: Boolean, required: true }
})
const emit = defineEmits(['close'])
function close() {
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

/* minimal overlay layout (no styling beyond essentials) */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 1rem;
  border-radius: 8px;
  max-height: 90vh;
  overflow-y: auto;
  min-width: 60%;
}

.modal-close {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
}

</style>
