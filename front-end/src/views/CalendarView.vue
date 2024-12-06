<template>
  <div class="mx-5 mt-14 md:mt-[1.5%] md:ml-0 md:mr-5">
    <Title>Calendrier</Title>

    <Button @click="openModalEvent" class="m-3 bg-green-600 text-white">+ Ajouter un évènement</Button>

    <vue-cal
        :view="currentView"
        @view-change="onViewChange"
        :events="eventsCalendar"
        class="z-10 border rounded-lg border-green-600"
        :time="false"
        :disable-views="['years', 'year', 'month']"
    >
      <template v-slot:event="props">
        <div class="vuecal__event">
          <span>{{ props.event.title }}</span>
          <!-- Affiche la description de l'événement avec un scroll -->
          <div v-if="props.event.description" class="vuecal__event-description">
            <p>Description : </p>
            {{ props.event.description }}
          </div>
        </div>
      </template>
    </vue-cal>
    <ModalForm
        :show="showModal"
        title="Ajouter un Event"
        submitLabel="Ajouter Event"
        @submit="handleSubmit"
        @close="closeModal"
        class="z-30"
    >
      <template #form-content>
        <div>
          <Input v-model="event.title" type="text" id="eventTitle" required name="titre" />
        </div>
        <div class="mb-4">
          <Input v-model="event.description" type="text" id="eventDescription" required name="Description" />
        </div>
        <div>

          <Input v-model="event.eventDate" type="date" required name="Commencer à partir de cette date"/>

        </div>
        <div>
          <label for="eventDate" class="block text-gray-700 font-bold mb-2">Lier une plante</label>
          <select v-model="event.plant" required >

            <option v-for="plant in plantStore.plants" :key="plant" :value="plant">{{ plant.name }}</option>
          </select>

        </div>
        <div>
          <Input v-model="event.cycle" type="number" required id="integerImute" name="Répéter tous les X jour" min="1"></Input>
        </div>
      </template>
    </ModalForm>
  </div>
  <div v-if="loading" class="loading-overlay">
    <img src="@/assets/img/greenthumb.png" alt="Feuille" class="spinner-image" />
  </div>
</template>

<style>
.vuecal__menu {
  background-color: rgb(22, 163, 74);
  color: white;
}

.vuecal__event{
  background-color: rgba(0, 128, 0, 0.5);
  color: white;



}
.vuecal__event-title{
  border: 2px solid #ffffff;
}
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  color: #333;
}
.spinner-image {
  width: 100px;
  height: 100px;
  animation: spin 1s linear infinite;
}


@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.vuecal__event-description {
  max-width: 100%; /* Vous pouvez ajuster cette largeur selon le besoin de votre mise en page */
  white-space: nowrap; /* Empêche le texte de se couper et force le défilement horizontal */
  overflow-x: auto;  /* Ajoute une barre de défilement horizontale si le contenu dépasse la largeur */
  font-size: 0.875rem; /* Ajustez la taille de la police selon vos besoins */
  padding: 5px;
  background-color: rgba(0, 128, 0, 0.5);
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>

<script setup>
import {ref, onMounted} from 'vue';
import VueCal from 'vue-cal';
import 'vue-cal/dist/vuecal.css';
import Button from '../components/Button.vue';
import ModalForm from '@/components/ModalForm.vue';
import Input from '@/components/Input.vue';



import {useEventStore} from '@/stores/useEventStore.js';
import {usePlantStore} from '@/stores/usePlantStore.js';
import Title from '../components/Title.vue';

const plantStore = usePlantStore();
const eventStore = useEventStore();
const eventsCalendar = ref([]);
const showModal = ref(false);
const currentView = ref("week");
const loading = ref(false);

const event = ref({
  title:'',
  description: '',
  eventDate: new Date(),
  user: {},
  cycle: 0,
  plant: {},
});


async function onViewChange(viewData) {
  loading.value = true;
  try {
  const startDate = new Date(viewData.startDate);
  const endDate = new Date(viewData.endDate);


  await useEventStore().fetchEvents(startDate, endDate);



  const formattedEvents = useEventStore().events.map(event => ({
    start: new Date(event.eventDate),
    end: new Date(event.eventDate),
    title: event.title,
    description: event.description
  }));




  if (JSON.stringify(eventsCalendar.value) !== JSON.stringify(formattedEvents)) {
    eventsCalendar.value = [...formattedEvents];
  }} finally {
    loading.value = false;
  }

}

function openModalEvent() {
  showModal.value = true;
}


function closeModal() {
  showModal.value = false;
}
async function handleSubmit() {

try{
console.log("event début"+event.value.title)
  await eventStore.createEvent(event);


}finally {
 event.value = {
    title:'',
    description: '',
    eventDate: new Date(),
    user: {},
    cycle: 0,
    plant: {},
  };
 closeModal();
  window.location.reload();
}

}


onMounted(async () => {

  //await plantStore.fetchPlants();
let  currentDate = new Date();
  let startOfWeek = new Date(currentDate.setDate(currentDate.getDate() - currentDate.getDay() + 1));
  let endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(startOfWeek.getDate() + 6);
    onViewChange({startDate: startOfWeek.toISOString().split('T')[0],
      endDate: endOfWeek.toISOString().split('T')[0],
      view: "week",
      events: [

      ]});

});
</script>
