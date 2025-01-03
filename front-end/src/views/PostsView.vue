<template>
  <div class="min-h-screen">
    <Title>Pour vous</Title>

    <main class="max-w-2xl mx-auto mt-4 px-4">
      <div class="bg-amber-50 rounded-lg shadow p-4 mb-4">
        <Input
          v-model="newArticleTitle"
          name="Titre de l'article"
          placeholder="Titre de l'article"
          required
        />
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
            :disabled="!newArticle.trim() || !newArticleTitle.trim()"
          >
            Nouvel Article
          </button>
        </div>
      </div>

      <div class="space-y-4">
        <template v-if="isLoading">
          <div v-for="n in 5" :key="n" class="bg-amber-50 rounded-lg shadow p-4 animate-pulse">
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
          <div v-for="article in articles" :key="article.id" class="bg-amber-50 rounded-lg shadow p-4">
            <div class="flex items-center space-x-4 mb-4">
              <img
                :src="article.author.imageUrl || '/placeholder.svg?height=48&width=48'"
                alt="User Avatar"
                class="w-12 h-12 rounded-full"
              />
              <div>
                <router-link
                  :to="`/profile/${article.author.UID}`"
                  class="font-bold text-green-700 hover:underline"
                >
                  {{ article.author.fullname }}
                </router-link>
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
              <div class="flex justify-between">
                <button @click="likeArticle(article)" class="flex items-center space-x-1 hover:text-green-700 m-2">
                  <ThumbsUpIcon :class="{'text-green-700': article.rating.hasLike}" class="w-5 h-5" />
                  <span>{{ article.rating.likes }}</span>
                </button>
                <button @click="dislikeArticle(article)" class="flex items-center space-x-1 hover:text-red-600 m-2">
                  <ThumbsDownIcon :class="{'text-red-600': article.rating.hasDislike}" class="w-5 h-5" />
                  <span>{{ article.rating.dislikes }}</span>
                </button>
              </div>
              <button @click="showComments(article)" class="flex items-center space-x-1 hover:text-green-700">
                <MessageCircleIcon class="w-5 h-5" />
                <span>{{ 0 }}</span>
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
import { useUserStore } from '@/stores/userStore';
import { UserIcon, ImageIcon, SmileIcon, MessageCircleIcon, ThumbsUpIcon, ThumbsDownIcon } from 'lucide-vue-next'
import Title from '../components/Title.vue'
import Input from '@/components/Input.vue';

const articleStore = useArticleStore()
const isLoading = ref(true)
const newArticle = ref('')
const newArticleTitle = ref('')
const articles = ref([])
const userStore = useUserStore()
const user = userStore.user;
const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' }
  return new Date(dateString).toLocaleDateString('fr-FR', options)
}
onMounted(()=> {
  console.log("michel"+user)
})
const addArticle = () => {
  console.log(user)
  if (newArticle.value.trim() && newArticleTitle.value.trim()) {
    const newArticleObj = {
      id: Date.now(),
      title: newArticleTitle.value,
      text: newArticle.value,
      author: {
        uid: user.uid,
        
        imageUrl: user.photoURL
      },
      date: new Date().toISOString(),
      files: [],
      rating: { likes: 0, dislikes: 0 },
    }
    articles.value.unshift(newArticleObj)
    articleStore.createArticle(newArticleObj)
    newArticle.value = ''
    newArticleTitle.value = ''
  }
}

const likeArticle = (article) => {
  if (article.disliked) {
    article.disliked = false
    article.rating.dislikes--
  }
  console.log(article.author)
  article.liked = !article.liked
  article.rating.likes += article.liked ? 1 : -1
  articleStore.likeArticle(article.id, userStore.user.uid)
}

const dislikeArticle = (article) => {
  if (article.liked) {
    article.liked = false
    article.rating.likes--
  }
  article.disliked = !article.disliked
  article.rating.dislikes += article.disliked ? 1 : -1
  articleStore.dislikeArticle(article.id, userStore.user.uid)
}

const showComments = (article) => {
  
  console.log(`Afficher les commentaires pour l'article ${article.id}: ${article.comments}`)
}

onMounted(async () => {
  try {
    if (articleStore.articles.length === 0) {
      console.log("fetching articles")
      await articleStore.fetchArticles()
    }
    console.log(articleStore.articles)
    articles.value = articleStore.articles
    articles.value.sort((a, b) => new Date(b.date) - new Date(a.date));
    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des articles:", err)
    isLoading.value = false
  }
})
</script>
