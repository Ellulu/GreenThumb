<script setup>
import { ref } from 'vue';
import Title_2 from "../components/Title_2.vue";
import TaskDisplay from "@/components/CardEventDisplay.vue";
import {onMounted} from "vue";
import {useEventStore} from "@/stores/useEventStore";
const enventStore = useEventStore();

const displayTasks = ref([]);


/*
const testevent = ref([{
  title: 'test',
  description: 'tessdfgsdfgsdfgsdfgsdfgsdfgt',
  eventDate: new Date(),
  user: {},
  cycle: 0,
  plant: {},

},{
  title: 'grzgzegzeg',
  description: 'tessdfgsdfgsdfgsdfgsdfgsdfgt',
  eventDate: new Date(),
  user: {},
  cycle: 0,
  plant: {},

}

])*/



async function fetchEvents() {
  let  currentDate = new Date();
  const startOfWeek = new Date().toISOString().split('T')[0];;

  await enventStore.fetchEvents(startOfWeek, startOfWeek);
  displayTasks.value = enventStore.events;


}

onMounted(() => {
  fetchEvents();
});

</script>

<template>

  <div>
    <Title_2>Tàches du jours</Title_2>
  </div>

    <TaskDisplay   :daily-task="displayTasks"/>


</template>