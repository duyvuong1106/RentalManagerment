import api from "./api";
const paymentService = {
  async customerList() {
    const r = await api.get("/api/customer/payments");
    return r.data;
  },
  async customerDetail(id) {
    const r = await api.get(`/api/customer/payments/${id}`);
    return r.data;
  },
  async create(data) {
    const r = await api.post("/api/customer/payments", data);
    return r.data;
  },
  async cancel(id) {
    const r = await api.put(`/api/customer/payments/${id}/cancel`);
    return r.data;
  },
  async landlordList() {
    const r = await api.get("/api/landlord/payments");
    return r.data;
  },
  async landlordDetail(id) {
    const r = await api.get(`/api/landlord/payments/${id}`);
    return r.data;
  },
  async confirm(id) {
    const r = await api.put(`/api/landlord/payments/${id}/confirm`);
    return r.data;
  },
  async reject(id) {
    const r = await api.put(`/api/landlord/payments/${id}/reject`);
    return r.data;
  },
};
export default paymentService;
