<script setup>
import {ref} from 'vue';
import { defineProps, onMounted } from 'vue';
import Button from "@/components/Button.vue";
import Title_2 from "@/components/Title_2.vue";
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
  <div>

    <div
        v-for="(task, index) in dailyTask"
        :key="index"
        class="border-green-600 bg border-2 p-4 mb-4 flex flex-col gap-2 text-center"
    >
      <title_3 class="text-green-600">{{ task.title }}</title_3>
      <p>
      <span >
         Plante: {{task.plant.name}}
        </span>
      </p>

      <p>
        <span >Description: </span>
        <span v-if="!showFullDescriptions[index]">
          {{ task.description.substring(0, 20) }}...
        </span>

        <span v-else> {{ task.description }}</span>
      </p>
      <Button
          @click="toggleDescription(index)"
          class="bg-green-600 text-white py-1 px-3 rounded"
      >
        {{ showFullDescriptions[index] ? '-' : '+' }}
      </Button>

    </div>
  </div>
</template>