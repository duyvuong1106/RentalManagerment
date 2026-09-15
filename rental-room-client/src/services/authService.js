import api from "./api";

const authService = {
  async register(data) {
    const response = await api.post("/api/auth/register", data);
    return response.data;
  },
  async login(data) {
    const response = await api.post("/api/auth/login", data);
    return response.data;
  },
  async logout() {
    const response = await api.post("/api/auth/logout");
    return response.data;
  },
  async getCurrentUser() {
    const response = await api.get("/api/auth/me");
    return response.data;
  },
  async changePassword(data) {
    const response = await api.put("/api/auth/change-password", data);
    return response.data;
  },
};

export default authService;
