<template>
  <div class="bg-amber-50 min-h-screen mr-5">
    <header class="bg-green-600 rounded-lg bg-primary text-white p-4 shadow-md">
      <h1 class="text-2xl font-bold">Flower Notes</h1>
    </header>

    <main class="container mx-auto p-4">
      <button @click="showAddModal = true" class="bg-green-600 bg-primary hover:bg-primary-dark text-white font-bold py-2 px-4 rounded mb-4">
        + Nouvelle Note
      </button>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="(note, index) in notes" :key="index" class="bg-amber-50 p-4 rounded shadow border border-green-600">
          <h2 class="text-xl font-semibold text-green-600 mb-2">{{ note.title }}</h2>
          <p class="text-gray-600 line-clamp-3">{{ note.content }}</p>
          <div class="flex space-x-2">
            <!-- Bouton Voir plus -->
            <button @click="openNoteModal(note)" class="bg-green-600 bg-primary hover:bg-primary-dark text-white font-bold py-2 px-4 rounded mb-4">Voir plus</button>
            <!-- Bouton Modifier -->
            <button @click="openEditModal(note, index)" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mb-4">Modifier</button>
          </div>
        </div>
      </div>
    </main>

    <!-- Modal pour ajouter une nouvelle note -->
    <div v-if="showAddModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
      <div class="bg-amber-50 p-8 rounded-lg shadow-xl w-full max-w-md">
        <h2 class="text-2xl font-bold text-green-600 mb-4">Ajouter Une Note</h2>
        <form @submit="handleSubmit">
          <div class="mb-4">
            <label for="noteTitle" class="block text-gray-700 font-bold mb-2">Titre</label>
            <input v-model="title" type="text" id="noteTitle" name="noteTitle" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-primary" required>
          </div>
          <div class="mb-4">
            <label for="noteContent" class="block text-gray-700 font-bold mb-2">Contenu</label>
            <textarea v-model="content" id="noteContent" name="noteContent" rows="4" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-primary" required></textarea>
          </div>
          <div class="flex justify-end">
            <button @click="showAddModal = false" type="button" class="bg-red-500 hover:bg-gray-400 text-white font-bold py-1 px-3 rounded mr-2">
              Annuler
            </button>
            <button type="submit" class="bg-green-600 hover:bg-primary-dark text-white font-bold py-1 px-3 rounded">
              Ajouter Note
            </button>

          </div>
        </form>
      </div>
    </div>

    <!-- Modal pour afficher une note -->
    <div v-if="showNoteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
      <div class="bg-amber-50 p-8 rounded-lg shadow-xl w-full max-w-md max-h-screen overflow-y-auto">
        <h2 class="text-2xl font-bold text-primary mb-4">{{ selectedNote.title }}</h2>
        <p class="text-gray-600">{{ selectedNote.content }}</p>
        <div class="flex justify-end mt-4">
          <button @click="showNoteModal = false" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
            Fermer
          </button>
        </div>
      </div>
    </div>

    <!-- Modal pour modifier une note -->
    <div v-if="showEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
      <div class="bg-amber-50 p-8 rounded-lg shadow-xl w-full max-w-md">
        <h2 class="text-2xl font-bold text-green-600 mb-4">Modifier la Note</h2>
        <form @submit="handleEditSubmit">
          <div class="mb-4">
            <label for="editTitle" class="block text-gray-700 font-bold mb-2">Titre</label>
            <input v-model="editTitle" type="text" id="editTitle" name="editTitle" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-primary" required>
          </div>
          <div class="mb-4">
            <label for="editContent" class="block text-gray-700 font-bold mb-2">Contenu</label>
            <textarea v-model="editContent" id="editContent" name="editContent" rows="4" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-primary" required></textarea>
          </div>
          <div class="flex justify-end">
            <button @click="showEditModal = false" type="button" class="bg-red-500 hover:bg-gray-400 text-white font-bold py-2 px-4 rounded mr-2">
              Annuler
            </button>
            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
              Sauvegarder
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const title = ref('');
const content = ref('');
const editTitle = ref('');
const editContent = ref('');
const selectedNote = ref({});
const selectedNoteIndex = ref(null);

const responseMessage = ref('');
const showAddModal = ref(false);
const showNoteModal = ref(false);
const showEditModal = ref(false);

//charger les notes ici
const notes = ref([
  { title: 'Rose Care', content: 'Water roses regularly and ensure they get enough sunlight.' },
  { title: 'Sunflower Planting', content: 'Plant sunflower seeds in moist soil and ensure they get plenty of sunlight.' },
  { title: 'Tulip Care', content: 'Water tulips every week and remove weeds from the surrounding area.' },
  { title: 'Detailed Tulip Care', content: 'Water the tulips consistently, making sure not to overwater...' }
]);


const handleSubmit = async (e) => {
  e.preventDefault();

  const newNote = {
    title: title.value,
    content: content.value
  };

  try {
    const response = await fetch('http://localhost:8080/api/notes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newNote)
    });

    if (response.ok) {
      notes.value.push(newNote);
      title.value = '';
      content.value = '';
      showAddModal.value = false;
    } else {
      console.error("Erreur lors de l'enregistrement.");
    }
  } catch (error) {
    console.error("Erreur de connexion au serveur:", error);
  }
};

// Méthode pour ouvrir la modale avec la note sélectionnée
const openNoteModal = (note) => {
  selectedNote.value = note;
  showNoteModal.value = true;
};

// Méthode pour ouvrir la modale de modification avec la note sélectionnée
const openEditModal = (note, index) => {
  editTitle.value = note.title;
  editContent.value = note.content;
  selectedNoteIndex.value = index;
  showEditModal.value = true;
};

// Méthode pour sauvegarder les modifications
const handleEditSubmit = async (e) => {
  e.preventDefault();

  const updatedNote = {
    title: editTitle.value,
    content: editContent.value
  };

  notes.value[selectedNoteIndex.value] = updatedNote;
  showEditModal.value = false;
};
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
