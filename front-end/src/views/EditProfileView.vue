<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import _ from "underscore";
import Title from "@/components/Title.vue";
import Input from "@/components/Input.vue";
import Button from "@/components/Button.vue";
import ErrorSpan from "@/components/ErrorSpan.vue";
import logo from "@/assets/img/default.jpg";

const userStore = useUserStore();
const { user, updateUser, saveProfilePicture } = userStore;

const isLoading = ref(false);
const name = ref(`${_.rest(user.displayName.split(" "))}` || "");
const firstName = ref(user.displayName.split(" ")[0] || "");
const email = ref(user.email);
const emailError = ref("");
const oldPassword = ref("");
const oldPasswordError = ref("");
const newPassword = ref("");
const newPasswordError = ref("");
const globalError = ref("");

const imageSrc = ref(user.photoURL || logo);
const file = ref(null);

function loadFile(event) {
    file.value = event.target.files[0];
    if (file.value) {
        const reader = new FileReader();
        reader.onload = (e) => {
            imageSrc.value = e.target.result;
        };
        reader.readAsDataURL(file.value);
    }
}

// TODO: Add validation
// TODO: Add Api calls

const handleSubmit = async () => {
    try {
        isLoading.value = true;
        if (file.value) {
            await saveProfilePicture(file.value);
        }



        isLoading.value = false;
    } catch(error) {
        isLoading.value = false;
        globalError.value = error.message;
    }
};

const handleDelete = () => {
    try {
        isLoading.value = true;

        // Do something...

        isLoading.value = false;
    } catch(error) {
        isLoading.value = false;
        globalError.value = error.message;
    }
};
</script>

<template>
    <div class="mt-14 md:mt-0 md:mr-5">
        <Title class="mb-5">Éditer votre profil</Title>

        <ErrorSpan v-if="globalError">{{ globalError }}</ErrorSpan>
        <form
            @submit.prevent="handleSubmit"
            class="flex flex-col gap-4 w-5/6 mx-auto md:w-1/2 lg:w-1/3"
        >
            <div class="flex items-center justify-center space-x-6">
                <div class="shrink-0">
                    <img
                        id="preview_img"
                        class="h-16 w-16 object-cover rounded-full border border-slate-300"
                        :src="imageSrc || logo"
                        alt="Current profile photo"
                    />
                </div>
                <label class="block">
                    <span class="sr-only">Choisissez une photo de profil</span>
                    <input
                        type="file"
                        @change="loadFile"
                        class="block w-full text-sm text-slate-500 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-100 file:text-green-700 hover:file:bg-green-200 hover:file:cursor-pointer"
                    />
                </label>
            </div>
            <div class="flex gap-2 w-full">
                <Input
                    class="min-w-0 w-full"
                    name="Prénom"
                    v-model="firstName"
                    placeholder="Ex: John"
                />
                <Input
                    class="min-w-0 w-full"
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
                class="bg-green-600 hover:bg-green-700 text-white disabled:bg-gray-400" :disabled="isLoading"
                >Enregistrer</Button
            >
        </form>

        <form
            @submit.prevent="handleDelete"
            class="flex flex-col gap-4 w-5/6 mt-5 mx-auto md:w-1/2 lg:w-1/3"
        >
            <Button type="submit" class="bg-red-600 hover:bg-red-700 text-white disabled:bg-gray-400" :disabled="isLoading"
                >Supprimer votre compte</Button
            >
        </form>
    </div>
</template>
