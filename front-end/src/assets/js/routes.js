import { createWebHistory, createRouter } from 'vue-router'

import HomeView from '../../views/HomeView.vue'
import LoginView from '../../views/LoginView.vue'
import RegisterView from '../../views/RegisterView.vue'
import NotFoundView from '../../views/NotFoundView.vue'
import TestView from '../../views/TestView.vue'

const routes = [
  { path: '/', component: HomeView },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView },
  
  { path: '/test', component: TestView },
  
  { path: '/404', component: NotFoundView },
  { path: '/:pathMatch(.*)*', redirect: '/404' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router