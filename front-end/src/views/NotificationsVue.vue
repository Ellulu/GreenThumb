<script setup>
import { ref, onMounted } from 'vue';
import {Button, Title} from "@/components/index.js";
import {useNotificationStore} from "@/stores/useNotficationStore.js";
import EventDisplay from "@/components/EventDisplay.vue";
const notificationStore = useNotificationStore();
const notifications = ref([]);
const event = ref(null);

async function showEvent(notification) {
let tempevent = notification.event;
tempevent.start  =notification.event.eventDate;
tempevent.plant= notification.event.plant.name;
event.value = tempevent;


await notificationStore.deleteNotification(notification.id);
 await notificationStore.fetchNotifications();
  notifications.value = notificationStore.notifications;

}
function closeModal() {
event.value = null;
}

onMounted(async () => {
await notificationStore.fetchNotifications();
  notifications.value = notificationStore.notifications;
});



</script>

<template>
  <div>
    <Title>Notifications</Title>
  </div>
  <div>
    <div
        v-for="(notification, index) in notifications"
        :key="index"
        class="border-green-600 bg border-2 p-4 mb-4 flex flex-col gap-2 text-center"
    >
      <Title class="text-green-600">{{ notification.event.title }}</Title>

      <p>
        <span>{{ notification.notificationDate }}</span>
      </p>
      <Button
          @click="showEvent(notification)"
          class="bg-green-600 text-white py-1 px-3 rounded"
      >
        Lire
      </Button>
    </div>

  </div>
  <EventDisplay @close="closeModal"  v-if="event" :event="event" />




</template>
