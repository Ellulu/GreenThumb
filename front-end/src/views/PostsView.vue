<template>
  <div class="min-h-screen">
    <Title>Pour vous</Title>

    <main class="max-w-2xl mx-auto mt-4 px-4">
      <NewArticleForm v-if="authUserStore.user" @submit-article="addArticle" />

      <AuthPrompt v-else  />


      <div class="space-y-4">
        <template v-if="isLoading">
            <PostsLoader/>
        </template>
        <template v-else>
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
                  <button
                    v-if="authUserStore.user && article.author.uid!=user.uid"
                    @click="toggleFollow(article.author)"
                    :class="isFollowing(article.author) ? 'bg-red-100 text-red-500 hover:bg-red-200' : 'bg-green-100 text-green-500 hover:bg-green-200'"
                    class="px-3 py-1 text-sm rounded-full font-medium transition"
                  >
                    {{ isFollowing(article.author) ? 'Unfollow' : 'Follow' }}
                  </button>
                </div>
                <p class="text-gray-500 text-sm">{{ formatDate(article.date) }}</p>
              </div>
            </div>
            <h4 class="text-xl font-bold mb-2">{{ article.title }}</h4>
            <p class="mb-4">{{ article.text }}</p>
            <div class="mb-4">
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
            <ArticleActions
            v-if="authUserStore.user"
            :article="article"
            @like="likeArticle(article)"
            @dislike="dislikeArticle(article)"
            @toggle-comments="toggleComments(article.id)"
            />
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
import Title from '../components/Title.vue';
import Title_3 from '../components/Title_3.vue';
import ArticleActions from '../components/ArticleActions.vue'
import PostsLoader from '../components/PostsLoader.vue';
import Input from '@/components/Input.vue';
import AuthPrompt from '@/components/AuthPrompt.vue'
import NewArticleForm from '@/components/NewArticleForm.vue'
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
const isVideo = (file) => {
  return file.match("/\.(jpeg|jpg|gif|png)$/") != null;
}
const getVideoType = (file) => {
      const extension = file.split('.').pop().toLowerCase();
      switch (extension) {
        case 'mp4':
          return 'video/mp4';
        case 'webm':
          return 'video/webm';
        case 'ogg':
          return 'video/ogg';
        default:
          return '';
      }
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
    articleStore.createArticle(newArticleObjSave,newArticle.files)
}
const convertFilesToUrls = (files) => {
  return files.map(file => URL.createObjectURL(file));
};
const likeArticle = (article) => {
  if ( article.rating.hasDislike) {
    article.rating.hasDislike = false
    article.rating.dislikes--
  }
  article.rating.hasLike = !article.rating.hasLike
  article.rating.likes += article.rating.hasLike ? 1 : -1
  articleStore.likeArticle(article.id)
}

const dislikeArticle = (article) => {
  if (article.rating.hasLike) {
    article.rating.hasLike = false
    article.rating.likes--
  }
  article.rating.hasDislike = !article.rating.hasDislike
  article.rating.dislikes += article.rating.hasDislike ? 1 : -1
  articleStore.dislikeArticle(article.id)
}

const toggleComments = async (articleId) => {
  const article = articles.value.find((a) => a.id === articleId);
  if (!article.showComments) {
    article.comments = await articleStore.fetchComments(articleId);
  }
  article.showComments = !article.showComments;
};
const handleFileUpload = (event) => {
  files.value = Array.from(event.target.files);
};
const addComment = async (articleId) => {
  if (newComment.value.trim()) {
    await articleStore.addComment(articleId, {
      userId: user.uid, 
      content: newComment.value,
    });
    console.log("user:",user);
    const showComment = {
      imageUrl : user.photoURL,
      username : user.displayName,
      text     : newComment.value
    }
    const article = articles.value.find((a) => a.id === articleId);
    article.comments.push(showComment);
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
    if ((!dBUserStore.user || dBUserStore.user.uid !== user  || !dBUserStore.user.uid)&& user && user.uid) {
      await dBUserStore.fetchUser(user.uid);
    }
    if (articleStore.articles.length === 0) {
      console.log(dBUserStore.user)
      if( dBUserStore.user ){
        console.log("fetchArticles")
        await articleStore.fetchArticles()
      }else{
        console.log("fetchALLArticles")
        await articleStore.fetchAllArticles()
      }
    }
    articles.value = articleStore.articles
    articles.value.sort((a, b) => new Date(b.date) - new Date(a.date));
    console.log(articleStore.articles)
    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des articles:", err)
    isLoading.value = false
  }
})
const toggleFollow = async (user) => {
  if (isFollowing(user)) {
    await dBUserStore.unfollowUser(user);
  } else {
    await dBUserStore.followUser(user);
  }
};

const isFollowing = (user) => {
  return dBUserStore.user?.following?.some(follower => follower.uid === user.uid);
};
</script>
