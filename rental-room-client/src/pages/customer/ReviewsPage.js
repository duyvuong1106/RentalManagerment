import { useEffect, useState } from "react";
import reviewService from "../../services/reviewService";
import { getErrorMessage } from "../../services/api";
import { formatDate } from "../../utils/format";
function ReviewsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState("");
  useEffect(() => {
    reviewService
      .listForCustomer()
      .then((x) => setItems(x || []))
      .catch((e) => setError(getErrorMessage(e)));
  }, []);
  return (
    <div>
      <h2 className="fw-bold">Đánh giá của tôi</h2>
      <p className="text-secondary">Các đánh giá bạn đã gửi.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      {items.length === 0 ? (
        <div className="alert alert-light border">Chưa có đánh giá.</div>
      ) : (
        <div className="row g-3">
          {items.map((x) => (
            <div className="col-md-6" key={x.id}>
              <div className="card border-0 shadow-sm">
                <div className="card-body">
                  <div className="text-warning mb-2">
                    {"★".repeat(x.rating || 0)}
                  </div>
                  <h6 className="fw-bold">
                    {x.roomNumber || `Phòng #${x.roomId}`}
                  </h6>
                  <p className="text-secondary">{x.comment}</p>
                  <small className="text-muted">
                    {formatDate(x.createdDate)}
                  </small>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
export default ReviewsPage;
