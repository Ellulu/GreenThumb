<script setup>
import Box from './Box.vue';
const props = defineProps({
    image: {
        type: String,
        default: 'https://www.pessleux.be/files/images/news/les-meilleurs-conseils-pour-une-magnifique-monstera-1000x667-66f5720b71845_og.jpg'
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