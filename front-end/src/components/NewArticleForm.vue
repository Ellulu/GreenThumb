<template>
  <div v-if="authUserStore.user" class="rounded-lg shadow p-4 mb-4">
    <Input
      v-model="localTitle"
      name="Titre de l'article"
      placeholder="Titre de l'article"
      required
    />
    <textarea
      v-model="localContent"
      placeholder="Quoi de neuf ?"
      class="w-full h-20 resize-none border-b border-green-200 focus:outline-none focus:border-green-500 mb-4"
    ></textarea>
    <div class="flex justify-between items-center">
      <div class="flex space-x-2 text-green-600">
        <label
          for="fileInput"
          class="p-2 rounded-full hover:bg-green-100 transition cursor-pointer"
        >
          <ImageIcon class="w-5 h-5" />
        </label>
        <input
          type="file"
          id="fileInput"
          multiple
          accept="image/*,video/*"
          @change="onFileUpload"
          class="hidden"
        />
      </div>
      <button
        @click="submitArticle"
        class="bg-green-500 text-white px-4 py-2 rounded-full font-bold hover:bg-green-600 transition"
        :disabled="!localTitle.trim() || !localContent.trim()"
      >
        Nouvel Article
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import Input from '@/components/Input.vue';
import { ImageIcon } from 'lucide-vue-next';

const emit = defineEmits(['submit-article']);
const authUserStore = useUserStore();
const localTitle = ref('');
const localContent = ref('');
const files = ref([]);

const onFileUpload = (event) => {
  files.value = Array.from(event.target.files);
};

const submitArticle = () => {
  if (localTitle.value.trim() && localContent.value.trim()) {
    const newArticle = {
      title: localTitle.value,
      content: localContent.value,
      files: files.value,
    };
    emit('submit-article', newArticle);

    files.value = [];
    localTitle.value = '';
    localContent.value = '';
  }
};
</script>
