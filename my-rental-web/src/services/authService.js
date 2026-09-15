import axiosClient from '../config/axiosClient';

export const authService = {
  login: async (credentials) => {
    // credentials: { email, password }
    const response = await axiosClient.post('/auth/login', credentials);
    return response.data; // Trả về { token, role, user... }
  },
  register: async (userData) => {
    const response = await axiosClient.post('/auth/register', userData);
    return response.data;
  },
};