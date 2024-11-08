<script setup>
import { ref } from 'vue'


const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  submitText: {
    type: String,
    default: 'Soumettre'
  },
  cancelText: {
    type: String,
    default: 'Annuler'
  }
})

const emit = defineEmits(['submit', 'cancel'])

const formData = ref({})

const handleSubmit = () => {
  emit('submit', formData.value)
}

const handleCancel = () => {
  emit('cancel')
}
</script>

<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-amber-50 rounded-lg p-8 w-full max-w-md">
      <h2 v-if="title" class="text-2xl font-bold mb-6">{{ title }}</h2>
      <form @submit.prevent="handleSubmit">
        <slot :formData="formData">
        </slot>
        <div class="flex justify-end mt-6">
          <button type="button" @click="handleCancel" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded mr-2">
            {{ cancelText }}
          </button>
          <button type="submit" class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
            {{ submitText }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>