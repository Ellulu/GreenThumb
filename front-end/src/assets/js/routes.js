import { createWebHistory, createRouter } from 'vue-router'
import { useUserStore } from '../../stores/userStore'

import DashboardView from '../../views/DashboardView.vue'
import HomeView from '../../views/HomeView.vue'
import LoginView from '../../views/LoginView.vue'
import RegisterView from '../../views/RegisterView.vue'
import NotFoundView from '../../views/NotFoundView.vue'
import TestView from '../../views/TestView.vue'

const routes = [
  { path: '/', component: HomeView },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView },
  { 
    path: '/dashboard', 
    component: DashboardView, 
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore()
      if (!userStore.isLoggedIn) {
        next('/')
      } else {
        next()
      }
    },
  },
  { path: '/test', component: TestView },

  { path: '/404', component: NotFoundView },
  { path: '/:pathMatch(.*)*', redirect: '/404' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router