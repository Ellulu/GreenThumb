import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';
import { useUserStore } from '@/stores/useUserStore';
export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    event: null,
    error: null
  }),
  actions: {
     async fetchEvents(startDate, endDate) {
         try {

             console.log("fetching events")
             const response = await APIService.post(`/events/zUaX99sOrsUNMrcx9SmU9YhJfXp2`, {
                 startDate: startDate,
                 endDate: endDate
             });
                console.log("response received")
             console.log(response.data)
                this.events =  response.data;




         } catch (error) {
            this.error = 'Failed to load events';
         }



    }

    /*
    async fetchToDayEvent(dalitasks,date){


    }*/
    ,

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
          const userStore = useUserStore();

    eventData.value.user = userStore.user



        await APIService.post('/events', eventData.value);
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
