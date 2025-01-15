<script setup>
import { RouterView } from "vue-router";
import Navbar from "@/components/Navbar.vue";
import Footer from "../components/Footer.vue";
import MobileNavbar from "../components/MobileNavbar.vue";
import {onMounted, onUnmounted, ref, watch} from "vue";
import {useWebSocketStore} from "@/stores/useWebSocketStore.js";

const latestMessage = ref("");
const notif = ref(null);
import {
  NewspaperIcon,
  CalendarIcon,
  UsersIcon,
  UserIcon,
  HelpCircleIcon, BellIcon, LayoutDashboard,
} from "lucide-vue-next";
import NotificationPopup from "@/components/Notification.vue";
const webSocketStore = useWebSocketStore();
const notifications = webSocketStore.notifications;
watch(
    () => notifications,
    (newVal) => {
      if (newVal.length > 0) {
        const newNotification = newVal[newVal.length - 1];
        latestMessage.value = newNotification.message || "Nouvelle notification !";


        notif.value.show();
      }
    },
    { deep: true }
);
const menuItems = [
  { name: "Fil d'actualité", path: "/posts", icon: NewspaperIcon },
  { name: "Calendrier", path: "/calendar", icon: CalendarIcon },
  { name: "Communauté", path: "/community", icon: UsersIcon },
  { name: "Mon profil", path: "/profile", icon: UserIcon },
  { name: "Notification", path: "/notification", icon: BellIcon },
  { name: "DashBoard", path: "/dashboard", icon:  LayoutDashboard },
  { name: "Aide", path: "/help", icon: HelpCircleIcon },
];

const isMobileScreen = ref(false);

const checkIfMobileScreen = () => {
  if (window.innerWidth <= 767) {
    isMobileScreen.value = true;
    return;
  }

  isMobileScreen.value = false;
};

onMounted(async () => {
  checkIfMobileScreen();
  window.addEventListener("resize", checkIfMobileScreen);
  try {
    await webSocketStore.connectWebSocket();
  } catch (error) {
    console.error("Erreur lors de la connexion WebSocket :", error);
  }
});

onUnmounted(async () => {
  window.removeEventListener("resize", checkIfMobileScreen);
  try {
    await webSocketStore.disconnectWebSocket();
  } catch (error) {
    console.error("Erreur lors de la déconnexion WebSocket :", error);
  }

});
</script>

<template>

  <div class="min-h-screen flex flex-col md:grid md:grid-cols-[auto_1fr] gap-4">
    <MobileNavbar :menuItems="menuItems" v-if="isMobileScreen" />
    <Navbar :menuItems="menuItems" v-else />

    <div class="w-full flex flex-col mt-5">
      <main class="mb-5">
        <RouterView v-slot="{ Component }">
          <component :is="Component"></component>
        </RouterView>

      </main>


      <NotificationPopup ref="notif" :message="latestMessage" :duration="3000" />

      <Footer v-if="!isMobileScreen"></Footer>
    </div>
  </div>
</template>

