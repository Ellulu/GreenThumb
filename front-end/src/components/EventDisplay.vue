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


  <div     class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50"
  >
    <div
        class="flex flex-col justify-between border-2 border-green-600 rounded-lg p-4 bg-white text-green-600
               mx-auto w-[90%] max-w-[500px] h-auto max-h-[400px] overflow-y-auto"
    >
      <Title_2 class="font-bold text-lg">Titre: {{ event.title }}</Title_2>
      <p><span class="font-bold">Description:</span> {{ event.description }}</p>
      <p><span class="font-bold">Plante:</span> {{ event.plant }}</p>
      <p><span class="font-bold">Date:</span> {{ formatDate(event.start) }}</p>
      <p v-if="event.cycle === 0">
        <span class="font-bold">Cet événement ne se répète pas.</span>
      </p>
      <p v-else>
        <span class="font-bold">Se répète tous les:</span> {{ event.cycle }} <span class="font-bold">jours</span>
      </p>
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