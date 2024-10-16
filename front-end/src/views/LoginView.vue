<script setup>
import { ref } from 'vue'
import GoogleSignIn from '../components/GoogleSignIn.vue'
import Input from '../components/Input.vue'
import Button from '../components/Button.vue'
import Title from '../components/Title.vue'

const email = ref('')
const password = ref('')
const rememberMe = ref(false)
</script>

<template>
  <div class="flex flex-col items-center justify-center h-screen md:w-1/2 mx-auto lg:w-1/3">
    <Title class="mb-5">Connectez-vous à votre compte</Title>
    
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
      <Input name="Email" v-model="email" placeholder="Ex: johndoe@mail.com" required />

      <Input name="Mot de passe" v-model="password" type="password" placeholder="********" required />

      <div class="flex items-center gap-2">
          <input type="checkbox" id="remember" v-model="rememberMe" class="w-4 h-4 text-green-600 accent-green-600 bg-gray-100 rounded-md cursor-pointer" />
          <label for="remember" class="text-sm font-medium text-gray-500 cursor-pointer">Se souvenir de moi</label>
      </div>

      <Button type="submit">Se connecter</Button>
    </form>

    <GoogleSignIn />
  </div>
</template>
