<script setup>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faEye, faEyeSlash } from '@fortawesome/free-regular-svg-icons'
import { computed, ref } from 'vue'

const props = defineProps({
    modelValue: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'text'
    },
    placeholder: {
      type: String,
      default: ''
    },
    required: {
      type: Boolean,
      default: false
    },
    errorMessage: {
      type: String,
      default: ''
    }
})

const showPassword = ref(false)

const inputType = computed(() => {
    if (props.type === 'password') {
        return showPassword.value ? 'text' : 'password'
    }
    return props.type
})

const hasError = computed(() => props.errorMessage !== '')
</script>

<template>
 <div class="flex flex-col gap-2">
    <label :for="name" class="text-sm font-medium text-gray-500">{{ name }}<span v-if="required" class="ml-1 text-red-500">*</span></label>
    <div class="relative w-full">
      <input 
        :type="inputType" 
        :placeholder="placeholder" 
        :required="required" 
        class="p-2 rounded-md border border-slate-300 outline-green-600 w-full"
        :class="{ 'outline-red-500': hasError }" 
        :value="modelValue" 
        @input="$emit('update:modelValue', $event.target.value)" 
      />
      
      <button 
        v-if="props.type === 'password'" 
        type="button" 
        @click="showPassword = !showPassword" 
        class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-600"
      >
        <FontAwesomeIcon :icon="faEye" v-if="!showPassword" />
        <FontAwesomeIcon :icon="faEyeSlash" v-else />
      </button>
    </div>
    <p v-if="hasError" class="text-red-500 text-sm">{{ errorMessage }}</p>
  </div>
</template>