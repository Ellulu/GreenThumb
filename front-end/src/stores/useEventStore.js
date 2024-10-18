import { defineStore } from 'pinia';
import APIService from '@/services/APIService';

export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    event: null,
    error: null,
  }),
  actions: {
    async fetchEvents() {
      try {
        const response = await APIService.get('/events');
        this.events = response.data; 
      } catch (error) {
        this.error = 'Failed to load events';
      }
    },
    async fetchEvent(id) {
      try {
        const response = await APIService.get(`/events/${id}`);
        this.event = response.data; 
      } catch (error) {
        this.error = `Failed to load event with id: ${id}`;
      }
    },
    async createEvent(eventData) {
      try {
        await APIService.post('/events', eventData);
      } catch (error) {
        this.error = 'Failed to create event';
      }
    },
    async updateEvent(id, eventData) {
      try {
        await APIService.put(`/events/${id}`, eventData);
      } catch (error) {
        this.error = `Failed to update event with id: ${id}`;
      }
    },
    async deleteEvent(id) {
      try {
        await APIService.delete(`/events/${id}`);
      } catch (error) {
        this.error = `Failed to delete event with id: ${id}`;
      }
    }
  }
});
