<script setup>
import Box from './Box.vue';
const props = defineProps({
    image: {
        type: String,
        default: 'https://i0.wp.com/pygear.com/wp-content/uploads/2024/05/Shrek-micro-penis.jpg?fit=1080%2C1038&ssl=1'
    },
    alt: {
        type: String,
        default: 'Plant'
    }
    
})
</script>
<template>
    <Box>
      <img :src="props.image" :alt="props.alt" class="w-full h-48 object-cover">
      <div class="p-4"><slot></slot></div>
    </Box>
</template>