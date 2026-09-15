import api from "./api";
const notificationService = {
  async list() {
    const r = await api.get("/api/notifications");
    return r.data;
  },
  async unread() {
    const r = await api.get("/api/notifications/unread");
    return r.data;
  },
  async unreadCount() {
    const r = await api.get("/api/notifications/unread/count");
    return r.data;
  },
  async detail(id) {
    const r = await api.get(`/api/notifications/${id}`);
    return r.data;
  },
  async markRead(id) {
    const r = await api.put(`/api/notifications/${id}/read`);
    return r.data;
  },
  async markAllRead() {
    const r = await api.put("/api/notifications/read-all");
    return r.data;
  },
  async sendFromLandlord(data) {
    const r = await api.post("/api/landlord/notifications", data);
    return r.data;
  },
};
export default notificationService;
