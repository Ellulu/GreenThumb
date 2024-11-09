<template>
  <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
    <div class="bg-white p-8 rounded-lg shadow-xl w-full max-w-md">

      <h2 class="text-2xl font-bold text-green-600 mb-4">{{ title }}</h2>


      <form @submit.prevent >

        <slot name="form-content"></slot>

        <div class="flex justify-end mt-4">

          <slot name="cancel-button">
            <button @click="closeModal" type="button" class="bg-red-500 hover:bg-gray-400 text-white font-bold py-1 px-3 rounded mr-2">
              Annuler
            </button>
          </slot>

          <slot name="submit-button">
            <Button type="submit" class="bg-green-600 hover:bg-primary-dark text-white font-bold py-1 px-3 rounded">
              {{ submitLabel }}
            </Button>
          </slot>
        </div>
      </form>
    </div>
  </div>
</template>

<script>

import Button from "@/components/Button.vue";
export default {
  components: {Button},
  props: {
    show: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      default: 'Formulaire'
    },
    submitLabel: {
      type: String,
      default: 'Soumettre'
    }
  },
  methods: {

    closeModal() {
      this.$emit('close');
    }
  }
}
</script>
