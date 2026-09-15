import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import reviewService from "../../services/reviewService";
import { getErrorMessage } from "../../services/api";
function ReviewCreatePage() {
  const nav = useNavigate();
  const [sp] = useSearchParams();
  const [form, setForm] = useState({
    roomId: sp.get("roomId") || "",
    rating: 5,
    comment: "",
  });
  const [error, setError] = useState("");
  function c(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    try {
      await reviewService.create({
        ...form,
        roomId: Number(form.roomId),
        rating: Number(form.rating),
      });
      nav("/customer/reviews");
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Đánh giá phòng</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit} className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="mb-3">
            <label className="form-label">Room ID</label>
            <input
              name="roomId"
              value={form.roomId}
              onChange={c}
              className="form-control"
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Điểm đánh giá</label>
            <select
              name="rating"
              value={form.rating}
              onChange={c}
              className="form-select"
            >
              {[5, 4, 3, 2, 1].map((n) => (
                <option value={n} key={n}>
                  {n} sao
                </option>
              ))}
            </select>
          </div>
          <div className="mb-3">
            <label className="form-label">Nhận xét</label>
            <textarea
              name="comment"
              rows="5"
              value={form.comment}
              onChange={c}
              className="form-control"
            />
          </div>
          <button className="btn btn-primary">Gửi đánh giá</button>
        </div>
      </form>
    </div>
  );
}
export default ReviewCreatePage;
