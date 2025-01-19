<script setup>
import { ref, onMounted } from 'vue';
import Title from "@/components/Title.vue";
import PostsLoader from '@/components/PostsLoader.vue';
import NewArticleForm from '@/components/NewArticleForm.vue';
import { useUserStore } from '@/stores/userStore';
import { useArticleStore } from '@/stores/useArticleStore';
import { useDBUserStore } from '@/stores/dbUserStore';

const articleStore = useArticleStore();
const isLoading = ref(true)
const isLoadingMore = ref(false)
const noMoreArticles = ref(false)
const page = ref(0)

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

const deleteArticle = async (articleId) => {
  await articleStore.deleteArticle(articleId)
  articles.value = articles.value.filter((a) => a.id !== articleId)
}

const addArticle = (newArticle) => {
    const newArticleObj = {
      id: Date.now(),
      title: newArticle.title,
      text: newArticle.content,
      author: {
        fullname: user.displayName,
        uid: user.uid,
        imageUrl: user.photoURL
      },
      files: convertFilesToUrls(newArticle.files),
      date: new Date().toISOString(),
      rating: { likes: 0, dislikes: 0 },
    }
    console.log("files",newArticleObj.files)

    articles.value.unshift(newArticleObj)
    const newArticleObjSave = {
      id: 0,
      title: newArticle.title,
      text: newArticle.content,
      author: {
        uid: user.uid,
      },
      date: new Date().toISOString(),
    }
    articleStore.createArticle(newArticleObjSave,newArticle.files);
}

const isFollowing = (user) => {
  return dBUserStore.user?.following?.some(follower => follower.uid === user.uid);
}

onMounted(async () => {
  try {
    if ((!dBUserStore.user || dBUserStore.user.uid !== user  || !dBUserStore.user.uid)&& user && user.uid) {
      await dBUserStore.fetchUser(user.uid);
    }
    if (articleStore.articles.length === 0) {
      console.log(dBUserStore.user)
      if( dBUserStore.user ){
        await articleStore.fetchArticles(0)
      }else{
        await articleStore.fetchAllArticles(0)
      }
    }
    articles.value = articleStore.articles
    articles.value.sort((a, b) => {
      const aFollowing = isFollowing(a.author);
      const bFollowing = isFollowing(b.author);
      if (aFollowing && !bFollowing) return -1;
      if (!aFollowing && bFollowing) return 1;
      return new Date(b.date) - new Date(a.date);
    });

    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des articles:", err)
    isLoading.value = false
  }
})
</script>

<template>
    <div class="mt-14 md:mt-0 md:mr-5">
        <Title class="mb-5">Communauté</Title> 
    </div>

    <div class="max-w-2xl mx-auto mt-4 px-4">
        <NewArticleForm v-if="authUserStore.user" @submit-article="addArticle" />

        <PostsLoader v-if="isLoading"/>
        <div v-else>
            <div v-for="article in articles" :key="article.id" class="rounded-lg shadow p-4">
            <div class="flex items-center space-x-4 mb-4">
              <img
              :src="article.author.imageUrl || '/placeholder.svg?height=48&width=48'"
              alt="User Avatar"
              class="w-12 h-12 rounded-full"
              />
              <div class="flex-1">
              <div class="flex items-center justify-between">
                <p class="font-bold">{{ article.author.fullname }}</p>
                <div class="flex space-x-2">
                <button
                  v-if="authUserStore.user && article.author.uid!=user.uid"
                  @click="toggleFollow(article.author)"
                  :class="isFollowing(article.author) ? 'bg-red-100 text-red-500 hover:bg-red-200' : 'bg-green-100 text-green-500 hover:bg-green-200'"
                  class="px-3 py-1 text-sm rounded-full font-medium transition"
                >
                  {{ isFollowing(article.author) ? 'Unfollow' : 'Follow' }}
                </button>
                <button
                  v-if="authUserStore.user && article.author.uid === user.uid"
                  @click="deleteArticle(article.id)"
                  class="bg-red-100 text-red-500 hover:bg-red-200 px-3 py-1 text-sm rounded-full font-medium transition"
                >
                  Supprimer
                </button>
                </div>
              </div>
              <p class="text-gray-500 text-sm">{{ formatDate(article.date) }}</p>
              </div>
            </div>
            <h4 class="text-xl font-bold mb-2">{{ article.title }}</h4>
            <p class="mb-4">{{ article.text }}</p>
            <div class="mb-4" v-if="article.files && article.files.length > 0">
              <div class="relative overflow-hidden rounded-lg shadow-md">
              <div class="flex overflow-x-auto snap-x snap-mandatory no-scrollbar space-x-4 p-4">
                <div
                v-for="file in article.files"
                :key="file"
                class="snap-center flex-shrink-0 w-64 h-64 relative"
                >
                <img
                  :src="file"
                  :alt="'Image from ' + article.author.fullname"
                  class="w-full h-full object-cover rounded-lg shadow-md"
                />
                </div>
              </div>
              </div>
            </div>
        </div>
        </div>
    </div>
</template>