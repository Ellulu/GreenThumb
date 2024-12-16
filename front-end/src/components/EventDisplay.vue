<script setup>
import { ref } from 'vue';
import { defineProps } from 'vue';

import Title_2 from "@/components/Title_2.vue";
import ModalForm from "@/components/ModalForm.vue";
const emit = defineEmits([ 'cancel'])
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


const handleCancel = () => {
  emit('cancel')
}


</script>

<template>


 <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
   <Title_2>event.title</Title_2>
    <span> event.description</span>
    <span>event.plant.nom</span>
   <span>event.date</span>
   <button type="button" @click="handleCancel" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded mr-2">
     {{ cancelText }}
   </button>
 </div>


</template>

<style scoped>

</style>