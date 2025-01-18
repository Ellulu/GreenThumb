import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useDBUserStore = defineStore('dbUser', {
  state: () => ({
    users: [],
    user: null,
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
    async followUser(user) {
      try {
        await APIService.post(`/users/${user.uid}/follow`);
        if (!this.user.following) this.user.following = [];
        this.user.following.push(user);
      } catch (error) {
        console.error("Erreur lors du suivi de l'utilisateur :", error);
      }
    },
    
    async unfollowUser(user) {
      try {
        await APIService.post(`/users/${user.uid}/unfollow`);
        if (this.user.following) {
          this.user.following = this.user.following.filter(following => following.uid !== user.uid);
        }

      } catch (error) {
        console.error("Erreur lors du désabonnement :", error);
      }
    },
    async fetchUser(id) {
      try {
        const response = await APIService.get(`/users/${id}`);
        this.user = response.data; 
        if (!this.user.following){
          this.user.following = [];
        }
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
