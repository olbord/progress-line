<template>
  <transition name="fade">
    <div v-if="visible" class="modal-overlay" @click.self="tryClose">
      <div class="modal-content-wrapper">
      <div class="modal-content">
        <button class="close-btn" @click="tryClose">×</button>
        <slot />
      </div>
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

.close-btn {
  position: absolute;
  top: 8px;
  right: 16px;
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
  line-height: 1;
  color: #333;
}

.close-btn:hover {
  color: #000;
}

.modal-overlay {
  display: flex;
  justify-content: center;
  align-items: center;

  padding: 24px;
  box-sizing: border-box;
}

</style>
