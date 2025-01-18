import { defineStore } from "pinia";
import { ref } from "vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { getAuth, onAuthStateChanged } from "firebase/auth";

export const useWebSocketStore = defineStore("websocket", () => {
    const notifications = ref([]);
    let stompClient = null;
    let reconnectAttempts = 0;
    const maxReconnectAttempts = 10;
    let reconnectTimeout = null;

    const connectWebSocket = async () => {
        const auth = getAuth();

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

                        // Réinitialiser le compteur de reconnexion
                        reconnectAttempts = 0;

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
                        attemptReconnect();
                    }
                );
            } catch (error) {
                console.error("Erreur lors de la récupération du token Firebase :", error);
                attemptReconnect();
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

        if (reconnectTimeout) {
            clearTimeout(reconnectTimeout);
            reconnectTimeout = null;
        }
    };

    const attemptReconnect = () => {
        if (reconnectAttempts >= maxReconnectAttempts) {
            console.error("Nombre maximal de tentatives de reconnexion atteint");
            return;
        }

        reconnectAttempts++;
        const delay = Math.min(5000, 1000 * Math.pow(2, reconnectAttempts));
        console.log(`Tentative de reconnexion dans ${delay / 1000} secondes...`);

        reconnectTimeout = setTimeout(() => {
            connectWebSocket();
        }, delay);
    };

    return { notifications, connectWebSocket, disconnectWebSocket };
});
