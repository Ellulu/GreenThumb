import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import './assets/css/style.css'
import App from './App.vue'
import router from './assets/js/routes'
import './assets/css/main.css'
import { useUserStore } from "@/stores/userStore";

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(pinia)
app.use(router)
const userStore = useUserStore();
userStore.initializeUser().then(() => {
  app.mount("#app");
});