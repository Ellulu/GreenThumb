<script setup>
import { ref } from 'vue';
import NoteForm from '../components/NoteForm.vue';

const showForm = ref(false);

const toggleForm = () => {
  showForm.value = !showForm.value;
};

const handleFormSubmitted = () => {
  showForm.value = false;
};
</script>

<template>
  <main>
    <button @click="toggleForm" class="absolute top-0 right-0 p-0">
      <img  src="../assets/carnet-de-notes.png" class="w-1/6 object-cover " alt="Description de l'image">
    </button>
    <NoteForm v-if="showForm" @formSubmitted="handleFormSubmitted" />
  </main>
</template>