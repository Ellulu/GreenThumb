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
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <p class="font-bold">{{ article.author.fullname }}</p>
                  <button
                    v-if="article.author.UID!=user.uid"
                    @click="toggleFollow(article.author.UID)"
                    :class="isFollowing(article.author.UID) ? 'bg-red-100 text-red-500 hover:bg-red-200' : 'bg-green-100 text-green-500 hover:bg-green-200'"
                    class="px-3 py-1 text-sm rounded-full font-medium transition"
                  >
                    {{ isFollowing(article.author.UID) ? 'Unfollow' : 'Follow' }}
                  </button>
                </div>
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
              <div class="flex space-x-4">
                <button @click="likeArticle(article)" class="flex items-center space-x-1 hover:text-green-700">
                  <ThumbsUpIcon :class="{'text-green-700': article.rating.hasLike}" class="w-5 h-5" />
                  <span>{{ article.rating.likes }}</span>
                </button>
                <button @click="dislikeArticle(article)" class="flex items-center space-x-1 hover:text-red-600">
                  <ThumbsDownIcon :class="{'text-red-600': article.rating.hasDislike}" class="w-5 h-5" />
                  <span>{{ article.rating.dislikes }}</span>
                </button>
                <button @click="toggleComments(article.id)" class="flex items-center space-x-1 hover:text-blue-600">
                  <MessageCircleIcon :class="{'text-blue-600': article.showComments}" class="w-5 h-5" />
                  <span>{{ article.comments ? article.comments.length : 0 }}</span>
                </button>
              </div>
            </div>
            
              <transition name="fade">
                <div v-if="article.showComments" class="mt-4 space-y-4">
                  <div v-for="comment in article.comments" :key="comment.id" class="flex space-x-3 p-3 bg-gray-50 rounded-lg">
                    <img :src="comment.imageUrl || '/placeholder.svg?height=40&width=40'" alt="User Avatar" class="w-10 h-10 rounded-full" />
                    <div class="flex-1">
                      <div class="flex items-center justify-between">
                        <p class="font-semibold text-sm">{{ comment.username }}</p>
                        <button @click="deleteComment(article.id, comment.id)" class="text-red-500 text-xs hover:underline">Supprimer</button>
                      </div>
                      <p class="text-sm text-gray-700 mt-1">{{ comment.text }}</p>
                    </div>
                  </div>
                  
                  <div class="flex items-center space-x-3 mt-4">
                    <img :src="user.photoURL || '/placeholder.svg?height=40&width=40'" alt="Your Avatar" class="w-10 h-10 rounded-full" />
                    <div class="flex-1">
                      <textarea 
                        v-model="newComment" 
                        class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent resize-none"
                        placeholder="Ajoutez un commentaire"
                        rows="2"
                      ></textarea>
                    </div>
                    <button 
                      @click="addComment(article.id)" 
                      class="bg-green-500 text-white py-2 px-4 rounded-full font-bold hover:bg-green-600 transition disabled:opacity-50 disabled:cursor-not-allowed"
                      :disabled="!newComment.trim()"
                    >
                      Commenter
                    </button>
                  </div>
                </div>
              </transition>
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
import { useDBUserStore } from '@/stores/dbUserStore';
import { UserIcon, ImageIcon, SmileIcon, MessageCircleIcon, ThumbsUpIcon, ThumbsDownIcon } from 'lucide-vue-next'
import Title from '../components/Title.vue'
import Title_3 from '../components/Title_3.vue'
import Input from '@/components/Input.vue';

const articleStore = useArticleStore()
const isLoading = ref(true)
const newArticle = ref('')
const newArticleTitle = ref('')
const articles = ref([])
const newComment = ref("");
const authUserStore = useUserStore()
const dBUserStore = useDBUserStore()
const user = authUserStore.user;
const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' }
  return new Date(dateString).toLocaleDateString('fr-FR', options)
}

const addArticle = () => {
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
  if (article.disliked || article.rating.hasDislike) {
    article.rating.hasDislike = false
    article.disliked = false
    article.rating.dislikes--
  }
  article.liked = !article.liked
  article.rating.hasLike = !article.rating.hasLike
  article.rating.likes += article.liked ? 1 : -1
  articleStore.likeArticle(article.id)
}

const dislikeArticle = (article) => {
  if (article.liked|| article.rating.hasLike) {
    article.liked = false
    article.rating.hasLike = false
    article.rating.likes--
  }
  article.disliked = !article.disliked
  article.rating.hasDislike = !article.rating.hasDislike
  article.rating.dislikes += article.disliked ? 1 : -1
  articleStore.dislikeArticle(article.id)
}

const toggleComments = async (articleId) => {
  const article = articles.value.find((a) => a.id === articleId);
  if (!article.showComments) {
    article.comments = await articleStore.fetchComments(articleId);
    console.log(article)
  }
  article.showComments = !article.showComments;
};

const addComment = async (articleId) => {
  if (newComment.value.trim()) {
    const comment = await articleStore.addComment(articleId, {
      userId: user.uid, 
      content: newComment.value,
    });
    const article = articles.value.find((a) => a.id === articleId);
    article.comments.push(comment);
    newComment.value = "";
  }
};
const deleteComment = async (articleId, commentId) => {
  await articleStore.deleteComment(commentId);
  const article = articles.value.find((a) => a.id === articleId);
  article.comments = article.comments.filter((c) => c.id !== commentId);
};

onMounted(async () => {
  try {


    if (!dBUserStore.user || dBUserStore.user.uid !== user) {
      console.log("Chargement de l'utilisateur depuis la base de données...");
      await dBUserStore.fetchUser(user.uid);
    } else {
      console.log("Utilisateur déjà chargé :", dBUserStore.user);
    }
    if (articleStore.articles.length === 0) {
      await articleStore.fetchArticles()
    }
    articles.value = articleStore.articles
    articles.value.sort((a, b) => new Date(b.date) - new Date(a.date));
    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des articles:", err)
    isLoading.value = false
  }
})
const toggleFollow = async (userId) => {
  if (isFollowing(userId)) {
    await dBUserStore.unfollowUser(userId);
  } else {
    await dBUserStore.followUser(userId);
  }
};

const isFollowing = (userId) => {
  return dBUserStore.user?.following?.some(follower => follower.uid === userId);
};
</script>
