import axios from 'axios';

export const api = axios.create({
  baseURL: 'https://gamelib-biblioteca-pessoal-de-jogos.onrender.com/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('@GameLib:token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});