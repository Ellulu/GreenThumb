<template>
  <div class="min-h-screen bg-green-50">

    <header class="bg-green-600 text-white p-4 sticky top-0 z-10">
      <div class="max-w-2xl mx-auto flex justify-between items-center">
        <h1 class="text-xl font-bold">Green Thumb</h1>
        <button class="p-2 rounded-full hover:bg-green-700 transition">
          <UserIcon class="w-6 h-6" />
        </button>
      </div>
    </header>

    <main class="max-w-2xl mx-auto mt-4 px-4">

      <div class="bg-white rounded-lg shadow p-4 mb-4">
        <textarea
            v-model="NewPoste"
            placeholder="What's happening?"
            class="w-full h-20 resize-none border-b border-green-200 focus:outline-none focus:border-green-500 mb-4"
        ></textarea>
        <div class="flex justify-between items-center">
          <div class="flex space-x-2 text-green-600">
            <button class="p-2 rounded-full hover:bg-green-100 transition">
              <ImageIcon class="w-5 h-5" />
            </button>
            <button class="p-2 rounded-full hover:bg-green-100 transition">
              <SmileIcon class="w-5 h-5" />
            </button>
          </div>
          <button
              @click="addTweet"
              class="bg-green-500 text-white px-4 py-2 rounded-full font-bold hover:bg-green-600 transition"
              :disabled="!NewPoste.trim()"
          >
            Tweet
          </button>
        </div>
      </div>

      <div class="space-y-4">
        <template v-if="isLoading">
          <div v-for="n in 5" :key="n" class="bg-white rounded-lg shadow p-4 animate-pulse">
            <div class="flex items-center space-x-4 mb-4">
              <div class="w-12 h-12 bg-green-200 rounded-full"></div>
              <div class="flex-1">
                <div class="h-4 bg-green-200 rounded w-1/4 mb-2"></div>
                <div class="h-3 bg-green-200 rounded w-1/2"></div>
              </div>
            </div>
            <div class="space-y-2">
              <div class="h-4 bg-green-200 rounded"></div>
              <div class="h-4 bg-green-200 rounded"></div>
              <div class="h-4 bg-green-200 rounded w-5/6"></div>
            </div>
          </div>
        </template>
        <template v-else>
          <div v-for="poste in postes" :key="poste.id" class="bg-white rounded-lg shadow p-4">
            <div class="flex items-center space-x-4 mb-4">
              <img :src="poste.avatar" alt="User Avatar" class="w-12 h-12 rounded-full" />
              <div>
                <h3 class="font-bold">{{ poste.username }}</h3>
                <p class="text-gray-500 text-sm">{{ poste.handle }}</p>
              </div>
            </div>
            <p class="mb-4">{{ poste.content }}</p>
            <div class="flex justify-between text-green-600">
              <button class="flex items-center space-x-1 hover:text-green-700">
                <MessageCircleIcon class="w-5 h-5" />
                <span>{{ poste.comments }}</span>
              </button>

              <button class="flex items-center space-x-1 hover:text-green-700">
                <HeartIcon class="w-5 h-5" />
                <span>{{ poste.likes }}</span>
              </button>

            </div>
          </div>
        </template>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { UserIcon, ImageIcon, SmileIcon, MessageCircleIcon, RepeatIcon, HeartIcon, ShareIcon } from 'lucide-vue-next'

const isLoading = ref(true)
const NewPoste = ref('')
const postes = ref([])

const addTweet = () => {
  if (NewPoste.value.trim()) {
    postes.value.unshift({
      id: Date.now(),
      username: 'Current User',
      avatar: '/placeholder.svg?height=48&width=48',
      content: NewPoste.value,
      comments: 0,
      likes: 0
    })
    NewPoste.value = ''
  }
}

onMounted(() => {
  // Charger les postes ici
  setTimeout(() => {
    postes.value = [

    ]
    isLoading.value = false
  }, 2000)
})
</script>