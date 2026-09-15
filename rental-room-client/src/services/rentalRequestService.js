import api from "./api";

const rentalRequestService = {
  async create(data) {
    const r = await api.post("/api/customer/rental-requests", data);
    return r.data;
  },
  async customerList() {
    const r = await api.get("/api/customer/rental-requests");
    return r.data;
  },
  async customerDetail(id) {
    const r = await api.get(`/api/customer/rental-requests/${id}`);
    return r.data;
  },
  async cancel(id) {
    const r = await api.put(`/api/customer/rental-requests/${id}/cancel`);
    return r.data;
  },
  async landlordList() {
    const r = await api.get("/api/landlord/rental-requests");
    return r.data;
  },
  async landlordDetail(id) {
    const r = await api.get(`/api/landlord/rental-requests/${id}`);
    return r.data;
  },
  async approve(id) {
    const r = await api.put(`/api/landlord/rental-requests/${id}/approve`);
    return r.data;
  },
  async reject(id) {
    const r = await api.put(`/api/landlord/rental-requests/${id}/reject`);
    return r.data;
  },
};
export default rentalRequestService;
