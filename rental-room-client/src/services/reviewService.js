import api from "./api";
const reviewService = {
  async listForCustomer() {
    const r = await api.get("/api/customer/reviews");
    return r.data;
  },
  async detail(id) {
    const r = await api.get(`/api/customer/reviews/${id}`);
    return r.data;
  },
  async create(data) {
    const r = await api.post("/api/customer/reviews", data);
    return r.data;
  },
};
export default reviewService;
