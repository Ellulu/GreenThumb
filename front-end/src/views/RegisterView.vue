<script setup>
import { ref } from 'vue'
import { createUserWithEmailAndPassword } from 'firebase/auth'
import { auth } from '../assets/js/firebase'
import GoogleSignIn from '../components/GoogleSignIn.vue'

const email = ref('')
const password = ref('')
const name = ref('')
const firstName = ref('')

</script>

<template>
    <div class="flex flex-col items-center justify-center h-screen">
    <div class="mb-5">
        <h1 class="text-2xl text-center font-bold text-gray-600">Créer un compte</h1>
    </div>

    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
        <div class="flex gap-2 flex-wrap w-full">
            <div class="flex flex-col gap-2 w-[48.5%]">
                <label for="firstName" class="text-sm font-medium text-gray-500">Prénom</label>
                <input type="text" v-model="firstName" placeholder="Ex: John" class="p-2 rounded-md border border-slate-300 outline-green-600" />
            </div>
            <div class="flex flex-col gap-2 w-[48.5%]">
              <label for="name" class="text-sm font-medium text-gray-500">Nom</label>
              <input type="text" v-model="name" placeholder="Ex: Doe" class="p-2 rounded-md border border-slate-300 outline-green-600" />
            </div>
        </div>
        
        <div class="flex flex-col gap-2">
          <label for="username" class="text-sm font-medium text-gray-500">Email</label>
          <input type="text" v-model="email" placeholder="Ex: johndoe@gmail.com" class="p-2 rounded-md border border-slate-300 outline-green-600" />
        </div>

      <div class="flex flex-col gap-2">
        <label for="password" class="text-sm font-medium text-gray-500">Mot de passe</label>
        <input type="password" v-model="password" placeholder="********" class="p-2 rounded-md border border-slate-300 outline-green-600" />
      </div>
      
      <div class="flex flex-col gap-2">
        <label for="password" class="text-sm font-medium text-gray-500">Confirmation du mot de passe</label>
        <input type="password" v-model="password" placeholder="********" class="p-2 rounded-md border border-slate-300 outline-green-600" />
      </div>
      <button type="submit" class="bg-green-600 text-white p-2 rounded-md">S'enregistrer</button>
    </form>

    <GoogleSignIn />
</div>
</template>