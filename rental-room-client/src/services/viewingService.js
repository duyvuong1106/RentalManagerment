import api from "./api";

const viewingService = {
  async customerList() {
    const r = await api.get("/api/customer/viewings");
    return r.data;
  },
  async customerDetail(id) {
    const r = await api.get(`/api/customer/viewings/${id}`);
    return r.data;
  },
  async create(data) {
    const r = await api.post("/api/customer/viewings", data);
    return r.data;
  },
  async cancel(id) {
    const r = await api.put(`/api/customer/viewings/${id}/cancel`);
    return r.data;
  },
  async landlordList() {
    const r = await api.get("/api/landlord/viewings");
    return r.data;
  },
  async landlordDetail(id) {
    const r = await api.get(`/api/landlord/viewings/${id}`);
    return r.data;
  },
  async approve(id) {
    const r = await api.put(`/api/landlord/viewings/${id}/approve`);
    return r.data;
  },
  async reject(id) {
    const r = await api.put(`/api/landlord/viewings/${id}/reject`);
    return r.data;
  },
  async complete(id) {
    const r = await api.put(`/api/landlord/viewings/${id}/complete`);
    return r.data;
  },
};
export default viewingService;
