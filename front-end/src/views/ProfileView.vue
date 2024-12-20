<script setup>
import Title from "../components/Title.vue";
import ProfileDetails from "../components/ProfileDetails.vue";
</script>

<template>
  <div class="mt-14 md:mt-[1.5%] md:w-1/2 mx-auto lg:w-1/3">
    <Title>Votre profil</Title>

    <ProfileDetails></ProfileDetails>

    <div
      class="flex flex-col md:flex-row gap-4 w-[85%] justify-center items-center md:w-full mx-auto md:mx-0 mt-8"
    >
    </div>
  </div>
</template>
