<script setup>
const props = defineProps({
    type: {
        type: String,
        default: 'button'
    }
})
</script>

<template>
    <button :type="type" class="bg-green-600 hover:bg-green-700 transition-colors duration-300 ease-in-out text-white p-2 rounded-md">
        <slot></slot>
    </button>
</template>