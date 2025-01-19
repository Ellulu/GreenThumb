<script setup>
import { ref, onMounted } from 'vue';
import Title from "@/components/Title.vue";
import PostsLoader from '@/components/PostsLoader.vue';
import NewArticleForm from '@/components/NewArticleForm.vue';
import { useUserStore } from '@/stores/userStore';
import { useAdviceStore } from '@/stores/useAdviceStore';
import { useDBUserStore } from '@/stores/dbUserStore';

const adviceStore = useAdviceStore();
const isLoading = ref(true)
const isLoadingMore = ref(false)
const noMoreAdvices = ref(false)
const page = ref(0)

const newAdvice = ref('')
const newAdviceTitle = ref('')
const advices = ref([])
const authUserStore = useUserStore()
const dBUserStore = useDBUserStore()
const user = authUserStore.user;

const isAdmin = ref(false);

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' }
  return new Date(dateString).toLocaleDateString('fr-FR', options)
}

const deleteAdvice = async (adviceId) => {
  await adviceStore.deleteAdvice(adviceId)
  advices.value = advices.value.filter((a) => a.id !== adviceId)
}

const convertFilesToUrls = (files) => {
  return files.map(file => URL.createObjectURL(file));
}

const addAdvice = (newAdvice) => {
    const newAdviceObj = {
      id: Date.now(),
      title: newAdvice.title,
      text: newAdvice.content,
      author: {
        fullname: user.displayName,
        uid: user.uid,
        imageUrl: user.photoURL
      },
      files: convertFilesToUrls(newAdvice.files),
      date: new Date().toISOString(),
    }
    console.log("files",newAdviceObj.files)

    advices.value.unshift(newAdviceObj)
    const newAdviceObjSave = {
      id: 0,
      title: newAdvice.title,
      text: newAdvice.content,
      author: {
        uid: user.uid,
      },
      date: new Date().toISOString(),
    }
    adviceStore.createAdvice(newAdviceObjSave,newAdvice.files);
}

onMounted(async () => {
  try {
    if ((!dBUserStore.user || dBUserStore.user.uid !== user || !dBUserStore.user.uid)&& user && user.uid) {
      await dBUserStore.fetchUser(user.uid);
    }

    isAdmin.value = await authUserStore.checkIsAdmin();

    if (adviceStore.advices.length === 0) {
      await adviceStore.fetchAdvices(0)
    }

    advices.value = adviceStore.advices
    console.log(advices.value);
    advices.value.sort((a, b) => {
      return new Date(b.date) - new Date(a.date);
    });

    isLoading.value = false
  } catch (err) {
    console.error("Erreur lors du chargement des advices:", err)
    isLoading.value = false
  }
})
</script>

<template>
    <div class="mt-14 md:mt-0 md:mr-5">
        <Title class="mb-5">Communauté</Title> 
    </div>

    <div class="max-w-2xl mx-auto mt-4 px-4">
        <NewArticleForm v-if="authUserStore.user" @submit-article="addAdvice" />

        <PostsLoader v-if="isLoading"/>
        <div v-else>
            <div v-for="advice in advices" :key="advice.id" class="rounded-lg shadow p-4">
            <div class="flex items-center space-x-4 mb-4">
              <img
              :src="advice.author.imageUrl || '/placeholder.svg?height=48&width=48'"
              alt="User Avatar"
              class="w-12 h-12 rounded-full"
              />
              <div class="flex-1">
              <div class="flex items-center justify-between">
                <p class="font-bold">{{ advice.author.fullname }}</p>
                <div class="flex space-x-2">
                <button
                  v-if="isAdmin && (authUserStore.user && advice.author.uid === user.uid)"
                  @click="deleteAdvice(advice.id)"
                  class="bg-red-100 text-red-500 hover:bg-red-200 px-3 py-1 text-sm rounded-full font-medium transition"
                >
                  Supprimer
                </button>
                </div>
              </div>
              <p class="text-gray-500 text-sm">{{ formatDate(advice.date) }}</p>
              </div>
            </div>
            <h4 class="text-xl font-bold mb-2">{{ advice.title }}</h4>
            <p class="mb-4">{{ advice.text }}</p>
            <div class="mb-4" v-if="advice.files && advice.files.length > 0">
              <div class="relative overflow-hidden rounded-lg shadow-md">
              <div class="flex overflow-x-auto snap-x snap-mandatory no-scrollbar space-x-4 p-4">
                <div
                v-for="file in advice.files"
                :key="file"
                class="snap-center flex-shrink-0 w-64 h-64 relative"
                >
                <img
                  :src="file"
                  :alt="'Image from ' + advice.author.fullname"
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