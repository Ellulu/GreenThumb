import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useUserStore = defineStore('user', {
  state: () => ({
    users: [],
    user: null,
    userCheck: null,
    error: null,
  }),
  actions: {
    async fetchUsers() {
      try {
        const response = await APIService.get('/users');
        this.users = response.data; 
      } catch (error) {
        this.error = 'Failed to load users';
      }
    },
    async subscribe(userId) {
      try {
        await APIService.post(`/users/${userId}/subscribe`);
      } catch (error) {
        console.error("Erreur lors de l'abonnement :", error);
      }
    },
    async unsubscribe(userId) {
      try {
        await APIService.post(`/users/${userId}/unsubscribe`);
      } catch (error) {
        console.error("Erreur lors du désabonnement :", error);
      }
    },
    async fetchUser(id) {
      try {
        const response = await APIService.get(`/users/${id}`);
        this.user = response.data; 
      } catch (error) {
        this.error = `Failed to load user with id: ${id}`;
      }
    },
    async createUser(userData) {
      try {
        await APIService.post('/users', userData);
      } catch (error) {
        this.error = 'Failed to create user';
      }
    },
    async deleteUser(id) {
      try {
        await APIService.delete(`/users/${id}`);
      } catch (error) {
        this.error = `Failed to delete user with id: ${id}`;
      }
    }
  }
});
