import { useEffect, useState } from "react";
import rentalRequestService from "../../services/rentalRequestService";
import { getErrorMessage } from "../../services/api";
import { formatDate, statusClass, statusLabel } from "../../utils/format";

function RentalRequestsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  async function load() {
    try {
      setLoading(true);
      setItems((await rentalRequestService.customerList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function cancel(id) {
    if (!window.confirm("Bạn có chắc muốn hủy yêu cầu này?")) return;
    try {
      await rentalRequestService.cancel(id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Yêu cầu thuê</h2>
          <p className="text-secondary mb-0">
            Theo dõi yêu cầu thuê phòng của bạn.
          </p>
        </div>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : items.length === 0 ? (
        <div className="card border-0 shadow-sm">
          <div className="card-body text-center py-5">
            <i className="bi bi-inbox fs-1 text-secondary" />
            <p className="text-secondary mt-3">Chưa có yêu cầu thuê.</p>
          </div>
        </div>
      ) : (
        <div className="table-responsive card border-0 shadow-sm">
          <table className="table mb-0 align-middle">
            <thead>
              <tr>
                <th>Phòng</th>
                <th>Ngày gửi</th>
                <th>Trạng thái</th>
                <th className="text-end">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => (
                <tr key={x.id}>
                  <td>
                    {x.roomNumber || x.room?.roomNumber || `Phòng #${x.roomId}`}
                  </td>
                  <td>{formatDate(x.requestDate || x.createdDate)}</td>
                  <td>
                    <span className={`badge text-bg-${statusClass(x.status)}`}>
                      {statusLabel(x.status)}
                    </span>
                  </td>
                  <td className="text-end">
                    {x.status === "PENDING" && (
                      <button
                        className="btn btn-sm btn-outline-danger"
                        onClick={() => cancel(x.id)}
                      >
                        Hủy
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
export default RentalRequestsPage;
