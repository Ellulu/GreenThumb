<script setup>
import Title from "../components/Title.vue";
import ProfileDetails from "../components/ProfileDetails.vue";
</script>

<template>
  <div class="mt-14 md:mt-[1.5%] md:w-1/2 mx-auto lg:w-1/3">
    <Title>{{ isOwnProfile ? "Votre profil" : profileUser?.displayName || "Profil utilisateur" }}</Title>

    <ProfileDetails></ProfileDetails>

    <div
      class="flex flex-col md:flex-row gap-4 w-[85%] justify-center items-center md:w-full mx-auto md:mx-0 mt-8"
    >
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
