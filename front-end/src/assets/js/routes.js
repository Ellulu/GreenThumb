import { createWebHistory, createRouter } from 'vue-router'

import HomeView from '../../views/HomeView.vue'
import LoginView from '../../views/LoginView.vue'
import NotFoundView from '../../views/NotFoundView.vue'
import TestView from '../../views/TestView.vue'
import MainLayout from '../../layout/main_layout.vue'
import PlantView from '../../views/PlantView.vue'
import NoteFormView from "@/views/NoteFormView.vue";
import PostsView from "@/views/PostsView.vue";

const routes = [
  {
    path: '/',
    component: MainLayout, 
    children: [
      {
        path: '',
        component: HomeView,
      },
      {
        path: 'test',
        component: TestView,
      },{
        path: 'notes',
        component: NoteFormView,
      },
      {
        path: 'plants',
        component: PlantView,
      },
      {
        path: 'posts',
        component: PostsView,
      },
    ],
  },
  
  { path: '/login', component: LoginView },
  { path: '/404', component: NotFoundView },
  { path: '/:pathMatch(.*)*', redirect: '/404' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
