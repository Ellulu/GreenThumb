<script setup>
import { RouterLink } from "vue-router";
import {
  NewspaperIcon,
  CalendarIcon,
  UsersIcon,
  UserIcon,
  HelpCircleIcon,
} from "lucide-vue-next";

const menuItems = [
  { name: "Posts", path: "/posts", icon: NewspaperIcon },
  { name: "Calendar", path: "/calendar", icon: CalendarIcon },
  { name: "Community", path: "/community", icon: UsersIcon },
  { name: "Profile", path: "/profile", icon: UserIcon },
  { name: "Help", path: "/help", icon: HelpCircleIcon },
];
</script>

<template>
  <nav
    class="bg-amber-50 drop-shadow-md fixed top-0 flex justify-evenly items-center w-full z-[100] py-2"
  >
    <RouterLink
      v-for="(item, index) in menuItems"
      :key="index"
      :to="item.path"
      class="flex flex-col justify-between items-center"
    >
      <span
        class="rounded-md p-2"
        :class="{ 'bg-green-100': $route.path.includes(item.path) }"
      >
        <component :is="item.icon" class="h-5 w-5 text-green-600"></component>
      </span>
    </RouterLink>
  </nav>
</template>
