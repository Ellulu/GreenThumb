<template>
  <div >
    <Title class="mb-8">Modifier Évènement</Title>

    <CardEventDisplay :daily-task="displayTasks">
      <template #default="{ task, index }">
        <Button @click="editTask(task, index)" class="bg-green-600 text-white py-1 px-3 rounded">
          Modifier
        </Button>
        <Button @click="deleteTask(task, index)" class="bg-red-600 text-white py-1 px-3 rounded">
          Supprimer
        </Button>
      </template>
    </CardEventDisplay>

  </div>
 <ModalForm :show="showEditForm"
            title="Modifier un évènement"
            submitLabel="Modifier"
            @submit="handleEdit"
            @close="closeModal"
            class="z-30">
  <template #form-content>
    <Input v-model="event.title" type="text" id="eventTitle" required name="titre" />
    <Input v-model="event.description" type="text" id="eventDescription" required name="Description" />
    <Input v-model="event.eventDate" type="date" required name="Commencer à partir de cette date"/>
    <label for="eventDate" class="block text-gray-700 font-bold mb-2">Lier une plante</label>
    <select v-model="event.plant" required >
      <option v-for="plant in plantStore.plants" :key="plant" :value="plant">{{ plant.name }}</option>
    </select>
    <Input v-model="event.cycle" type="number" required id="integerImute"  name="Répéter tous les X jours (0 pour aucune répètitions)" min="0"></Input>
  </template>
 </ModalForm>


  <Loading :loading="isLoading" />
</template>

<script setup>
import {onMounted, ref} from 'vue';
import CardEventDisplay from '@/components/CardEventDisplay.vue';
import Title from '@/components/Title.vue';
import Button from '@/components/Button.vue';
import { useEventStore } from '@/stores/useEventStore.js';
import ModalForm from "@/components/ModalForm.vue";
import Input from "@/components/Input.vue";
import {usePlantStore} from "@/stores/usePlantStore.js";
import Loading from "@/components/Loading.vue";

const eventStore = useEventStore();
const plantStore = usePlantStore();
const isLoading = ref(true);
const displayTasks = ref([]);
const showEditForm = ref(false);
const event = ref({});



 async function editTask(task) {
  event.value = task;
  await plantStore.fetchPlants();
  event.value.plant = plantStore.plants.find(plant => plant.id === task.plant.id);
  showEditForm.value = true;
}
function closeModal() {
  showEditForm.value = false;
  event.value = {};
}
async function handleEdit() {

  await eventStore.editEvent(event.value.id,event);

  displayTasks.value = eventStore.events;
  closeModal();
}


async function deleteTask(task) {
  await eventStore.deleteEvent(task.id);
  await eventStore.fetchAllEvent();
  displayTasks.value = eventStore.events;
}

onMounted(async () => {
  await eventStore.fetchAllEvent();
  eventStore.events.forEach(event => {
    event.eventDate = new Date(event.eventDate).toISOString().split('T')[0];
  });
  displayTasks.value = eventStore.events;
  isLoading.value = false;
});
</script>
