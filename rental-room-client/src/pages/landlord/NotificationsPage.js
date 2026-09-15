import { useState } from "react";
import notificationService from "../../services/notificationService";
import { getErrorMessage } from "../../services/api";
function NotificationsPage() {
  const [f, setF] = useState({
      customerId: "",
      title: "",
      content: "",
      type: "GENERAL",
    }),
    [msg, setMsg] = useState(""),
    [error, setError] = useState("");
  function c(e) {
    setF({ ...f, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    setMsg("");
    setError("");
    try {
      await notificationService.sendFromLandlord({
        ...f,
        customerId: Number(f.customerId),
      });
      setMsg("Đã gửi thông báo.");
      setF({ ...f, customerId: "", title: "", content: "" });
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Gửi thông báo</h2>
      <p className="text-secondary">
        Gửi thông báo cho khách thuê đang có quan hệ thuê phù hợp.
      </p>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit} className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="mb-3">
            <label className="form-label">Customer ID</label>
            <input
              type="number"
              name="customerId"
              value={f.customerId}
              onChange={c}
              className="form-control"
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Tiêu đề</label>
            <input
              name="title"
              value={f.title}
              onChange={c}
              className="form-control"
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Nội dung</label>
            <textarea
              rows="5"
              name="content"
              value={f.content}
              onChange={c}
              className="form-control"
              required
            />
          </div>
          <button className="btn btn-primary">Gửi thông báo</button>
        </div>
      </form>
    </div>
  );
}
export default NotificationsPage;
