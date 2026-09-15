import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import roomService from "../../services/roomService";
import rentalRequestService from "../../services/rentalRequestService";
import viewingService from "../../services/viewingService";
import { useAuth } from "../../context/AuthContext";
import { getErrorMessage } from "../../services/api";
import { formatCurrency, formatDate } from "../../utils/format";
function RoomDetailPage() {
  const { id } = useParams();
  const nav = useNavigate();
  const { user, isAuthenticated } = useAuth();
  const [room, setRoom] = useState(null),
    [images, setImages] = useState([]),
    [amenities, setAmenities] = useState([]),
    [reviews, setReviews] = useState([]),
    [selected, setSelected] = useState(""),
    [loading, setLoading] = useState(true),
    [error, setError] = useState(""),
    [message, setMessage] = useState("");
  const [req, setReq] = useState("");
  const [view, setView] = useState({ date: "", time: "", message: "" });
  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const r = await roomService.getRoomById(id);
        setRoom(r);
        const [im, am, re] = await Promise.all([
          roomService.getRoomImages(id),
          roomService.getRoomAmenities(id),
          roomService.getRoomReviews(id),
        ]);
        setImages(im || []);
        setAmenities(am || []);
        setReviews(re || []);
        const t = (im || []).find((x) => x.isThumbnail);
        setSelected(t?.imageUrl || im?.[0]?.imageUrl || "");
      } catch (e) {
        setError(getErrorMessage(e));
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);
  async function sendRequest() {
    if (!isAuthenticated || user?.role !== "CUSTOMER") {
      nav(`/login?redirect=/rooms/${id}`);
      return;
    }
    try {
      setMessage("");
      await rentalRequestService.create({ roomId: Number(id), message: req });
      setReq("");
      setMessage("Gửi yêu cầu thuê thành công.");
    } catch (e) {
      setMessage(getErrorMessage(e));
    }
  }
  async function bookView(e) {
    e.preventDefault();
    if (!isAuthenticated || user?.role !== "CUSTOMER") {
      nav(`/login?redirect=/rooms/${id}`);
      return;
    }
    try {
      await viewingService.create({
        roomId: Number(id),
        appointmentDate: view.date,
        appointmentTime: view.time,
        message: view.message,
      });
      setMessage("Đã gửi yêu cầu đặt lịch xem phòng.");
      setView({ date: "", time: "", message: "" });
    } catch (e) {
      setMessage(getErrorMessage(e));
    }
  }
  if (loading)
    return (
      <div className="container py-5 text-center">
        <div className="spinner-border text-primary" />
      </div>
    );
  if (error || !room)
    return (
      <div className="container py-5">
        <div className="alert alert-danger">
          {error || "Không tìm thấy phòng."}
        </div>
        <Link to="/rooms" className="btn btn-primary">
          Quay lại
        </Link>
      </div>
    );
  return (
    <div className="bg-light min-vh-100">
      <div className="container py-4">
        <Link to="/rooms" className="text-decoration-none">
          <i className="bi bi-arrow-left me-1" />
          Tất cả phòng
        </Link>
        <div className="row g-4 mt-1">
          <div className="col-lg-7">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-3">
                {selected ? (
                  <img
                    src={selected}
                    className="room-detail-image"
                    alt={room.title || "Phòng"}
                  />
                ) : (
                  <div className="room-detail-image bg-secondary-subtle d-flex align-items-center justify-content-center">
                    <i className="bi bi-house fs-1 text-secondary" />
                  </div>
                )}
                {images.length > 0 && (
                  <div className="d-flex flex-wrap gap-2 mt-3">
                    {images.map((x) => (
                      <img
                        key={x.id}
                        src={x.imageUrl}
                        className="room-thumbnail"
                        onClick={() => setSelected(x.imageUrl)}
                        alt=""
                      />
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
          <div className="col-lg-5">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-4">
                <div className="d-flex justify-content-between">
                  <div>
                    <h1 className="h3 fw-bold">
                      {room.title || `Phòng ${room.roomNumber}`}
                    </h1>
                    <p className="text-secondary">
                      <i className="bi bi-geo-alt me-1" />
                      {room.address || room.areaName}
                    </p>
                  </div>
                  <span className="badge text-bg-success">{room.status}</span>
                </div>
                <div className="text-primary fw-bold fs-3 mb-4">
                  {formatCurrency(room.price)} / tháng
                </div>
                <div className="row g-3 mb-4">
                  <div className="col-6">
                    <div className="border rounded-3 p-3">
                      <small className="text-secondary d-block">
                        Diện tích
                      </small>
                      <strong>{room.areaSize || "—"} m²</strong>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="border rounded-3 p-3">
                      <small className="text-secondary d-block">
                        Loại phòng
                      </small>
                      <strong>{room.roomTypeName || "—"}</strong>
                    </div>
                  </div>
                </div>
                {message && (
                  <div
                    className={`alert ${message.toLowerCase().includes("thành công") || message.toLowerCase().includes("gửi") ? "alert-success" : "alert-danger"}`}
                  >
                    {message}
                  </div>
                )}
                <div className="mb-3">
                  <label className="form-label fw-semibold">
                    Lời nhắn thuê phòng
                  </label>
                  <textarea
                    rows="3"
                    className="form-control"
                    value={req}
                    onChange={(e) => setReq(e.target.value)}
                    placeholder="Tôi muốn thuê phòng..."
                  />
                </div>
                <button
                  className="btn btn-primary w-100 mb-4"
                  disabled={room.status !== "AVAILABLE"}
                  onClick={sendRequest}
                >
                  Gửi yêu cầu thuê
                </button>
                <hr />
                <h6 className="fw-bold">Đặt lịch xem phòng</h6>
                <form onSubmit={bookView}>
                  <div className="row g-2">
                    <div className="col-6">
                      <input
                        type="date"
                        className="form-control"
                        required
                        value={view.date}
                        onChange={(e) =>
                          setView({ ...view, date: e.target.value })
                        }
                      />
                    </div>
                    <div className="col-6">
                      <input
                        type="time"
                        className="form-control"
                        required
                        value={view.time}
                        onChange={(e) =>
                          setView({ ...view, time: e.target.value })
                        }
                      />
                    </div>
                  </div>
                  <textarea
                    className="form-control mt-2"
                    rows="2"
                    placeholder="Ghi chú"
                    value={view.message}
                    onChange={(e) =>
                      setView({ ...view, message: e.target.value })
                    }
                  />
                  <button className="btn btn-outline-primary w-100 mt-2">
                    Đặt lịch xem
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
        <div className="row g-4 mt-1">
          <div className="col-lg-8">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-4">
                <h4 className="fw-bold">Mô tả</h4>
                <p className="text-secondary mb-0">
                  {room.description || "Chưa có mô tả."}
                </p>
              </div>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-4">
                <h4 className="fw-bold">Tiện ích</h4>
                {amenities.length === 0 ? (
                  <p className="text-secondary">Chưa có tiện ích.</p>
                ) : (
                  amenities.map((x, i) => {
                    const a = x.amenity || x;
                    return (
                      <div className="room-amenity-item mb-2" key={a.id || i}>
                        <i className="bi bi-check-circle text-success me-2" />
                        {a.name || "Tiện ích"}
                      </div>
                    );
                  })
                )}
              </div>
            </div>
          </div>
        </div>
        <div className="card border-0 shadow-sm mt-4">
          <div className="card-body p-4">
            <div className="d-flex justify-content-between">
              <h4 className="fw-bold">Đánh giá</h4>
              <span className="text-secondary">{reviews.length}</span>
            </div>
            {reviews.length === 0 ? (
              <p className="text-secondary">Chưa có đánh giá.</p>
            ) : (
              reviews.map((x) => (
                <div className="review-card p-3 mt-3" key={x.id}>
                  <div className="d-flex justify-content-between">
                    <strong>{x.customerUsername || "Khách hàng"}</strong>
                    <span className="text-warning">
                      {"★".repeat(x.rating || 0)}
                    </span>
                  </div>
                  <p className="text-secondary mt-2 mb-1">{x.comment}</p>
                  <small className="text-muted">
                    {formatDate(x.createdDate)}
                  </small>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
export default RoomDetailPage;
