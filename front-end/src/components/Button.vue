<script setup>
const props = defineProps({
    type: {
        type: String,
        default: 'button'
    }
})
</script>

<template>
    <button :type="type" class="transition-colors duration-300 ease-in-out p-2 rounded-md font-bold">
        <slot></slot>
    </button>
</template>