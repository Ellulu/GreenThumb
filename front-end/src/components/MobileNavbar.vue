<script setup>
import { RouterLink } from "vue-router";

defineProps({ menuItems: { type: Array, required: true, default: [] } });
</script>

<template>
  <nav
    class="drop-shadow-md bg-white fixed top-0 flex justify-evenly items-center w-full z-[100] py-2"
  >
    <RouterLink
      v-for="(item, index) in $props.menuItems"
      :key="index"
      :to="item.path"
      class="rounded-md p-2 text-green-600"
      :class="{ 'bg-green-600 text-white': $route.path.includes(item.path) }"
    >
      <component :is="item.icon" class="h-5 w-5"></component>
    </RouterLink>
  </nav>
</template>
