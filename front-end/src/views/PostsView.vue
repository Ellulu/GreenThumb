<template>
  <div class="min-h-screen">
    <Title>Pour vous</Title>

    <main class="max-w-2xl mx-auto mt-4 px-4">
      <div class="bg-white rounded-lg shadow p-4 mb-4">
        <textarea
          v-model="newArticle"
          placeholder="Quoi de neuf ?"
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
            @click="addArticle"
            class="bg-green-500 text-white px-4 py-2 rounded-full font-bold hover:bg-green-600 transition"
            :disabled="!newArticle.trim()"
          >
            Nouvel Article
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
          <div v-for="article in articles" :key="article.id" class="bg-white rounded-lg shadow p-4">
            <div class="flex items-center space-x-4 mb-4">
              <img :src="article.author.profilePhoto || '/placeholder.svg?height=48&width=48'" alt="User Avatar" class="w-12 h-12 rounded-full" />
              <div>
                <h3 class="font-bold">{{ article.author.firstName }} {{ article.author.lastName }}</h3>
                <p class="text-gray-500 text-sm">{{ formatDate(article.date) }}</p>
              </div>
            </div>
            <h4 class="text-xl font-bold mb-2">{{ article.title }}</h4>
            <p class="mb-4">{{ article.text }}</p>
            <div v-if="article.files.length > 0" class="mb-4">
              <h5 class="font-semibold mb-2">Fichiers joints :</h5>
              <ul class="list-disc list-inside">
                <li v-for="file in article.files" :key="file" class="text-blue-600 hover:underline">
                  {{ file }}
                </li>
              </ul>
            </div>
            <div class="flex justify-between text-green-600">
              <div class ="flex justify-between">
              <button @click="likeArticle(article)" class="flex items-center space-x-1 hover:text-green-700 m-2">
                <ThumbsUpIcon :class="{'text-green-700': article.liked}" class="w-5 h-5" />
                <span>{{ article.rating.likeCount }}</span>
              </button>
              <button @click="dislikeArticle(article)" class="flex items-center space-x-1 hover:text-red-600 m-2">
                <ThumbsDownIcon :class="{'text-red-600': article.disliked}" class="w-5 h-5" />
                <span>{{ article.rating.dislikeCount }}</span>
              </button>
              </div>
              <button @click="showComments(article)" class="flex items-center space-x-1 hover:text-green-700">
                <MessageCircleIcon class="w-5 h-5" />
                <span>{{ article.comments || 0 }}</span>
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
import { useArticleStore } from '@/stores/useArticleStore'
import { UserIcon, ImageIcon, SmileIcon, MessageCircleIcon, ThumbsUpIcon, ThumbsDownIcon } from 'lucide-vue-next'
import Title from '../components/Title.vue'

const articleStore = useArticleStore()
const isLoading = ref(true)
const newArticle = ref('')
const articles = ref([])

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' }
  return new Date(dateString).toLocaleDateString('fr-FR', options)
}

const addArticle = () => {
  if (newArticle.value.trim()) {
    const newArticleObj = {
      id: Date.now(),
      title: 'Nouveau article',
      text: newArticle.value,
      author: {
        firstName: 'Current',
        lastName: 'User',
        profilePhoto: '/placeholder.svg?height=48&width=48'
      },
      date: new Date().toISOString(),
      files: [],
      rating: { likeCount: 0, dislikeCount: 0 },
      comments: 0,
      liked: false,
      disliked: false
    }
    articles.value.unshift(newArticleObj)
    newArticle.value = ''
  }
}

const likeArticle = (article) => {
  if (article.disliked) {
    article.disliked = false
    article.rating.dislikeCount--
  }
  article.liked = !article.liked
  article.rating.likeCount += article.liked ? 1 : -1
  articleStore.createArticle(article)
}

const dislikeArticle = (article) => {
  if (article.liked) {
    article.liked = false
    article.rating.likeCount--
  }
  article.disliked = !article.disliked
  article.rating.dislikeCount += article.disliked ? 1 : -1
  articleStore.createArticle(article)
}

const showComments = (article) => {
  console.log(`Afficher les commentaires pour l'article ${article.id}`)
}

onMounted(async () => {
  try {
    if (articleStore.articles.length === 0) {
      await articleStore.fetchArticles()
    }
    articles.value = articleStore.articles
    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des articles:", err)
    isLoading.value = false
  }
})
</script>