import { useEffect, useState } from "react";
import viewingService from "../../services/viewingService";
import { getErrorMessage } from "../../services/api";
import { formatDate, statusClass, statusLabel } from "../../utils/format";
function ViewingsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState("");
  async function load() {
    try {
      setItems((await viewingService.landlordList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function act(id, t) {
    try {
      await viewingService[t](id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Lịch xem phòng</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="row g-3">
        {items.map((x) => (
          <div className="col-md-6" key={x.id}>
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <div className="d-flex justify-content-between">
                  <h5 className="fw-bold">{x.roomNumber || x.roomId}</h5>
                  <span className={`badge text-bg-${statusClass(x.status)}`}>
                    {statusLabel(x.status)}
                  </span>
                </div>
                <p className="text-secondary mb-1">
                  Khách:{" "}
                  {x.customerUsername || x.customer?.username || x.customerId}
                </p>
                <p className="text-secondary">
                  {formatDate(x.appointmentDate)} {x.appointmentTime || ""}
                </p>
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
                {x.status === "APPROVED" && (
                  <button
                    className="btn btn-sm btn-primary"
                    onClick={() => act(x.id, "complete")}
                  >
                    Hoàn thành
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
      {items.length === 0 && (
        <div className="alert alert-light border mt-3">Chưa có lịch.</div>
      )}
    </div>
  );
}
export default ViewingsPage;
