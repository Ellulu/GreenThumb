<template>
  <div class="min-h-screen flex items-center justify-center px-4 py-8">
    <div class="max-w-md w-full text-center">
      <div class="relative mb-8">
        <div class="text-9xl font-bold text-green-600 opacity-20">404</div>
        <div class="absolute inset-0 flex items-center justify-center">
          <LeafIcon class="h-32 w-32 text-green-600 animate-wiggle" />
        </div>
      </div>
      <h1 class="text-4xl font-bold text-green-800 mb-4">Oups ! Page non trouvée</h1>
      <p class="text-xl text-green-700 mb-8">
        Il semble que cette plante n'aie pas encore poussé dans notre jardin.
      </p>
      <router-link 
        to="/posts" 
        class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out"
      >
        <HomeIcon class="h-5 w-5 mr-2" />
        Retour à l'accueil
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { LeafIcon, HomeIcon } from 'lucide-vue-next';
</script>

<style scoped>
@keyframes wiggle {
  0%, 100% { transform: rotate(-3deg); }
  50% { transform: rotate(3deg); }
}

.animate-wiggle {
  animation: wiggle 2s ease-in-out infinite;
}
</style>