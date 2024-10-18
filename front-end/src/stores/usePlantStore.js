import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const usePlantStore = defineStore('plant', {
  state: () => ({
    plants: [],
    plant: null,
    error: null,
  }),
  actions: {
    async fetchPlants() {
      try {
        const response = await APIService.get('/plants');
        this.plants = response.data;
      } catch (error) {
        this.error = 'Failed to load plants';
      }
    },
    async fetchPlant(id) {
      try {
        const response = await APIService.get(`/plants/${id}`);
        this.plant = response.data;
      } catch (error) {
        this.error = `Failed to load plant with id: ${id}`;
      }
    },
    async createPlant(plantData) {
      try {
        await APIService.post('/plants', plantData);
      } catch (error) {
        this.error = 'Failed to create plant';
      }
    },
    async deletePlant(id) {
      try {
        await APIService.delete(`/plants/${id}`);
      } catch (error) {
        this.error = `Failed to delete plant with id: ${id}`;
      }
    }
  }
});
