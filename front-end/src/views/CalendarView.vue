
<template>
  <div>
    <h1>Calendar</h1>
    <Button class="bg-green-600 text-white">+Event</Button>

    <vue-cal :events="events" class="border rounded-lg border-green-600 " :time="false"  :disable-views="['years', 'year', 'month']" />
  </div>
<div>
  <p>   </p>
</div>
</template>

<style>
.vuecal__menu {
  background-color: rgb(22,163,74);
  color: white;


}

</style>

<script setup>
import { ref, onMounted } from 'vue'
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'
import Button from '../components/Button.vue'
const events = ref([])

async function fetchevent() {
  try {
    const response = await fetch('http://localhost:8080/api/events')
    if (response.ok) {
      const fetchedEvents = await response.json()

      const formattedEvents = fetchedEvents.map(event => ({
        start: new Date(event.eventDate),
        end: new Date(event.eventDate),
        title: event.description
      }))

      if (JSON.stringify(events.value) !== JSON.stringify(formattedEvents)) {
        events.value = formattedEvents
      }
    } else {
      console.error("Erreur lors de la récupération des événements.")
    }
  } catch (error) {
    console.error("Erreur de connexion au serveur:", error)
  }
}

onMounted(() => {
  fetchevent()
})
</script>
