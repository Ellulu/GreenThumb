<script setup>
const props = defineProps({
    modelValue: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'text'
    },
    placeholder: {
      type: String,
      default: ''
    },
    required: {
      type: Boolean,
      default: false
    }
})
</script>

<template>
 <div class="flex flex-col gap-2">
    <label :for="name" class="text-sm font-medium text-gray-500">{{ name }}</label>
    <input :type="type" :placeholder="placeholder" :required="required" class="p-2 rounded-md border border-slate-300 outline-green-600"
      :value="modelValue" 
      @input="$emit('update:modelValue', $event.target.value)" />
  </div>
</template>