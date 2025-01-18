<script setup>
import { onMounted } from 'vue'
import { usePlantStore } from '@/stores/usePlantStore'
import { useDBUserStore } from '@/stores/DBUserStore'
import { useEventStore } from '@/stores/useEventStore'
import { useArticleStore } from '@/stores/useArticleStore'

const plantStore = usePlantStore()
const userStore = useDBUserStore()
const eventStore = useEventStore()
const articleStore = useArticleStore()

const plantId = 1
const userId = 1
const eventId = 1
const articleId = 1

onMounted(async () => {
  await plantStore.fetchPlants() 
  console.log('All Plants:', plantStore.plants)

  await plantStore.fetchPlant(plantId)
  console.log(`Plant by ID (${plantId}):`, plantStore.plant)

  await userStore.fetchUsers()
  console.log('All Users:', userStore.users)

  await userStore.fetchUser(userId)
  console.log(`User by ID (${userId}):`, userStore.user)

  await eventStore.fetchEvents()
  console.log('All Events:', eventStore.events)

  await eventStore.fetchEvent(eventId)
  console.log(`Event by ID (${eventId}):`, eventStore.event)

  await articleStore.fetchArticles()
  console.log('All Articles:', articleStore.articles)

  await articleStore.fetchArticle(articleId)
  console.log(`Article by ID (${articleId}):`, articleStore.article)
})

</script>

<template>
  <div>
    <h1>Dashboard</h1>

    <div v-if="plantStore.error">
      <p>Error (Plants): {{ plantStore.error }}</p>
    </div>
    <div v-else-if="plantStore.plants && plantStore.plants.length">
      <h2>All Plants</h2>
      <ul>
        <li v-for="plant in plantStore.plants" :key="plant.id">{{ plant.name }}</li>
      </ul>
    </div>
    <div v-else>
      <p>Loading Plants...</p>
    </div>

    <div v-if="userStore.error">
      <p>Error (User): {{ userStore.error }}</p>
    </div>
    <div v-else-if="userStore.user">
      <h2>User</h2>
      <p>{{ userStore.user.name }}</p>
    </div>
    <div v-else>
      <p>Loading User...</p>
    </div>

    <div v-if="eventStore.error">
      <p>Error (Events): {{ eventStore.error }}</p>
    </div>
    <div v-else-if="eventStore.events && eventStore.events.length">
      <h2>All Events</h2>
      <ul>
        <li v-for="event in eventStore.events" :key="event.id">{{ event.name }}</li>
      </ul>
    </div>
    <div v-else>
      <p>Loading Events...</p>
    </div>

    <div v-if="articleStore.error">
      <p>Error (Articles): {{ articleStore.error }}</p>
    </div>
    <div v-else-if="articleStore.articles && articleStore.articles.length">
      <h2>All Articles</h2>
      <ul>
        <li v-for="article in articleStore.articles" :key="article.id">{{ article.title }}</li>
      </ul>
    </div>
    <div v-else>
      <p>Loading Articles...</p>
    </div>

  </div>
</template>
