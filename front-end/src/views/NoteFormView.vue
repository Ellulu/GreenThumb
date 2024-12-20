<template>
  <div class="bg-white min-h-screen mr-5">
    <Title>Mes notes</Title>

    <main class="container mx-auto p-4">
      <Button @click="showAddModal = true" class="bg-green-600 bg-primary hover:bg-primary-dark text-white font-bold py-2 px-4 rounded mb-4">
        + Nouvelle Note
      </Button>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="(note, index) in notes" :key="index" class="bg-white text-center p-4 rounded shadow border border-green-600">
          <h2 class="text-xl font-semibold text-green-600 mb-3 truncate-title">{{ note.title }}</h2>
          <p class="text-gray-600 line-clamp-3 mb-3" >{{ note.content }}</p>
          <div class="flex justify-between">

            <Button @click="openEditModal(note, index)" class="bg-green-700 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mb-4 flex items-center justify-center">
              <Edit class="inline "/>
            </Button>
            <Button  v-if="note.content.length > maxContentLength" @click="openNoteModal(note)" class="bg-green-600 bg-primary hover:bg-primary-dark text-white font-bold py-2 px-4 rounded mb-4">
              <Plus class="inline "/>
            </Button>
            <Button @click="deleteNoteEvent(note)" class="bg-red-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mb-4">
              <Trash class="inline "/>
            </Button>
          </div>
        </div>
      </div>
    </main>
    <div v-if="showNoteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
      <div class="bg-white p-8 rounded-lg shadow-xl w-full max-w-md max-h-screen">
        <h2 class="text-2xl font-bold text-primary mb-4">{{ selectedNote.title }}</h2>
        <p class="text-gray-600 whitespace-pre-wrap break-words overflow-y-auto max-h-80">
          {{ selectedNote.content }}
        </p>
        <div class="flex justify-end mt-4">
          <Button @click="showNoteModal = false" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
            Fermer
          </Button>
        </div>
      </div>
    </div>

    <ModalForm
        :show="showAddModal"
        title="Ajouter Une Note"
        submitLabel="Ajouter Note"
        @submit="handleSubmit"
        @close="closeModal"
    >
      <template #form-content>
        <div class="mb-4">

          <Input v-model="newNote.title" type="text" id="noteTitle" required name="Titre" maxlength="30">
          </Input>
        </div>
        <div class="mb-4">
          <label for="noteContent" class="block text-gray-700 font-bold mb-2">Contenu</label>
          <textarea v-model="newNote.content" id="noteContent" rows="4" class="w-full px-3 py-2 border border-gray-300 rounded" required></textarea>
        </div>
      </template>
    </ModalForm>
    <ModalForm
        :show="showEditModal"
        title="Éditer Une Note"
        submitLabel="Sauvegarder"
        @submit="handleEditSubmit"
        @close="closeModal"
    >
      <template #form-content>
        <div class="mb-4">
          <label for="editTitle" class="block text-gray-700 font-bold mb-2" >Titre</label>
          <input v-model="editNoteVar.title" type="text" id="editTitle" name="editTitle" class="w-full px-3 py-2 border border-gray-300 rounded" maxlength="30" required>
        </div>
        <div class="mb-4">
          <label for="editContent" class="block text-gray-700 font-bold mb-2">Contenu</label>
          <textarea v-model="editNoteVar.content" id="editContent" name="editContent" rows="4" class="w-full px-3 py-2 border border-gray-300 rounded" required></textarea>
        </div>
      </template>
    </ModalForm>
  </div>


  </div>



</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useNoteStore} from "@/stores/useNoteStore.js";
import ModalForm from "@/components/ModalForm.vue";
import Input from "@/components/Input.vue";
import Button from "@/components/Button.vue";
import { toRefs } from 'vue';

import {Trash } from "lucide-vue-next";
import {Edit } from "lucide-vue-next";
import {Plus } from "lucide-vue-next";

import {Title} from "@/components/index.js";

const noteStore = useNoteStore();
const showAddModal = ref(false);
const showEditModal = ref(false)
const showNoteModal = ref(false);
const selectedNote = ref({});

const maxContentLength = 100
const title = ref('');
const content = ref('');
const id = ref(null);
const {fetchNotes,deleteNote,createNote,editNote} = noteStore;
const { notes } = toRefs(noteStore);
const newNote =ref({
  title: '',
  content:''
})
const editNoteVar = ref({
  title:'',
  content: '',
  id: null,
})
function resetForm() {
newNote.value = {
  title: '',
  content: ''
}

}


function openEditModal(note) {
  editNoteVar.value = note;
  showEditModal.value = true
}

function openNoteModal(note) {

  selectedNote.value = note;
  showNoteModal.value = true;
}

async function deleteNoteEvent(note) {
  try{
      await deleteNote( note.id);
  }catch (error) {
    console.error("Erreur de connexion au serveur:", error);
  }
}

function closeModal() {
  resetForm();
  showAddModal.value = false;
  showEditModal.value = false
  showNoteModal.value = false;
}

const handleEditSubmit = async ()=> {


  try {
    await editNote(editNoteVar.value);



  } catch (error) {
    console.error("Erreur de connexion au serveur:", error);
  }finally
  {
    closeModal();
  }

}


const handleSubmit = async () => {


  try {

    await createNote(newNote.value);




  } catch (error) {
    console.error("Erreur de connexion au serveur:", error);
  }finally {
    closeModal();
  }

};

onMounted(async () => {
  await fetchNotes();
});
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  line-clamp: 3;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.truncate-title {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.line-clamp-3 {
  display: -webkit-box;
  line-clamp: 3;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
