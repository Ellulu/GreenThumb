<script setup>
import { ref } from 'vue'
import { createUserWithEmailAndPassword } from 'firebase/auth'
import { auth } from '../assets/js/firebase'
import GoogleSignIn from '../components/GoogleSignIn.vue'
import Input from '../components/Input.vue'
import Button from '../components/Button.vue'
import Title from '../components/Title.vue'

const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const name = ref('')
const firstName = ref('')
</script>

<template>
    <div class="flex flex-col items-center justify-center h-screen md:w-1/2 mx-auto lg:w-1/3">
    <Title class="mb-5">Créer un compte</Title>

    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
        <div class="flex gap-2 w-full">
            <Input class="min-w-0" name="Prénom" v-model="firstName" placeholder="Ex: John" required />
            <Input class="min-w-0" name="Nom" v-model="name" placeholder="Ex: Doe" required />
        </div>
        
        <Input name="Email" v-model="email" placeholder="Ex: johndoe@gmail.com" required />

        <Input name="Mot de passe" v-model="password" type="password" placeholder="********" required />
      
        <Input name="Confirmation du mot de passe" v-model="confirmPassword" type="password" placeholder="********" required />
        <Button type="submit">S'enregistrer</Button>
    </form>

    <GoogleSignIn />
</div>
</template>