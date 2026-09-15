import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import roomService from "../../services/roomService";
import { getErrorMessage } from "../../services/api";
function RoomImagesPage() {
  const { id } = useParams();
  const nav = useNavigate();
  const [items, setItems] = useState([]),
    [file, setFile] = useState(null),
    [thumb, setThumb] = useState(false),
    [error, setError] = useState("");
  async function load() {
    try {
      setItems((await roomService.getRoomImages(id)) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  useEffect(() => {
    load();
  }, [id]);
  async function upload() {
    if (!file) return;
    try {
      await roomService.uploadImage(id, file, thumb);
      setFile(null);
      setThumb(false);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  async function remove(imageId) {
    if (!window.confirm("Xóa ảnh?")) return;
    try {
      await roomService.deleteImage(id, imageId);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  async function setT(imageId) {
    try {
      await roomService.setThumbnail(id, imageId);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <div className="d-flex justify-content-between mb-4">
        <h2 className="fw-bold">Ảnh phòng</h2>
        <button
          className="btn btn-outline-secondary"
          onClick={() => nav(`/landlord/rooms/${id}/edit`)}
        >
          Quay lại
        </button>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-body">
          <div className="row g-3 align-items-end">
            <div className="col-md-7">
              <label className="form-label">Chọn ảnh</label>
              <input
                type="file"
                accept="image/*"
                className="form-control"
                onChange={(e) => setFile(e.target.files?.[0] || null)}
              />
            </div>
            <div className="col-md-3">
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="checkbox"
                  checked={thumb}
                  onChange={(e) => setThumb(e.target.checked)}
                  id="thumb"
                />
                <label className="form-check-label" htmlFor="thumb">
                  Đặt làm ảnh đại diện
                </label>
              </div>
            </div>
            <div className="col-md-2">
              <button
                className="btn btn-primary w-100"
                onClick={upload}
                disabled={!file}
              >
                Upload
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className="row g-3">
        {items.map((x) => (
          <div className="col-6 col-md-4 col-xl-3" key={x.id}>
            <div className="card border-0 shadow-sm h-100">
              <img
                src={x.imageUrl}
                className="card-img-top room-gallery-image"
                alt="Room"
              />
              <div className="card-body">
                <div className="small mb-2">
                  {x.isThumbnail ? (
                    <span className="badge text-bg-primary">Ảnh đại diện</span>
                  ) : (
                    <span className="text-secondary">Ảnh phụ</span>
                  )}
                </div>
                <div className="d-grid gap-2">
                  {!x.isThumbnail && (
                    <button
                      className="btn btn-sm btn-outline-primary"
                      onClick={() => setT(x.id)}
                    >
                      Đặt ảnh đại diện
                    </button>
                  )}
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => remove(x.id)}
                  >
                    Xóa
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
export default RoomImagesPage;
