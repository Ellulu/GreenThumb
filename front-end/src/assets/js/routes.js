import { createWebHistory, createRouter } from 'vue-router'

import HomeView from '../../views/HomeView.vue'
import LoginView from '../../views/LoginView.vue'
import NotFoundView from '../../views/NotFoundView.vue'
import TestView from '../../views/TestView.vue'
import MainLayout from '../../layout/main_layout.vue'

const routes = [
  {
    path: '/',
    component: MainLayout, // Layout commun
    children: [
      {
        path: '', // Route par défaut (Home)
        component: HomeView,
      },
      {
        path: 'test', // Route pour TestView
        component: TestView,
      },
    ],
  },
  
  { path: '/login', component: LoginView }, // Route sans layout
  { path: '/404', component: NotFoundView }, // Page 404
  { path: '/:pathMatch(.*)*', redirect: '/404' }, // Redirection pour les chemins non valides
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
