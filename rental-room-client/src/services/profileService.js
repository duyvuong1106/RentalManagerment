import api from "./api";
const profileService = {
  async update(data) {
    const r = await api.put("/api/profile", data);
    return r.data;
  },
  async uploadAvatar(file) {
    const form = new FormData();
    form.append("file", file);
    const r = await api.post("/api/profile/avatar", form, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return r.data;
  },
};
export default profileService;
