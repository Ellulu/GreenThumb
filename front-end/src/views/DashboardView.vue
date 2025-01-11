<script setup>


import Title_2 from "../components/Title_2.vue";
import TaskDisplay from "@/components/CardEventDisplay.vue";
import { ref, onMounted, onUnmounted } from "vue";
import {useEventStore} from "@/stores/useEventStore";
const eventStore = useEventStore();
const displayTasks = ref([]);


async function fetchEvents() {
  const startDate = new Date().toISOString().split('T')[0];
  const current = new Date().toISOString().split('T')[0];

  await eventStore.fetchEvents(startDate, current);
  displayTasks.value = eventStore.events;


}

onMounted(async () => {
  await fetchEvents();


});


</script>

<template>

  <div>
    <Title_2>Tàches du jours</Title_2>
  </div>

  <TaskDisplay :daily-task="displayTasks" />



</template>