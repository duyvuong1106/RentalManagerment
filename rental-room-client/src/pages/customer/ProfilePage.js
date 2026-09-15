import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import profileService from "../../services/profileService";
import { getErrorMessage } from "../../services/api";
function ProfilePage() {
  const { user, loadCurrentUser } = useAuth();
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    address: "",
  });
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");
  useEffect(() => {
    setForm({
      firstName: user?.firstName || "",
      lastName: user?.lastName || "",
      email: user?.email || "",
      phone: user?.phone || "",
      address: user?.address || "",
    });
  }, [user]);
  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    setMsg("");
    setError("");
    try {
      await profileService.update(form);
      await loadCurrentUser();
      setMsg("Cập nhật hồ sơ thành công.");
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  async function avatar(e) {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      await profileService.uploadAvatar(file);
      await loadCurrentUser();
      setMsg("Cập nhật ảnh đại diện thành công.");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Hồ sơ</h2>
      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="row g-4">
            <div className="col-md-3 text-center">
              <img
                src={
                  user?.avatarUrl || "https://placehold.co/160x160?text=User"
                }
                className="rounded-circle img-fluid profile-avatar"
                alt="Avatar"
              />
              <input
                type="file"
                accept="image/*"
                className="form-control mt-3"
                onChange={avatar}
              />
            </div>
            <div className="col-md-9">
              {msg && <div className="alert alert-success">{msg}</div>}
              {error && <div className="alert alert-danger">{error}</div>}
              <form onSubmit={submit}>
                <div className="row g-3">
                  <div className="col-md-6">
                    <label className="form-label">Họ</label>
                    <input
                      name="firstName"
                      className="form-control"
                      value={form.firstName}
                      onChange={change}
                    />
                  </div>
                  <div className="col-md-6">
                    <label className="form-label">Tên</label>
                    <input
                      name="lastName"
                      className="form-control"
                      value={form.lastName}
                      onChange={change}
                    />
                  </div>
                  <div className="col-md-6">
                    <label className="form-label">Email</label>
                    <input
                      type="email"
                      name="email"
                      className="form-control"
                      value={form.email}
                      onChange={change}
                    />
                  </div>
                  <div className="col-md-6">
                    <label className="form-label">Điện thoại</label>
                    <input
                      name="phone"
                      className="form-control"
                      value={form.phone}
                      onChange={change}
                    />
                  </div>
                  <div className="col-12">
                    <label className="form-label">Địa chỉ</label>
                    <input
                      name="address"
                      className="form-control"
                      value={form.address}
                      onChange={change}
                    />
                  </div>
                </div>
                <button className="btn btn-primary mt-4">Lưu thay đổi</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default ProfilePage;
