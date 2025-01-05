import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useDBUserStore = defineStore('dbUser', {
  state: () => ({
    users: [],
    user: {
      following: [], 
    },
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
    async followUser(userId) {
      try {
        await APIService.post(`/users/${userId}/follow`);
        if (!this.user.following) this.user.following = [];
        this.user.following.push(userId);
      } catch (error) {
        console.error("Erreur lors du suivi de l'utilisateur :", error);
      }
    },
    
    async unfollowUser(userId) {
      try {
        await APIService.post(`/users/${userId}/unfollow`);
        if (this.user.following) {
          this.user.following = this.user.following.filter(id => id !== userId);
        }
      } catch (error) {
        console.error("Erreur lors du désabonnement :", error);
      }
    },
    async fetchUser(id) {
      try {
        const response = await APIService.get(`/users/${id}`);
        this.user = response.data; 
        console.log(this.user)
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
