<script setup>


import Title_2 from "../components/Title_2.vue";
import TaskDisplay from "@/components/CardEventDisplay.vue";
import { ref, onMounted, onUnmounted } from "vue";
import {useEventStore} from "@/stores/useEventStore";
import {useWebSocketStore} from "@/stores/useWebSocketStore.js";
const eventStore = useEventStore();
const displayTasks = ref([]);

const webSocketStore = useWebSocketStore();
const notifications = webSocketStore.notifications; // Accès réactif aux notifications
async function fetchEvents() {
  const startDate = new Date().toISOString().split('T')[0];
  const current = new Date().toISOString().split('T')[0];

  await eventStore.fetchEvents(startDate, current);
  displayTasks.value = eventStore.events;


}

onMounted(async () => {
  await fetchEvents();
  await webSocketStore.connectWebSocket();

});

onUnmounted(() => {
  webSocketStore.disconnectWebSocket(); // Déconnecte le WebSocket lors du démontage
});
</script>

<template>

  <div>
    <Title_2>Tàches du jours</Title_2>
  </div>

  <TaskDisplay :daily-task="displayTasks" />

  <div class="notifications-container">
    <h2 class="text-lg font-bold mb-4">Notifications</h2>
    <ul>
      <li
          v-for="notification in notifications"
          :key="notification.id"
          class="notification-item"
      >
        <div class="notification-content">
          <strong>{{ notification.title }}</strong>
          <p>{{ notification.body }}</p>
        </div>
      </li>
    </ul>
    <div v-if="notifications.length === 0" class="no-notifications">
      Aucune notification pour le moment.
    </div>
  </div>
</template>