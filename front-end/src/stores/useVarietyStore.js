import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useVarietyStore = defineStore('variety', {
  state: () => ({
    varieties: [],
    variety: null,
    error: null,
  }),
  actions: {
    async fetchVarieties() {
      try {
        const response = await APIService.get('/varieties');
        this.varieties = response.data;
      } catch (error) {
        this.error = 'Failed to load varieties';
      }
    },
    async fetchVariety(id) {
      try {
        const response = await APIService.get(`/varieties/${id}`);
        this.variety = response.data;
      } catch (error) {
        this.error = `Failed to load variety with id: ${id}`;
      }
    },
    async createVariety(varietyData) {
      try {
        await APIService.post('/varieties', varietyData);
      } catch (error) {
        this.error = 'Failed to create variety';
      }
    },
    async deleteVariety(id) {
      try {
        await APIService.delete(`/varieties/${id}`);
      } catch (error) {
        this.error = `Failed to delete variety with id: ${id}`;
      }
    }
  }
});
