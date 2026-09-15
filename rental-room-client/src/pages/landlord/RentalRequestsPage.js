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
      setItems((await rentalRequestService.landlordList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function act(id, type) {
    try {
      if (type === "approve") await rentalRequestService.approve(id);
      else await rentalRequestService.reject(id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Yêu cầu thuê</h2>
      <p className="text-secondary">
        Tiếp nhận và xử lý yêu cầu thuê của khách hàng.
      </p>
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
                <th>Khách</th>
                <th>Phòng</th>
                <th>Ngày</th>
                <th>Trạng thái</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => (
                <tr key={x.id}>
                  <td>
                    {x.customerUsername || x.user?.username || x.customerId}
                  </td>
                  <td>{x.roomNumber || x.room?.roomNumber || x.roomId}</td>
                  <td>{formatDate(x.requestDate)}</td>
                  <td>
                    <span className={`badge text-bg-${statusClass(x.status)}`}>
                      {statusLabel(x.status)}
                    </span>
                  </td>
                  <td className="text-end">
                    {x.status === "PENDING" && (
                      <>
                        <button
                          className="btn btn-sm btn-success me-2"
                          onClick={() => act(x.id, "approve")}
                        >
                          Duyệt
                        </button>
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => act(x.id, "reject")}
                        >
                          Từ chối
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              ))}
              {items.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-5">
                    Không có yêu cầu.
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
export default RentalRequestsPage;
