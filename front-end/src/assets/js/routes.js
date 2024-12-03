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

const checkAuth = (to, from, next) => {
  const userStore = useUserStore();

  if (userStore.user) {
    console.log("Utilisateur connecté :", userStore.user);
    next();
  } else {
    console.log("Utilisateur déconnecté");
    next("/login");
  }
};

const routes = [
  {
    path: "/",
    component: MainLayout,
    children: [
      {
        path: "",
        component: HomeView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "test",
        component: TestView,
      },
      {
        path: "profile/notes",
        component: NoteFormView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "profile/plants",
        component: PlantView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "profile/edit",
        component: EditProfileView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "posts",
        component: PostsView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "calendar",
        component: CalendarView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
      {
        path: "profile",
        component: ProfileView,
        beforeEnter: (to, from, next)=>checkAuth(to, from, next),
      },
    ],
  },
  { path: "/login", component: LoginView },
  { path: "/register", component: RegisterView },
  {
    path: "/dashboard",
    component: DashboardView,
    beforeEnter: checkAuth,
  },
  { path: "/404", component: NotFoundView },
  { path: "/:pathMatch(.*)*", redirect: "/404" },
];


const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
