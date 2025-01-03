<script setup>
import { RouterLink, useRoute } from "vue-router";
import { useUserStore } from "../stores/useUserStore";
import { useUserStore as useMyUserStore} from "../stores/userStore";
import Title from "../components/Title.vue";
import { LeafIcon, NotebookPen, Edit, LogOut, UserPlus, UserCheck } from "lucide-vue-next";
import { ref, computed, onMounted } from "vue";

const userStore = useUserStore();
const myUserStore = useMyUserStore()
const route = useRoute();

const profileUserId = route.params.userId || userStore.user?.uid; // ID de l'utilisateur à afficher
const isOwnProfile = computed(() => profileUserId === userStore.user?.uid); // Est-ce le profil de l'utilisateur connecté
const profileUser = ref(null); // Données du profil affiché
const isSubscribed = ref(false); // Statut de l'abonnement

const fetchUserProfile = async () => {
  console.log(profileUserId)
  if (profileUserId) {
    await userStore.fetchUser(profileUserId);
    profileUser.value = userStore.user
    console.log(profileUser)
    isSubscribed.value = myUserStore.user?.subscriptions?.includes(profileUserId);
  }
};

const toggleSubscription = async () => {
  try {
    if (isSubscribed.value) {
      await userStore.unsubscribe(profileUserId);
    } else {
      await userStore.subscribe(profileUserId);
    }
    isSubscribed.value = !isSubscribed.value;
  } catch (error) {
    console.error("Erreur lors du changement d'abonnement :", error);
  }
};

onMounted(fetchUserProfile);

const menuItems = [
  { name: "Vos plantes", path: "/profile/plants", icon: LeafIcon },
  { name: "Vos notes", path: "/profile/notes", icon: NotebookPen },
  { name: "Modifier votre profil", path: "/profile/edit", icon: Edit },
];
</script>

<template>
  <div class="mt-14 md:mt-[1.5%] md:mr-5">
    <Title>{{ isOwnProfile ? "Votre profil" : profileUser?.displayName || "Profil utilisateur" }}</Title>

    <div class="flex flex-col items-center gap-4 mt-6">
      <img
        :src="profileUser?.photoUrl || '/default-avatar.png'"
        alt="Avatar utilisateur"
        class="w-32 h-32 rounded-full border-2 border-green-600"
      />
      <div class="text-center">
        <h2 class="text-lg font-bold">{{ profileUser?.displayName || "Nom inconnu" }}</h2>
        <p class="text-gray-600">{{ profileUser?.email }}</p>
      </div>
    </div>

    <div v-if="isOwnProfile" class="flex flex-col md:flex-row gap-4 w-[85%] justify-center items-center md:w-full mx-auto md:mx-0 mt-8">
      <RouterLink
        v-for="(item, index) in menuItems"
        :key="index"
        :to="item.path"
        class="w-full flex md:flex-col md:w-1/3 md:max-w-80 items-center gap-4 bg-green-600 text-white rounded-md drop-shadow-md p-2 md:p-4 hover:bg-green-700 transition-all ease-in-out duration-200"
      >
        <component :is="item.icon" class="h-5 w-5"></component>
        <p>{{ item.name }}</p>
      </RouterLink>

      <button
        class="w-full flex md:flex-col md:w-1/3 md:max-w-80 items-center gap-4 bg-red-600 text-white rounded-md drop-shadow-md p-2 md:p-4 hover:bg-red-700 transition-all ease-in-out duration-200"
        @click="userStore.logout()"
      >
        <LogOut class="h-5 w-5" />
        <p>Déconnexion</p>
      </button>
    </div>

    <div v-else class="flex flex-col items-center gap-4 mt-8">
      <button
        @click="toggleSubscription"
        :class="{
          'bg-green-600 hover:bg-green-700': !isSubscribed,
          'bg-red-600 hover:bg-red-700': isSubscribed
        }"
        class="text-white rounded-md drop-shadow-md p-3 transition-all ease-in-out duration-200"
      >
        <component :is="isSubscribed ? UserCheck : UserPlus" class="h-5 w-5 inline-block mr-2" />
        {{ isSubscribed ? "Se désabonner" : "S'abonner" }}
      </button>
    </div>
  </div>
</template>
