<script setup>
const props = defineProps({
    value: {
        type: Number,
        default: 0
    },
    color:{
        type: String,
        default: 'bg-yellow-500'
    }
})
</script>
<template>
    <div class="bg-gray-200 w-full rounded-full h-2">
        <div class="rounded-full h-2" :class='props.color' :style="{  width: `${props.value}%` }"></div>
    </div>
</template>