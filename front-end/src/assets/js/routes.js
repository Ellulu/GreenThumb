import { createWebHistory, createRouter } from "vue-router";
import { onAuthStateChanged } from "firebase/auth";
import { useUserStore } from "../../stores/userStore";
import { auth } from "./firebase";

import DashboardView from "../../views/DashboardView.vue";
import HomeView from "../../views/HomeView.vue";
import LoginView from "../../views/LoginView.vue";
import RegisterView from "../../views/RegisterView.vue";
import NotFoundView from "../../views/NotFoundView.vue";
import TestView from "../../views/TestView.vue";
import MainLayout from "../../layout/MainLayout.vue";
import PlantView from "../../views/PlantView.vue";
import NoteFormView from "@/views/NoteFormView.vue";
import PostsView from "@/views/PostsView.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import ProfileView from "../../views/ProfileView.vue";
import CalendarView from "@/views/CalendarView.vue";

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
    children: [
      {
        path: "",
        component: HomeView,
      },
      {
        path: "test",
        component: TestView,
      },
      {
        path: "profile/notes",
        component: NoteFormView,
      },
      {
        path: "profile/plants",
        component: PlantView,
      },
      {
        path: "profile/edit",
        component: EditProfileView,
        beforeEnter: (to, from, next) => checkAuth(next),
      },
      {
        path: "posts",
        component: PostsView,
      },
      {
        path: "calendar",
        component: CalendarView,
      },
      {
        path: "profile",
        component: ProfileView,
      },
    ],
  },
  { path: "/login", component: LoginView },
  { path: "/register", component: RegisterView },
  {
    path: "/dashboard",
    component: DashboardView,
    beforeEnter: (to, from, next) => checkAuth(next),
  },
  { path: "/404", component: NotFoundView },
  { path: "/:pathMatch(.*)*", redirect: "/404" },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
