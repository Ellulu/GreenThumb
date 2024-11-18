<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  modelValue: {
    type: [String, Number],
    required: true
  },
  options: {
    type: Array,
    required: true
  },
  required: {
    type: Boolean,
    default: false
  },
  errorMessage: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const hasError = computed(() => props.errorMessage !== '')
</script>

<template>
  <div class="flex flex-col gap-2">
    <label :for="name" class="text-sm font-medium text-gray-500">
      {{ name }}<span v-if="required" class="ml-1 text-red-500">*</span>
    </label>
    <select
      :id="name"
      :name="name"
      :required="required"
      class="p-2 rounded-md border border-slate-300 outline-green-600 w-full"
      :class="{ 'outline-red-500': hasError }"
      :value="modelValue"
      @change="$emit('update:modelValue', $event.target.value)"
    >
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.label }}
      </option>
    </select>
    <p v-if="hasError" class="text-red-500 text-sm">{{ errorMessage }}</p>
  </div>
</template>