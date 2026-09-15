import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import roomService from "../../services/roomService";
import { getErrorMessage } from "../../services/api";
import { statusClass, statusLabel, formatCurrency } from "../../utils/format";
function RoomsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  async function load() {
    try {
      setLoading(true);
      setItems((await roomService.getLandlordRooms()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function del(id) {
    if (!window.confirm("Xóa phòng này?")) return;
    try {
      await roomService.deleteRoom(id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Quản lý phòng</h2>
          <p className="text-secondary mb-0">
            Quản lý danh sách phòng của bạn.
          </p>
        </div>
        <Link to="/landlord/rooms/new" className="btn btn-primary">
          <i className="bi bi-plus-lg me-2" />
          Thêm phòng
        </Link>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : (
        <div className="table-responsive card border-0 shadow-sm">
          <table className="table align-middle mb-0">
            <thead>
              <tr>
                <th>Mã</th>
                <th>Tiêu đề</th>
                <th>Giá</th>
                <th>Trạng thái</th>
                <th>Duyệt</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => (
                <tr key={x.id}>
                  <td>{x.roomNumber}</td>
                  <td>{x.title || `Phòng ${x.roomNumber}`}</td>
                  <td>{formatCurrency(x.price)}</td>
                  <td>
                    <span className={`badge text-bg-${statusClass(x.status)}`}>
                      {statusLabel(x.status)}
                    </span>
                  </td>
                  <td>
                    <span
                      className={`badge text-bg-${statusClass(x.approvalStatus)}`}
                    >
                      {statusLabel(x.approvalStatus)}
                    </span>
                  </td>
                  <td className="text-end">
                    <Link
                      to={`/landlord/rooms/${x.id}/edit`}
                      className="btn btn-sm btn-outline-primary me-2"
                    >
                      Sửa
                    </Link>
                    <a
                      href={`/landlord/rooms/${x.id}/images`}
                      className="btn btn-sm btn-outline-secondary me-2"
                    >
                      Ảnh
                    </a>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => del(x.id)}
                    >
                      Xóa
                    </button>
                  </td>
                </tr>
              ))}
              {items.length === 0 && (
                <tr>
                  <td colSpan="6" className="text-center py-5">
                    Chưa có phòng.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
export default RoomsPage;
