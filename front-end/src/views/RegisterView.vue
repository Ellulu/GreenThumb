<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '../stores/userStore'
import { isEmailValid, validatePassword, isPasswordConfirmationValid } from '../assets/js/utils'
import GoogleSignIn from '../components/GoogleSignIn.vue'
import Input from '../components/Input.vue'
import Button from '../components/Button.vue'
import Title from '../components/Title_3.vue'

const userStore = useUserStore()

const name = ref('')
const firstName = ref('')
const email = ref('')
const emailError = ref('')
const password = ref('')
const passwordError = ref('')
const confirmPassword = ref('')
const confirmPasswordError = ref('')

watch(email, (value) => {
    if (!isEmailValid(value)) {
        emailError.value = 'L\'email n\'est pas valide'
    } else {
        emailError.value = ''
    }
})

watch(password, (value) => {
    if (validatePassword(value).length > 0) {
        passwordError.value = 'Le mot de passe doit contenir ' + validatePassword(value).join(', ')
    } else {
        passwordError.value = ''
    }
})

watch(confirmPassword, (value) => {
    if (!isPasswordConfirmationValid(password.value, value)) {
        confirmPasswordError.value = 'Les mots de passe ne correspondent pas'
    } else {
        confirmPasswordError.value = ''
    }
})

// TODO: save uid to database
// TODO: add error handling
const handleSubmit = async () => {
    try {
        await userStore.register(email.value, password.value, firstName.value + ' ' + name.value)
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div class="flex flex-col items-center justify-center h-screen md:w-1/2 mx-auto lg:w-1/3">
    <Title class="mb-5">Créer un compte</Title>

    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
        <div class="flex gap-2 w-full">
            <Input class="min-w-0" name="Prénom" v-model="firstName" placeholder="Ex: John" required />
            <Input class="min-w-0" name="Nom" v-model="name" placeholder="Ex: Doe" required />
        </div>
        
        <Input name="Email" v-model="email" placeholder="Ex: johndoe@gmail.com" :errorMessage="emailError" required />

        <Input name="Mot de passe" v-model="password" type="password" placeholder="********" :errorMessage="passwordError" required />
      
        <Input name="Confirmation du mot de passe" v-model="confirmPassword" type="password" placeholder="********" :errorMessage="confirmPasswordError" required />
        <Button type="submit">S'enregistrer</Button>
    </form>

    <GoogleSignIn />
</div>
</template>