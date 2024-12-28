import { createWebHistory, createRouter } from "vue-router";
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "./firebase";

import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import TestView from "@/views/TestView.vue";
import MainLayout from "@/layout/MainLayout.vue";
import PlantView from "@/views/PlantView.vue";
import NoteFormView from "@/views/NoteFormView.vue";
import PostsView from "@/views/PostsView.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import ProfileView from "@/views/ProfileView.vue";
import CalendarView from "@/views/CalendarView.vue";
import DashboardView from "@/views/DashboardView.vue";
import EditEventView from "@/views/EditEventView.vue";

const checkAuth = (next) => {
  onAuthStateChanged(auth, (user) => {
    if (user) {
      console.log("Utilisateur connecté :", user);
      next();
    } else {
      console.log("Utilisateur déconnecté");
      next("/login");
    }
  })
}

const routes = [
  {
    path: "/",
    component: MainLayout,
    redirect: {path: "posts"},
    children: [
      {
        path: "test",
        component: TestView,
      },
      {
        path: "dashboard",
        component: DashboardView,
      },
      {
        path: "posts",
        component: PostsView,
      },
      {
        path: "calendar",
        component: CalendarView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "calendar/editevents",
        component: EditEventView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "profile",
        component: ProfileView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "profile/notes",
        component: NoteFormView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "profile/plants",
        component: PlantView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "profile/edit",
        component: EditProfileView,
        beforeEnter: (to, from, next) => checkAuth(next),
      }
    ],
  },
  { path: "/login", component: LoginView },
  { path: "/register", component: RegisterView },
  { path: "/404", component: NotFoundView },
  { path: "/:pathMatch(.*)*", redirect: "/404" },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
