<script setup>



import TaskDisplay from "@/components/CardEventDisplay.vue";
import { ref, onMounted, onUnmounted } from "vue";
import {useEventStore} from "@/stores/useEventStore";
import {Title} from "@/components/index.js";
import Loading from "@/components/Loading.vue";
const eventStore = useEventStore();
const displayTasks = ref([]);
const isLoading = ref(true);

async function fetchEvents() {
  const startDate = new Date().toISOString().split('T')[0];
  const current = new Date().toISOString().split('T')[0];

  await eventStore.fetchEvents(startDate, current);
  displayTasks.value = eventStore.events;


}

onMounted(async () => {
  await fetchEvents();
  isLoading.value = false;

});


</script>

<template>

  <div class="mt-14 md:mt-0 md:mr-5">
    <Title>Tâches du jour</Title>
  </div>

  <TaskDisplay :daily-task="displayTasks" />


  <Loading :loading="isLoading" />
</template>