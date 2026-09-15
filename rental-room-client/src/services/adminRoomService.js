import api from "./api";
const adminRoomService = {
  async pending() {
    const r = await api.get("/api/admin/rooms/pending");
    return r.data;
  },
  async approve(id) {
    const r = await api.put(`/api/admin/rooms/${id}/approve`);
    return r.data;
  },
  async reject(id) {
    const r = await api.put(`/api/admin/rooms/${id}/reject`);
    return r.data;
  },
};
export default adminRoomService;
