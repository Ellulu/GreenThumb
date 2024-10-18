<script setup>
import { useUserStore } from '../stores/userStore'
import googleLogo from '../assets/img/google.png'

const userStore = useUserStore()

// TODO: save uid to database
// TODO: add error handling
const signInWithGoogle = () => {
    userStore.loginWithGoogle()
}
</script>

<template>
    <div class="flex flex-col items-center gap-4 w-5/6 mt-5">
        <p class="text-sm text-gray-500">Ou</p>
        <button @click="signInWithGoogle" class="flex items-center gap-2 w-full justify-center border border-slate-300 rounded-md p-1 bg-white hover:bg-slate-100 transition-colors duration-300 ease-in-out">
            <img :src="googleLogo" alt="google logo" class="w-auto h-10">
            <p class="text-sm font-medium text-gray-500">Connectez-vous avec Google</p>
        </button>
    </div>
</template>