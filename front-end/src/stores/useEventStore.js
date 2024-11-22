import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    event: null,
    error: null,
  }),
  actions: {
     async fetchEvents(startDate, endDate) {
         try {

        const response = await APIService.get('/events');
        let evts = response.data;



        let enventList = [];
      for(let evt of evts) {


        for (let date = new Date(startDate); date <= endDate; date.setDate(date.getDate() + 1)) {


            const currentDate = new Date(date);
            const setupDate = new Date(evt.eventDate);



            const diffDays = Math.floor((currentDate - setupDate) / (1000 * 60 * 60 * 24)); // Arrondir au jour près


            if (diffDays >= 0 && diffDays % evt.cycle === 0) {


                let newEvent = { ...evt };

                newEvent.eventDate = new Date(currentDate);

                enventList.push(newEvent);
            }

        }
      }
      this.events = enventList;


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
        console.log(eventData);
        await APIService.post('/events', eventData);
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
