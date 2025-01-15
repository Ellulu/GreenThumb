import axios from 'axios';
import { getAuth } from 'firebase/auth';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(async (config) => {
  const auth = getAuth();
  const user = auth.currentUser;

  if (user) {
    const token = await user.getIdToken();
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default {
  get: (url) => apiClient.get(url),
  post: (url, data, isMultipart = false) => {
    if (isMultipart) {
      const formData = new FormData();
      formData.append("file", data);

      return apiClient.post(url, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
    }

    return apiClient.post(url, data);
  },
  postWithMultipart: (url, data) => {
    return apiClient.post(url, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
  put: (url, data) => apiClient.put(url, data),
  delete: (url) => apiClient.delete(url),
};
