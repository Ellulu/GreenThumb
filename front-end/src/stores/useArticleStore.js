import { defineStore } from 'pinia';
import APIService from '@/services/APIService';

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
    async likeOrDislikeArticle(id,userid,isLike){
      try {
        await APIService.put(`/articles/${id}`,{id,userid,isLike});
      } catch (error) {
        this.error = `Failed to like this article: ${id}`;
      }
    }
  }
});
