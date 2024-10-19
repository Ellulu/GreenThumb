<template>

  <div class="absolute inset-0 flex items-center justify-center backdrop-blur-md bg-black bg-opacity-50 z-10">
    <div class=" flex flex-col flex-nowrap">

      <div class="flex flex-col justify-center items-center bg-green-600 rounded-lg p-6 ">
        <button @click="close" class="ml-64 p-1 bg-red-500 rounded-lg text-white flex justify-center">
          X
        </button>

        <form @submit.prevent="handleSubmit">

          <h1 class="text-white text-4xl font-bold text-center">Nouvelle Note</h1>
          <div>
            <label class="block text-2xl font-medium text-white mt-4 text-center" for="title">Titre</label>
            <input class="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200 ease-in-out" type="text" v-model="title" id="title" required />
          </div>
          <div>
            <label class="block text-2xl font-medium text-white mt-4 text-center" for="content">Contenu</label>
            <textarea class="w-full h-32 p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200 ease-in-out" v-model="content" id="content" required></textarea>
          </div>
          <div class="flex justify-center">
            <button class="border border-white focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800" type="submit">Enregistrer</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref ,defineEmits} from 'vue';

const title = ref('');
const content = ref('');
const responseMessage = ref('');
const emit = defineEmits(['formSubmitted']);

const close = () => {
  emit('formSubmitted');
}

const handleSubmit = async () => {
  const data = {
    title: title.value,
    content: content.value
  };

  try {
    const response = await fetch('http://localhost:8080/api/notes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      responseMessage.value = "Article enregistré avec succès!";
      emit('formSubmitted');

    } else {
      responseMessage.value = "Erreur lors de l'enregistrement.";
      emit('formSubmitted');

    }
  } catch (error) {
    responseMessage.value = "Erreur de connexion au serveur.";
    emit('formSubmitted');

    console.error("Erreur :", error);
  }
}
</script>