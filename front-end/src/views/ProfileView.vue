<script setup>
import { RouterLink } from "vue-router";
import Title from "../components/Title.vue";
import { LeafIcon, NotebookPen, Edit } from "lucide-vue-next";

const menuItems = [
  { name: "Vos plantes", path: "/profile/plants", icon: LeafIcon },
  { name: "Vos notes", path: "/profile/notes", icon: NotebookPen },
  { name: "Modifier votre profil", path: "/profile/edit", icon: Edit },
];
</script>

<template>
  <div class="mt-14 md:mt-[1.5%] md:mr-5">
    <Title>Votre profil</Title>

    <div
      class="flex flex-col md:flex-row gap-4 w-[85%] justify-center items-center md:w-full mx-auto md:mx-0 mt-8"
    >
      <RouterLink
        v-for="(item, index) in menuItems"
        :key="index"
        :to="item.path"
        class="w-full flex md:flex-col md:w-1/3 md:max-w-80 items-center gap-4 bg-green-600 text-white rounded-md drop-shadow-md p-2 md:p-4 hover:bg-green-700 transition-all ease-in-out duration-200"
      >
        <component :is="item.icon" class="h-5 w-5"></component>
        <p>{{ item.name }}</p>
      </RouterLink>
    </div>
  </div>
</template>
