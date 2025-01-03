import { defineStore } from "pinia";
import { ref } from "vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { getAuth, onAuthStateChanged } from "firebase/auth";

export const useWebSocketStore = defineStore("websocket", () => {
    const notifications = ref([]);
    let stompClient = null; // Instance WebSocket


    const connectWebSocket = async () => {
        const auth = getAuth();

        // Observer l'état de connexion utilisateur
        onAuthStateChanged(auth, async (user) => {
            if (!user) {
                console.error("Utilisateur non connecté");
                return;
            }

            try {
                const token = await user.getIdToken();

                const socket = new SockJS("http://localhost:8080/notifications");
                stompClient = Stomp.over(socket);


                stompClient.connect(
                    { Authorization: `Bearer ${token}` },
                    () => {
                        console.log("WebSocket connecté");


                        stompClient.subscribe("/user/queue/notifications", (message) => {
                            const payload = JSON.parse(message.body);
                            console.log("Notification reçue :", payload);


                            notifications.value.push({
                                id: new Date().getTime(),
                                title: payload.title || "Notification",
                                body: payload.body || "",
                            });
                        });
                    },
                    (error) => {
                        console.error("Erreur de connexion WebSocket :", error);
                    }
                );
            } catch (error) {
                console.error("Erreur lors de la récupération du token Firebase :", error);
            }
        });
    };


    const disconnectWebSocket = () => {
        if (stompClient) {
            stompClient.disconnect(() => {
                console.log("WebSocket déconnecté");
            });
            stompClient = null;
        }
    };

    return { notifications, connectWebSocket, disconnectWebSocket };
});
