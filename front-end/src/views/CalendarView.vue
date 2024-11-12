<template>
  <div>
    <h1>Calendar</h1>
    <Button @click="openModalEvent" class="bg-green-600 text-white">+Event</Button>

    <vue-cal :view="currentView" @view-change="onViewChange" :events="eventsCalendar" class="border rounded-lg border-green-600" :time="false" :disable-views="['years', 'year', 'month']" />

    <ModalForm
        :show="showModal"
        title="Ajouter un Event"
        submitLabel="Ajouter Event"
        @submit="handleSubmit"
        @close="closeModal"
    >
      <template #form-content>
        <div class="mb-4">
          <Input v-model="event.description" type="text" id="eventTitle" required name="Description" />
        </div>
        <div>
          <label for="eventDate" class="block text-gray-700 font-bold mb-2">Commencer à partir de cette date</label>
          <DateInput v-model="event.eventDate" />

        </div>
        <div>
          <label for="eventDate" class="block text-gray-700 font-bold mb-2">Lier une plante</label>
          <select v-model="event.plant">
            <option value="">Choisir une plante</option>
            <option v-for="plant in plantStore.plants" :key="plant" :value="plant">{{ plant.name }}</option>
          </select>

        </div>
        <div>
          <Input v-model="event.cycle" type="number" id="integerImute"  name="Répéter tous les X jour" ></Input>
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
</style>

<script setup>
import {ref, onMounted,toRefs} from 'vue';
import VueCal from 'vue-cal';
import 'vue-cal/dist/vuecal.css';
import Button from '../components/Button.vue';
import ModalForm from '@/components/ModalForm.vue';
import Input from '@/components/Input.vue';
import DateInput from '@/components/DateImput.vue';

import {useEventStore} from '@/stores/useEventStore.js';
import {usePlantStore} from '@/stores/usePlantStore.js';

const plantStore = usePlantStore();
const eventStore = useEventStore();
const eventsCalendar = ref([]);
const showModal = ref(false);
const currentView = ref("week");

const event = ref({
  description: '',
  eventDate: null,
  user_id: 'A123ze45',
  cycle: null,
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
    eventsCalendar.value = formattedEvents;
  }

}

function openModalEvent() {
  showModal.value = true;
}


function closeModal() {
  showModal.value = false;
}
async function handleSubmit() {
  console.log(event.value);
  await eventStore.createEvent(event.value);
  //clear event
  event.value = {
    description: '',
    eventDate: null,
    user_id: 'A123ze45',
    cycle: null,
    plant: {},
  };
  closeModal();
}
onMounted(async () => {
  await plantStore.fetchPlants();

    onViewChange({startDate: "2024-11-11",
      endDate: "2024-11-17",
      view: "week",
      events: [

      ]});

});
</script>
