<template>
  <div class="mx-5 mt-14 md:mt-[1.5%] md:ml-0 md:mr-5">
    <Title>Calendrier</Title>
    <div class="flex  justify-between">
      <Button @click="openModalEvent" class="m-3 bg-green-600 text-white">+ Ajouter un évènement</Button>
      <Button  @click="navigateToEditEvent" class="m-3 bg-green-600 text-white">Éditer les évènement</Button>
    </div>
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
          <Button @click="showDescription(props.event)" class="hover:bg-green-800">
            <span>{{ props.event.title }}</span>
          </Button>

          <EventDisplay
              v-if="selectedEvent && selectedEvent.id === props.event.id"
              class="z-30"
              :event="selectedEvent"
              @close="closeModal"
          />
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
  </div>
  <Loading :loading="isLoading" />

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
import { useRouter } from "vue-router";

const router = useRouter();
import {useEventStore} from '@/stores/useEventStore.js';
import {usePlantStore} from '@/stores/usePlantStore.js';
import Title from '../components/Title.vue';
import EventDisplay from "@/components/EventDisplay.vue";
import Loading from "@/components/Loading.vue";

const plantStore = usePlantStore();
const eventStore = useEventStore();
const eventsCalendar = ref([]);
const showModal = ref(false);
const currentView = ref("week");
const isLoading = ref(true);
const showEvent = ref(false);

const selectedEvent = ref(null);

const navigateToEditEvent = () => {
  router.push("/calendar/editevents");
};

const event = ref({

  title:'',
  description: '',
  eventDate: new Date(),
  user: {},
  cycle: 0,
  plant: {},
});

const showDescription = (event) => {
  selectedEvent.value = event;
};


async function onViewChange(viewData) {
  isLoading.value = true;
  try {

    const startDate = new Date(viewData.startDate).toISOString().split('T')[0];
    const endDate = new Date(viewData.endDate).toISOString().split('T')[0];

    await useEventStore().fetchEvents(startDate, endDate);



    const formattedEvents = useEventStore().events.map(event => ({
      start: new Date(event.eventDate),
      end: new Date(event.eventDate),
      title: event.title,
      description: event.description,
      plant: event.plant.name,
      cycle: event.cycle,
    }));




    if (JSON.stringify(eventsCalendar.value) !== JSON.stringify(formattedEvents)) {
      eventsCalendar.value = [...formattedEvents];
    }} finally {
    isLoading.value = false;
  }

}
//!!! ATTENTION QUAND DAns les plants on récupe bien le user a modifier
async function  openModalEvent() {
  showModal.value = true;
  await plantStore.fetchPlants();
}


function closeModal() {
  showModal.value = false;
  showEvent.value = false;
  selectedEvent.value = null;
}

async function handleSubmit() {

  try{

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


  let currentDate = new Date();
  let day = currentDate.getDay();


  let startOfWeek = new Date(currentDate);
  startOfWeek.setDate(currentDate.getDate() - (day === 0 ? 6 : day - 1));

  let endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(startOfWeek.getDate() + 6);


  await onViewChange({startDate: startOfWeek.toISOString().split('T')[0],
    endDate: endOfWeek.toISOString().split('T')[0],
    view: "week",
    events: [

    ]});

});

</script>