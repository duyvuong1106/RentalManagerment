import api from "./api";
const contractService = {
  async customerList() {
    const r = await api.get("/api/customer/contracts");
    return r.data;
  },
  async customerDetail(id) {
    const r = await api.get(`/api/customer/contracts/${id}`);
    return r.data;
  },
  async landlordList() {
    const r = await api.get("/api/landlord/contracts");
    return r.data;
  },
  async landlordDetail(id) {
    const r = await api.get(`/api/landlord/contracts/${id}`);
    return r.data;
  },
  async create(data) {
    const r = await api.post("/api/landlord/contracts", data);
    return r.data;
  },
  async activate(id) {
    const r = await api.put(`/api/landlord/contracts/${id}/activate`);
    return r.data;
  },
  async terminate(id) {
    const r = await api.put(`/api/landlord/contracts/${id}/terminate`);
    return r.data;
  },
};
export default contractService;
