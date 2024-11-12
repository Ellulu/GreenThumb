<script setup>
import { ref, Transition } from "vue";
import { RouterLink } from "vue-router";
import { ChevronsRight, ChevronsLeft } from "lucide-vue-next";
import logo from "../assets/img/greenthumb.png";

defineProps({ menuItems: { type: Array, default: [] } });

const isMenuOpen = ref(true);

const toggle = () => {
  isMenuOpen.value = !isMenuOpen.value;
};
</script>

<template>
  <div
    class="transition-all duration-500 ease-in-out"
    :class="{ 'w-24': !isMenuOpen, 'w-64': isMenuOpen }"
  ></div>
  <div
    class="fixed top-0 h-screen transition-all duration-500 ease-in-out z-[100] md:translate-x-0"
  >
    <nav
      class="top-0 flex flex-col justify-between m-5 h-[95%] rounded-md bg-green-600 text-white space-y-6 p-2 transition-all duration-500 ease-in-out md:translate-x-0"
      :class="{ 'w-[4.3rem]': !isMenuOpen, 'w-60': isMenuOpen }"
    >
      <div>
        <div
          class="bg-amber-50 text-green-600 py-1 mb-5 px-2 rounded-md flex items-center space-x-1"
        >
          <img :src="logo" class="w-8 h-8" alt="logo" />
          <Transition name="fade">
            <span v-show="isMenuOpen" class="text-lg font-bold">
              GreenThumb
            </span>
          </Transition>
        </div>

        <RouterLink
          v-for="(item, index) in $props.menuItems"
          :key="index"
          :to="item.path"
          class="my-1 py-2.5 px-4 rounded-md transition duration-200 hover:bg-green-700 flex items-center space-x-2"
          :class="{ 'bg-green-700': $route.path.includes(item.path) }"
        >
          <component :is="item.icon" class="h-5 w-5 my-0.5" />
          <Transition name="fade">
            <span v-show="isMenuOpen">{{ item.name }}</span>
          </Transition>
        </RouterLink>
      </div>

      <button
        :onclick="toggle"
        class="w-full mt-auto my-0.5 py-2.5 px-4 rounded-md transition duration-200 hover:bg-green-700"
      >
        <div class="flex items-center space-x-2" v-if="isMenuOpen">
          <ChevronsLeft class="h-5 w-5" />
          <Transition name="fade">
            <span>Collapse</span>
          </Transition>
        </div>
        <ChevronsRight class="h-5 w-5" v-else />
      </button>
    </nav>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-active {
  transition-delay: 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
