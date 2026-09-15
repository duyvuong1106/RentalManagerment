import { useEffect, useState } from "react";
import contractService from "../../services/contractService";
import { getErrorMessage } from "../../services/api";
import {
  formatCurrency,
  formatDate,
  statusClass,
  statusLabel,
} from "../../utils/format";
function ContractsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState("");
  async function load() {
    try {
      setItems((await contractService.landlordList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function act(id, t) {
    try {
      await contractService[t](id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Hợp đồng</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="table-responsive card border-0 shadow-sm">
        <table className="table align-middle mb-0">
          <thead>
            <tr>
              <th>#</th>
              <th>Khách</th>
              <th>Phòng</th>
              <th>Thời hạn</th>
              <th>Giá thuê</th>
              <th>Trạng thái</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {items.map((x) => (
              <tr key={x.id}>
                <td>{x.id}</td>
                <td>{x.username || x.customerUsername || x.userId}</td>
                <td>{x.roomNumber || x.roomId}</td>
                <td>
                  {formatDate(x.startDate)} - {formatDate(x.endDate)}
                </td>
                <td>{formatCurrency(x.monthlyRent)}</td>
                <td>
                  <span className={`badge text-bg-${statusClass(x.status)}`}>
                    {statusLabel(x.status)}
                  </span>
                </td>
                <td>
                  {x.status === "DRAFT" && (
                    <button
                      className="btn btn-sm btn-success"
                      onClick={() => act(x.id, "activate")}
                    >
                      Kích hoạt
                    </button>
                  )}
                  {x.status === "ACTIVE" && (
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => act(x.id, "terminate")}
                    >
                      Chấm dứt
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {items.length === 0 && (
              <tr>
                <td colSpan="7" className="text-center py-5">
                  Chưa có hợp đồng.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
export default ContractsPage;
