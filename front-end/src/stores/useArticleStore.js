import { defineStore } from 'pinia';
import APIService from '@/services/ApiService';

export const useArticleStore = defineStore('article', {
  state: () => ({
    articles: [],
    article: null,
    error: null,
  }),
  actions: {
    async fetchArticles() {
      try {
        const response = await APIService.get('/articles');
        this.articles = response.data; 
      } catch (error) {
        this.error = 'Failed to load articles';
      }
    },
    async fetchArticle(id) {
      try {
        const response = await APIService.get(`/articles/${id}`);
        this.article = response.data; 
      } catch (error) {
        this.error = `Failed to load article with id: ${id}`;
      }
    },
    async fetchComments(articleId) {
      try {
        const response = await APIService.get(`/articles/${articleId}/comments`);
        return response.data;
      } catch (error) {
        this.error = `Failed to load comments for article with id: ${articleId}`;
      }
    },
    
    async addComment(articleId, comment) {
      try {
        const response = await APIService.post(`/articles/${articleId}/comments`, {
          userId: comment.userId, // Aligné avec le backend
          content: comment.content, // Correspond à `content`
        });
        return response.data; // Retourne le commentaire créé
      } catch (error) {
        this.error = `Failed to add comment to article with id: ${articleId}`;
      }
    },
    
    async deleteComment(commentId) {
      try {
        await APIService.delete(`/articles/${commentId}/comments`);
      } catch (error) {
        this.error = `Failed to delete comment with id: ${commentId}`;
      }
    },    
    async createArticle(articleData) {
      try {
        await APIService.post('/articles', articleData);
      } catch (error) {
        this.error = 'Failed to create article';
      }
    },
    async deleteArticle(id) {
      try {
        await APIService.delete(`/articles/${id}`);
      } catch (error) {
        this.error = `Failed to delete article with id: ${id}`;
      }
    },
    async likeArticle(id) {
      try {
        const requestData = true;
        console.log("Données envoyées :", requestData);
    
        const response = await APIService.post(`/articles/${id}/like`, requestData);
        console.log("Réponse reçue :", response.data);
      } catch (error) {
        console.error("Erreur lors de l'appel API:", error);
        this.error = error.response?.data?.message || `Failed to like article with id: ${id}`;
      }
    },
    async dislikeArticle(id) {
      try {
        const requestData = false;
        const response = await APIService.post(`/articles/${id}/like`, requestData);
      } catch (error) {
        this.error = error.response?.data?.message || `Failed to dislike article with id: ${id}`;
      }
    }
  }
});
