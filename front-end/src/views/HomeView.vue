<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-green-800 mb-6">Feed GreenThumb(depecated)</h1>

    <div v-if="loading" class="text-center py-8">
      <Loader2Icon class="animate-spin h-8 w-8 mx-auto text-green-600" />
      <p class="mt-2 text-gray-600">Chargement des articles...</p>
    </div>

    <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="articles.length === 0" class="text-center py-8">
      <LeafIcon class="h-16 w-16 mx-auto text-gray-400" />
      <p class="mt-4 text-xl text-gray-600">Aucun article pour le moment.</p>
    </div>

    <div v-else class="space-y-6">
      <article v-for="article in articles" :key="article.id" class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6">
          <div class="flex items-center mb-4">
            <img :src="article.author.profilePhoto || 'https://via.placeholder.com/40'" alt="Profile photo" class="w-10 h-10 rounded-full mr-4">
            <div>
              <h2 class="font-semibold text-lg">{{ article.author.firstName }} {{ article.author.lastName }}</h2>
              <p class="text-gray-500 text-sm">{{ formatDate(article.date) }}</p>
            </div>
          </div>
          <h3 class="text-xl font-bold mb-2">{{ article.title }}</h3>
          <p class="text-gray-700 mb-4">{{ article.text }}</p>
          <div v-if="article.files.length > 0" class="mb-4">
            <h4 class="font-semibold mb-2">Fichiers joints :</h4>
            <ul class="list-disc list-inside">
              <li v-for="file in article.files" :key="file" class="text-blue-600 hover:underline">
                {{ file }}
              </li>
            </ul>
          </div>
          <div class="flex items-center justify-between text-gray-500">
            <div class="flex items-center space-x-4">
              <button @click="likeArticle(article)" class="flex items-center space-x-1 hover:text-green-600">
                <ThumbsUpIcon :class="{'text-green-600': article.liked}" />
                <span>{{ article.rating.likeCount }}</span>
              </button>
              <button @click="dislikeArticle(article)" class="flex items-center space-x-1 hover:text-red-600">
                <ThumbsDownIcon :class="{'text-red-600': article.disliked}" />
                <span>{{ article.rating.dislikeCount }}</span>
              </button>
              <button @click="showComments(article)" class="flex items-center space-x-1 hover:text-blue-600">
                <MessageCircleIcon />
                <span>{{ article.comments || 0 }}</span>
              </button>
            </div>
          </div>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useArticleStore } from '@/stores/useArticleStore';
import { Loader2Icon, LeafIcon, ThumbsUpIcon, ThumbsDownIcon, MessageCircleIcon } from 'lucide-vue-next';

const articleStore = useArticleStore();
const loading = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    if (articleStore.articles.length === 0) {
      await articleStore.fetchArticles();
    }
    loading.value = false;
  } catch (err) {
    error.value = "Erreur lors du chargement des articles.";
    loading.value = false;
  }
});

const { articles } = articleStore;

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
  return new Date(dateString).toLocaleDateString('fr-FR', options);
};

const likeArticle = (article) => {
  if (article.disliked) {
    article.disliked = false;
    article.rating.dislikeCount--;
  }
  article.liked = !article.liked;
  article.rating.likeCount += article.liked ? 1 : -1;
};

const dislikeArticle = (article) => {
  if (article.liked) {
    article.liked = false;
    article.rating.likeCount--;
  }
  article.disliked = !article.disliked;
  article.rating.dislikeCount += article.disliked ? 1 : -1;
};

const showComments = (article) => {
  console.log(`Afficher les commentaires pour l'article ${article.id}`);
};
</script>