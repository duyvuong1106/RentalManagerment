import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import roomService from "../../services/roomService";
import catalogService from "../../services/catalogService";
import { getErrorMessage } from "../../services/api";
function RoomFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editing = Boolean(id);
  const [areas, setAreas] = useState([]),
    [types, setTypes] = useState([]),
    [form, setForm] = useState({
      roomNumber: "",
      title: "",
      description: "",
      address: "",
      areaId: "",
      roomTypeId: "",
      area: "",
      price: "",
    }),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(editing);
  useEffect(() => {
    (async () => {
      try {
        const [a, t] = await Promise.all([
          catalogService.getAreas(),
          catalogService.getRoomTypes(),
        ]);
        setAreas(a || []);
        setTypes(t || []);
        if (editing) {
          const r = await roomService.getLandlordRoom(id);
          setForm({
            roomNumber: r.roomNumber || "",
            title: r.title || "",
            description: r.description || "",
            address: r.address || "",
            areaId: r.areaId || "",
            roomTypeId: r.roomTypeId || "",
            area: r.areaSize || "",
            price: r.price || "",
          });
        }
      } catch (e) {
        setError(getErrorMessage(e));
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);
  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    setError("");
    try {
      const payload = {
        ...form,
        areaId: Number(form.areaId),
        roomTypeId: Number(form.roomTypeId),
        area: Number(form.area),
        price: Number(form.price),
      };
      if (editing) await roomService.updateRoom(id, payload);
      else await roomService.createRoom(payload);
      navigate("/landlord/rooms");
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  if (loading)
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" />
      </div>
    );
  return (
    <div>
      <h2 className="fw-bold mb-4">
        {editing ? "Chỉnh sửa phòng" : "Thêm phòng mới"}
      </h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit} className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="row g-3">
            <div className="col-md-4">
              <label className="form-label">Mã phòng</label>
              <input
                name="roomNumber"
                value={form.roomNumber}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-8">
              <label className="form-label">Tiêu đề</label>
              <input
                name="title"
                value={form.title}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Khu vực</label>
              <select
                name="areaId"
                value={form.areaId}
                onChange={change}
                className="form-select"
                required
              >
                <option value="">Chọn khu vực</option>
                {areas.map((x) => (
                  <option value={x.id} key={x.id}>
                    {x.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="col-md-6">
              <label className="form-label">Loại phòng</label>
              <select
                name="roomTypeId"
                value={form.roomTypeId}
                onChange={change}
                className="form-select"
                required
              >
                <option value="">Chọn loại phòng</option>
                {types.map((x) => (
                  <option value={x.id} key={x.id}>
                    {x.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="col-md-6">
              <label className="form-label">Diện tích (m²)</label>
              <input
                type="number"
                step="0.1"
                min="0.1"
                name="area"
                value={form.area}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Giá thuê/tháng</label>
              <input
                type="number"
                min="1"
                name="price"
                value={form.price}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-12">
              <label className="form-label">Địa chỉ</label>
              <input
                name="address"
                value={form.address}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-12">
              <label className="form-label">Mô tả</label>
              <textarea
                rows="4"
                name="description"
                value={form.description}
                onChange={change}
                className="form-control"
              />
            </div>
          </div>
          <div className="mt-4 d-flex gap-2">
            <button className="btn btn-primary">
              {editing ? "Lưu thay đổi" : "Tạo phòng"}
            </button>
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={() => navigate("/landlord/rooms")}
            >
              Hủy
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}
export default RoomFormPage;
