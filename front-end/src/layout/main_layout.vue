<template>
  <div class="flex h-screen bg-gray-100">
    <!-- Navbar verticale -->
    <nav class="bg-green-600 text-white w-64 space-y-6 py-7 px-2 absolute inset-y-0 left-0 transform -translate-x-full md:relative md:translate-x-0 transition duration-200 ease-in-out">
      <div class="flex items-center justify-center mb-8">
        <span class="text-2xl font-semibold">GreenThumb</span>
      </div>
      <nav>
        <!-- Utilisation de router-link pour la navigation -->
        <router-link
          v-for="(item, index) in menuItems"
          :key="index"
          :to="item.path"
          class="block py-2.5 px-4 rounded transition duration-200 hover:bg-green-700"
          :class="{ 'bg-green-700': $route.path === item.path }"
        >
          <div class="flex items-center space-x-2">
            <component :is="item.icon" class="h-5 w-5" />
            <span>{{ item.name }}</span>
          </div>
        </router-link>
      </nav>
    </nav>

    <div class="flex-1 p-10">
      <transition name="fade" mode="out-in">
        <router-view />
      </transition>
    </div>

    <button
      @click="toggleMenu"
      class="md:hidden fixed z-90 bottom-10 right-8 bg-green-600 w-10 h-10 rounded-full drop-shadow-lg flex justify-center items-center text-white text-4xl hover:bg-green-700 duration-300"
    >
      <span class="sr-only">Toggle Menu</span>
      <MenuIcon v-if="!isMenuOpen" class="h-6 w-6" />
      <XIcon v-else class="h-6 w-6" />
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { HomeIcon, NewspaperIcon, LeafIcon, CalendarIcon, UsersIcon, UserIcon, HelpCircleIcon, MenuIcon, XIcon } from 'lucide-vue-next'

const menuItems = [
  { name: 'Dashboard', path: '/', icon: HomeIcon },
  { name: 'Posts', path: '/test', icon: NewspaperIcon },
  { name: 'Plants', path: '/plants', icon: LeafIcon },
  { name: 'Calendar', path: '/calendar', icon: CalendarIcon },
  { name: 'Community', path: '/community', icon: UsersIcon },
  { name: 'Profile', path: '/profile', icon: UserIcon },
  { name: 'Help', path: '/help', icon: HelpCircleIcon },
]

const isMenuOpen = ref(false)

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
  const nav = document.querySelector('nav')
  nav.classList.toggle('-translate-x-full')
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
