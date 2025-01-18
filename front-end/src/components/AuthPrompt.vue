<template>
    <div class="rounded-lg shadow p-4 mb-4 text-center">
      <p class="mb-4">Connectez-vous pour ajouter un nouvel article</p>
      <div class="space-x-4">
        <button 
          @click="() => navigateTo('/login')" 
          class="bg-green-500 text-white px-4 py-2 rounded-full font-bold hover:bg-green-600 transition">
          Se connecter
        </button>
        <button 
          @click="() => navigateTo('/register')" 
          class="bg-blue-500 text-white px-4 py-2 rounded-full font-bold hover:bg-blue-600 transition">
          S'inscrire
        </button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  const navigateTo = (path) => {
    router.push(path);
  };
  </script>
  