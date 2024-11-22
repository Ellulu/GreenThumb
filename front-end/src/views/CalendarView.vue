<template>
  <div class="mx-5 mt-14 md:mt-[1.5%] md:ml-0 md:mr-5">
    <Title>Calendrier</Title>

    <Button @click="openModalEvent" class="m-3 bg-green-600 text-white">+ Ajouter un évènement</Button>

    <vue-cal  :view="currentView" @view-change="onViewChange" :events="eventsCalendar" class=" z-10 border rounded-lg border-green-600" :time="false" :disable-views="['years', 'year', 'month']" />

    <ModalForm
        :show="showModal"
        title="Ajouter un Event"
        submitLabel="Ajouter Event"
        @submit="handleSubmit"
        @close="closeModal"
        class="z-30"
    >
      <template #form-content>
        <div class="mb-4">
          <Input v-model="event.description" type="text" id="eventTitle" required name="Description" />
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
          <Input v-model="event.cycle" type="number" required id="integerImute" name="Répéter tous les X jour"></Input>
        </div>
      </template>
    </ModalForm>
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

const event = ref({
  description: '',
  eventDate: new Date(),
  user_id: 'A123ze45',
  cycle: 0,
  plant: {},
});

async function onViewChange(viewData) {
  const startDate = new Date(viewData.startDate);
  const endDate = new Date(viewData.endDate);


  await useEventStore().fetchEvents(startDate, endDate);



  const formattedEvents = useEventStore().events.map(event => ({
    start: new Date(event.eventDate),
    end: new Date(event.eventDate),
    title: event.description
  }));




  if (JSON.stringify(eventsCalendar.value) !== JSON.stringify(formattedEvents)) {
    eventsCalendar.value = [...formattedEvents];
  }

}

function openModalEvent() {
  showModal.value = true;
}


function closeModal() {
  showModal.value = false;
}
async function handleSubmit() {

  await eventStore.createEvent(event.value);

  event.value = {
    description: '',
    eventDate: new Date(),
    user_id: 'A123ze45',
    cycle: 0,
    plant: {},
  };
  closeModal();
  window.location.reload();
}


onMounted(async () => {
  await plantStore.fetchPlants();
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
