import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './assets/css/style.css'
import App from './App.vue'
import router from './assets/js/routes'

createApp(App).use(router).use(createPinia()).mount('#app')
