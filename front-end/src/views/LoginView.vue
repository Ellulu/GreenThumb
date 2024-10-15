<script setup>
import { ref } from 'vue'
import GoogleSignIn from '../components/GoogleSignIn.vue'

const username = ref('')
const password = ref('')
</script>

<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <div class="mb-5">
        <h1 class="text-2xl text-center font-bold text-gray-600">Connectez-vous à votre compte</h1>
    </div>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
      <div class="flex flex-col gap-2">
        <label for="username" class="text-sm font-medium text-gray-500">Email</label>
        <input type="text" v-model="username" placeholder="Ex: johndoe@gmail.com" class="p-2 rounded-md border border-slate-300 outline-green-600" />
      </div>

      <div class="flex flex-col gap-2">
        <label for="password" class="text-sm font-medium text-gray-500">Mot de passe</label>
        <input type="password" v-model="password" placeholder="********" class="p-2 rounded-md border border-slate-300 outline-green-600" />
      </div>
      <div class="flex items-center gap-2">
          <input type="checkbox" id="remember" class="w-4 h-4 text-green-600 accent-green-600 bg-gray-100 rounded-md" />
          <label for="remember" class="text-sm font-medium text-gray-500">Se souvenir de moi</label>
      </div>
      <button type="submit" class="bg-green-600 text-white p-2 rounded-md">Se connecter</button>
    </form>

    <GoogleSignIn />
  </div>
</template>
