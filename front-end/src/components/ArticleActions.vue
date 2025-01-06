<template>
    <div class="flex space-x-4 text-green-600">
      <button @click="handleLike" class="flex items-center space-x-1 hover:text-green-900">
        <ThumbsUpIcon :class="{'text-green-900': article.rating.hasLike}" class="w-5 h-5" />
        <span>{{ article.rating.likes }}</span>
      </button>
      <button @click="handleDislike" class="flex items-center space-x-1 hover:text-red-600">
        <ThumbsDownIcon :class="{'text-red-600': article.rating.hasDislike}" class="w-5 h-5" />
        <span>{{ article.rating.dislikes }}</span>
      </button>
      <button @click="toggleComments" class="flex items-center space-x-1 hover:text-blue-600">
        <MessageCircleIcon :class="{'text-blue-600': article.showComments}" class="w-5 h-5" />
        <span>{{ article.comments ? article.comments.length : 0 }}</span>
      </button>
    </div>
  </template>
  
  <script setup>
  import { ThumbsUpIcon, ThumbsDownIcon, MessageCircleIcon } from 'lucide-vue-next';
  
  const props = defineProps({
    article: {
      type: Object,
      required: true,
    },
  });
  
  const emit = defineEmits(['like', 'dislike', 'toggle-comments']);
  
  const handleLike = () => emit('like');
  const handleDislike = () => emit('dislike');
  const toggleComments = () => emit('toggle-comments');
  </script>
  