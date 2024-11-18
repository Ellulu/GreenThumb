<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import Title from "@/components/Title.vue";
import Input from "@/components/Input.vue";
import Button from "@/components/Button.vue";
import logo from "../assets/img/default.jpg";

const userStore = useUserStore();

const name = ref(userStore.user.displayName.split(" ")[1] || "");
const firstName = ref(userStore.user.displayName.split(" ")[0] || "");
const email = ref(userStore.user.email);
const emailError = ref("");
const oldPassword = ref("");
const oldPasswordError = ref("");
const newPassword = ref("");
const newPasswordError = ref("");

// TODO: Add validation
// TODO: Add Api calls
// TODO: Add error handling

const handleSubmit = () => {
    console.log("submit");
};

const handleDelete = () => {
    console.log("delete");
};
</script>

<template>
    <div class="mt-14 md:mt-0 md:mr-5">
        <Title class="mb-5">Éditer votre profil</Title>

        <form
            @submit.prevent="handleSubmit"
            class="flex flex-col gap-4 w-5/6 mx-auto md:w-1/2 lg:w-1/3"
        >
            <div class="flex items-center justify-center space-x-6">
                <div class="shrink-0">
                    <img
                        id="preview_img"
                        class="h-16 w-16 object-cover rounded-full border border-slate-300"
                        :src="logo"
                        alt="Current profile photo"
                    />
                </div>
                <label class="block">
                    <span class="sr-only">Choisissez une photo de profil</span>
                    <input
                        type="file"
                        onchange="loadFile(event)"
                        class="block w-full text-sm text-slate-500 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-100 file:text-green-700 hover:file:bg-green-200 hover:file:cursor-pointer"
                    />
                </label>
            </div>
            <div class="flex gap-2 w-full">
                <Input
                    class="min-w-0"
                    name="Prénom"
                    v-model="firstName"
                    placeholder="Ex: John"
                />
                <Input
                    class="min-w-0"
                    name="Nom"
                    v-model="name"
                    placeholder="Ex: Doe"
                />
            </div>

            <Input
                name="Email"
                v-model="email"
                placeholder="Ex: johndoe@gmail.com"
                :errorMessage="emailError"
            />

            <Input
                name="Ancien mot de passe"
                v-model="oldPassword"
                type="password"
                placeholder="********"
                :errorMessage="oldPasswordError"
            />

            <Input
                name="Nouveau mot de passe"
                v-model="newPassword"
                type="password"
                placeholder="********"
                :errorMessage="newPasswordError"
            />
            <Button
                type="submit"
                class="bg-green-600 hover:bg-green-700 text-white"
                >Enregistrer</Button
            >
        </form>

        <form
            @submit.prevent="handleDelete"
            class="flex flex-col gap-4 w-5/6 mt-5 mx-auto md:w-1/2 lg:w-1/3"
        >
            <Button type="submit" class="bg-red-600 hover:bg-red-700 text-white"
                >Supprimer votre compte</Button
            >
        </form>
    </div>
</template>
