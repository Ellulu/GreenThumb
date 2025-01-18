<template>
  <div
      v-if="isVisible"
      class="fixed top-0 left-1/2 transform -translate-x-1/2 bg-green-600 text-white font-medium py-3 px-6 rounded-lg shadow-lg
           transition-transform duration-500 ease-in-out z-50"
      :class="popupVisible ? 'translate-y-0' : '-translate-y-full'"
  >
    <span>{{ message }}</span>
  </div>
</template>

<script setup>
import { ref } from 'vue';


const props = defineProps({
  message: {
    type: String,
    default: 'Action effectuée avec succès !',
  },
  duration: {
    type: Number,
    default: 3000,
  },
});

const isVisible = ref(false);
const popupVisible = ref(false);
let timeoutId = null;

function show() {

  if (timeoutId) {
    clearTimeout(timeoutId);
    timeoutId = null;
  }

  isVisible.value = true;


  setTimeout(() => {
    popupVisible.value = true;
  }, 10);


  timeoutId = setTimeout(() => {
    popupVisible.value = false;


    setTimeout(() => {
      isVisible.value = false;
    }, 500);
  }, props.duration);
}

defineExpose({
  show,
});
</script>
