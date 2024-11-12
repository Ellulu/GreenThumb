<template>
  <input
      type="date"
      :value="modelValue"
      @input="updateDate"
      class="date-input"
  />
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:modelValue']);

function updateDate(event) {
  emit('update:modelValue', event.target.value);
}
</script>

<style scoped>
.date-input {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.25rem;
  font-size: 1rem;
  color: #374151;
}
</style>
