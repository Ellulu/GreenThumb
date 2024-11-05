<script setup>
import { useUserStore } from '../stores/userStore'

const userStore = useUserStore()
</script>

<template>
    <div>
        <h1>Dashboard</h1>
        <p>Bienvenue {{ userStore.user.displayName }}</p>
        <p>Vous êtes connecté avec l'email {{ userStore.user.email }}</p>

        <button class="bg-red-500 text-white p-2 rounded-md" @click="userStore.logout">Déconnexion</button>
    </div>
</template>