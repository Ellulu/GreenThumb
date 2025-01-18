import ApiService from "@/services/ApiService.js";
import {defineStore} from "pinia";

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications: [],
        error: null
    }),
    actions: {
        async fetchNotifications() {
            try {
                const response = await ApiService.post('/notifications/get');
                this.notifications = response.data;
            } catch (error) {
                this.error = 'Failed to load notifications';
            }
        },
        async deleteNotification(id) {
            try {
                await ApiService.delete(`/notifications/${id}`);


            } catch (error) {
                this.error = `Failed to delete notification with id: ${id}`;
            }
        }
    }
});