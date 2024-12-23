
import { defineStore } from 'pinia';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications: [],
        stompClient: null,
    }),

    actions: {
        connectWebSocket() {
            const client = new Client({
                brokerURL: null, // Pas besoin d'URL directe, car SockJS est utilisé
                webSocketFactory: () => new SockJS('http://localhost:8080/ws'), // URL de ton serveur SockJS
                onConnect: () => {
                    console.log('WebSocket connecté');
                    client.subscribe('/topic/notifications', (message) => {
                        console.log('Notification reçue :', message.body);
                        // Mets à jour l'état de ton store ici si nécessaire
                    });
                },
                debug: (str) => console.log(str), // Pour le débogage
            });

            client.activate();
        },

        disconnectWebSocket() {
            if (this.stompClient) {
                this.stompClient.disconnect(() => {
                    console.log('Disconnected from WebSocket');
                });
                this.stompClient = null;
            }
        },

        addNotification(message) {
            this.notifications.push(message);
        },

        clearNotifications() {
            this.notifications = [];
        },
    },
});