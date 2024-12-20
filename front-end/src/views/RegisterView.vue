<script setup>
import { ref, watch } from "vue";
import { useUserStore } from "../stores/userStore";
import {
  isEmailValid,
  validatePassword,
  isPasswordConfirmationValid,
} from "../assets/js/utils";
import GoogleSignIn from "../components/GoogleSignIn.vue";
import Input from "../components/Input.vue";
import Button from "../components/Button.vue";
import Title from "../components/Title.vue";
import ErrorSpan from "../components/ErrorSpan.vue";

const userStore = useUserStore();

const isLoading = ref(false);
const name = ref("");
const firstName = ref("");
const email = ref("");
const emailError = ref("");
const password = ref("");
const passwordError = ref("");
const confirmPassword = ref("");
const confirmPasswordError = ref("");
const globalError = ref("");

watch(email, (value) => {
  if (!isEmailValid(value)) {
    emailError.value = "L'email n'est pas valide";
  } else {
    emailError.value = "";
  }
});

watch(password, (value) => {
  if (validatePassword(value).length > 0) {
    passwordError.value =
      "Le mot de passe doit contenir " + validatePassword(value).join(", ");
  } else {
    passwordError.value = "";
  }
});

watch(confirmPassword, (value) => {
  if (!isPasswordConfirmationValid(password.value, value)) {
    confirmPasswordError.value = "Les mots de passe ne correspondent pas";
  } else {
    confirmPasswordError.value = "";
  }
});

const handleSubmit = async () => {
  try {
    isLoading.value = true;
    await userStore.register(
      email.value,
      password.value,
      firstName.value + " " + name.value
    );
    isLoading.value = false;
  } catch (error) {
    isLoading.value = false;
    globalError.value = error.message;
  }
};
</script>

<template>
  <div
    class="flex flex-col items-center justify-center h-screen md:w-1/2 mx-auto lg:w-1/3"
  >
    <Title class="mb-5">Créer un compte</Title>

    <ErrorSpan v-if="globalError">{{ globalError }}</ErrorSpan>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4 w-5/6">
      <div class="flex gap-2 w-full">
        <Input
          class="min-w-0"
          name="Prénom"
          v-model="firstName"
          placeholder="Ex: John"
          required
        />
        <Input
          class="min-w-0"
          name="Nom"
          v-model="name"
          placeholder="Ex: Doe"
          required
        />
      </div>

      <Input
        name="Email"
        v-model="email"
        placeholder="Ex: johndoe@gmail.com"
        :errorMessage="emailError"
        required
      />

      <Input
        name="Mot de passe"
        v-model="password"
        type="password"
        placeholder="********"
        :errorMessage="passwordError"
        required
      />

      <Input
        name="Confirmation du mot de passe"
        v-model="confirmPassword"
        type="password"
        placeholder="********"
        :errorMessage="confirmPasswordError"
        required
      />
      <Button type="submit" class="bg-green-600 hover:bg-green-700 text-white disabled:bg-gray-400" :disabled="isLoading"
        >S'enregistrer</Button
      >
    </form>

    <GoogleSignIn />
  </div>
</template>
