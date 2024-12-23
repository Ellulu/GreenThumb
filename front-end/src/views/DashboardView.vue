<script setup>
import {onUnmounted, ref} from 'vue';
import Title_2 from "../components/Title_2.vue";
import TaskDisplay from "@/components/CardEventDisplay.vue";
import {onMounted} from "vue";
import {useEventStore} from "@/stores/useEventStore";
import { useNotificationStore } from '@/stores/useNotification';

const enventStore = useEventStore();
const notificationStore = useNotificationStore();
const displayTasks = ref([]);




async function fetchEvents() {
  let  currentDate = new Date();
  const startOfWeek = new Date().toISOString().split('T')[0];;

  await enventStore.fetchEvents(startOfWeek, startOfWeek);
  displayTasks.value = enventStore.events;


}

onMounted(() => {
  notificationStore.connectWebSocket();
  fetchEvents();
});
onUnmounted(() => {
  notificationStore.disconnectWebSocket(); // Déconnexion à la fermeture du composant
});
const notifications = notificationStore.notifications;
</script>

<template>

  <div>
    <Title_2>Tàches du jours</Title_2>
  </div>

    <TaskDisplay   :daily-task="displayTasks"/>

  <div>
    <h2>Notifications</h2>
    <ul>
      <li v-for="(notification, index) in notifications" :key="index">
        {{ notification }}
      </li>
    </ul>
  </div>
</template>