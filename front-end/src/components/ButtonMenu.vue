<script setup>
import { ref } from 'vue';
import { EllipsisVertical } from 'lucide-vue-next';
import ButtonItem from './ButtonItem.vue';

const showMenu = ref(false);

defineProps({
    items: {
        type: Array,
        required: true
    }
});
</script>

<template>
    <div class="relative">
        <button @click="showMenu = !showMenu" class="p-2 rounded-full hover:bg-gray-100 transition-all ease-in-out duration-500">
            <EllipsisVertical />
        </button>

        <div v-show="showMenu" class="absolute right-0 z-10 p-4 rounded-md bg-white flex flex-col gap-2 justify-center shadow-md">
            <ButtonItem 
            v-for="(item, index) in items" 
            :key="index"
            :icon="item.icon"
            :text="item.text"
            :clickFunction="item.clickFunction"
            ></ButtonItem>
        </div>
    </div>
</template>