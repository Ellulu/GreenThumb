<script setup>
defineProps({
    icon: {
        type: Object,
        required: true
    },
    text: {
        type: String,
        required: true
    },
    clickFunction: {
        type: Function,
        required: false
    }
});
</script>

<template>
    <div @click="clickFunction" class="flex gap-2 items-center p-2 hover:bg-gray-200 rounded-md transition-all ease-in-out duration-200 w-full cursor-pointer">
        <component :is="icon" class="w-4 h-4" />
        <p>{{ text }}</p>
    </div>
</template>