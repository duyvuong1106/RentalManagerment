import api from "./api";
const invoiceService = {
  async customerList() {
    const r = await api.get("/api/customer/invoices");
    return r.data;
  },
  async customerDetail(id) {
    const r = await api.get(`/api/customer/invoices/${id}`);
    return r.data;
  },
  async landlordList() {
    const r = await api.get("/api/landlord/invoices");
    return r.data;
  },
  async landlordDetail(id) {
    const r = await api.get(`/api/landlord/invoices/${id}`);
    return r.data;
  },
  async create(data) {
    const r = await api.post("/api/landlord/invoices", data);
    return r.data;
  },
};
export default invoiceService;
