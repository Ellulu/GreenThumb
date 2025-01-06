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
                console.log("fetching notifications");
                const response = await ApiService.post('/notifications/get');
                console.log("response received");
                console.log(response.data);
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