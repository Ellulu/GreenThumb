<script setup>
import { ref, defineProps, onMounted } from 'vue';
import Button from "@/components/Button.vue";
import Title_3 from "@/components/Title_3.vue";

const props = defineProps({
  dailyTask: {
    type: Array,
    required: true
  }
});

const showFullDescriptions = ref([]);

onMounted(() => {
  showFullDescriptions.value = props.dailyTask.map(() => false);
});

function toggleDescription(index) {
  showFullDescriptions.value[index] = !showFullDescriptions.value[index];
}
</script>

<template>
  <div class="mx-auto max-w-lg">
    <div
        v-for="(task, index) in dailyTask"
        :key="index"
        class="border-2 border-green-600 rounded-lg bg-white w-[90%] mx-auto p-4 mb-4 flex flex-col gap-2 text-center shadow-sm"
    >
      <Title_3 class="text-green-600">{{ task.title }}</Title_3>
      <p>
        <span>Plante: {{ task.plant?.name || 'Plante inconnue' }}</span>
      </p>
      <p class="break-words overflow-hidden">
        <span>Description: </span>
        <span v-if="!showFullDescriptions[index]">
          {{ task.description?.substring(0, 20) || 'Pas de description' }}...
        </span>
        <span v-else>{{ task.description || 'Pas de description' }}</span>
      </p>
      <Button
          @click="toggleDescription(index)"
          class="bg-green-600 text-white py-1 px-3 rounded"
      >
        {{ showFullDescriptions[index] ? '-' : '+' }}
      </Button>


      <div class="flex gap-2 justify-center mt-4">
        <slot :task="task" :index="index"></slot>
      </div>
    </div>
  </div>
</template>
