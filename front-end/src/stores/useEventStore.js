import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';
import * as events from "node:events";
export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    event: null,
    error: null
  }),
  actions: {
     async fetchEvents(startDate, endDate) {
         try {

             console.log(startDate);
                console.log(endDate);
             const response = await APIService.post('/events/get', {
                 startDate: startDate,
                 endDate: endDate
             });

                this.events =  response.data;

return response.data;
         } catch (error) {
            this.error = 'Failed to load events';
         }



    }


    ,

    async fetchEvent(id) {
      try {
        const response = await APIService.get(`/events/${id}`);
        this.event = response.data; 
      } catch (error) {
        this.error = `Failed to load event with id: ${id}`;
      }
    },    async fetchAllEvent() {
      try {

        const response = await APIService.post(`/events/all`);

        this.events = response.data;

      } catch (error) {
        this.error = `Failed to load event with id: ${id}`;
      }
    },
    async createEvent(eventData) {
      try {
          await APIService.post('/events/create', eventData.value);
      } catch (error) {
        this.error = 'Failed to create event';
      }
    },async editEvent(id,eventData) {
      try {
            await APIService.put(`/events/${id}`, eventData.value);

      } catch (error) {
        this.error = 'Failed to create event';
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
