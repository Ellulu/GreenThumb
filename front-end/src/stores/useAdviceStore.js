import { ref } from 'vue';
import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useAdviceStore = defineStore('advice', {
    states: () => ({
        advices: ref([]),
        advice: null,
        error: null,
    }),
    actions: {
        async fetchAdvices(page) {
            try {
                console.log("sending request...");
                const response = await APIService.get(`/advices/page/${page}`);
                console.log(response.data);
                this.advices.value = response.data;
            } catch (error) {
                this.error = `Failed to load advices for page: ${page}`;
            }
        },

        async fetchAdvice(id) {
            try {
                const response = await APIService.get(`/advices/${id}`);
                this.advice = response.data;
            } catch (error) {
                this.error = `Failed to load advice with id: ${id}`;
            }
        },

        async createAdvice(adviceData, pictures) {
            try {
                console.log("Pictures to upload:", pictures);
                const formData = new FormData();
                
                // Ajouter l'advice JSON
                formData.append("advice", JSON.stringify(adviceData));
                
                // Ajouter chaque fichier individuellement
                if (pictures && pictures.length > 0) {
                    pictures.forEach((picture) => {
                    formData.append("pictures", picture); // Ajoute chaque fichier
                    });
                }
                
                // Affiche le contenu du FormData avant l'envoi
                console.log([...formData.entries()]);
                
                // Envoie la requête
                await APIService.postWithMultipart('/advices', formData);
            } catch (error) {
                console.error('Erreur lors de la création de l\'advice :', error);
            }
        },

        async deleteAdvice(id) {
            try {
                await APIService.delete(`/advices/${id}`);
            } catch (error) {
                this.error = `Failed to delete advice with id: ${id}`;
            }
        }
    }
})