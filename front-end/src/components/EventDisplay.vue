<script setup>
import { ref } from 'vue';
import { defineProps } from 'vue';

import Title_2 from "@/components/Title_2.vue";
import ModalForm from "@/components/ModalForm.vue";
const emit = defineEmits([ 'close'])
const props = defineProps({
  event: {
    type: Object,
    required: true
  },
  cancelText: {
    type: String,
    default: 'Annuler'
  }
});

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString('fr-FR');
}
const handleCancel = () => {
  emit('close')
}


</script>

<template>


  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div
        class="flex flex-col justify-between border-2 border-green-600 rounded-lg p-4 bg-white text-green-600
           w-full max-w-[500px] min-w-[500px] max-h-[400px] min-h-[400px] overflow-y-auto"
    >
      <Title_2 class="font-bold text-lg">Titre: {{ event.title }}</Title_2>
      <p><span class="font-bold">Description:</span> {{ event.description }}</p>
      <p><span class="font-bold">Plante:</span> {{ event.plant }}</p>
      <p><span class="font-bold">Date:</span> {{ formatDate(event.start) }}</p>
      <p><span class="font-bold">Ce répète tous les:</span> {{ event.cycle }} <span class="font-bold">jours </span></p>
      <button
          type="button"
          @click="handleCancel"
          class="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mt-2"
      >
        {{ cancelText }}
      </button>
    </div>
  </div>

</template>

<style scoped>

</style>