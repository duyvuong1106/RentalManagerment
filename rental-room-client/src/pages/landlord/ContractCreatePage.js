import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import rentalRequestService from "../../services/rentalRequestService";
import contractService from "../../services/contractService";
import { getErrorMessage } from "../../services/api";

function ContractCreatePage() {
  const navigate = useNavigate();
  const [requests, setRequests] = useState([]);
  const [form, setForm] = useState({
    rentalRequestId: "",
    startDate: "",
    endDate: "",
    monthlyRent: "",
    deposit: "",
    contractUrl: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    rentalRequestService
      .landlordList()
      .then((data) =>
        setRequests((data || []).filter((x) => x.status === "APPROVED")),
      )
      .catch((e) => setError(getErrorMessage(e)));
  }, []);
  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  function pickRequest(e) {
    const id = e.target.value;
    const item = requests.find((x) => String(x.id) === String(id));
    setForm((prev) => ({
      ...prev,
      rentalRequestId: id,
      monthlyRent: item?.roomPrice || item?.price || prev.monthlyRent,
    }));
  }
  async function submit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await contractService.create({
        ...form,
        rentalRequestId: Number(form.rentalRequestId),
        monthlyRent: Number(form.monthlyRent),
        deposit: Number(form.deposit),
      });
      navigate("/landlord/contracts");
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Tạo hợp đồng</h2>
      <p className="text-secondary">
        Tạo hợp đồng từ yêu cầu thuê đã được duyệt.
      </p>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit} className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="mb-3">
            <label className="form-label">Yêu cầu thuê đã duyệt</label>
            <select
              className="form-select"
              value={form.rentalRequestId}
              onChange={pickRequest}
              required
            >
              <option value="">Chọn yêu cầu</option>
              {requests.map((x) => (
                <option key={x.id} value={x.id}>
                  #{x.id} - {x.customerUsername || x.customerId} -{" "}
                  {x.roomNumber || x.roomId}
                </option>
              ))}
            </select>
          </div>
          <div className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Ngày bắt đầu</label>
              <input
                type="date"
                name="startDate"
                value={form.startDate}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Ngày kết thúc</label>
              <input
                type="date"
                name="endDate"
                value={form.endDate}
                onChange={change}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Tiền thuê/tháng</label>
              <input
                type="number"
                name="monthlyRent"
                value={form.monthlyRent}
                onChange={change}
                className="form-control"
                required
                min="1"
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Tiền cọc</label>
              <input
                type="number"
                name="deposit"
                value={form.deposit}
                onChange={change}
                className="form-control"
                required
                min="0"
              />
            </div>
            <div className="col-12">
              <label className="form-label">URL hợp đồng (nếu có)</label>
              <input
                name="contractUrl"
                value={form.contractUrl}
                onChange={change}
                className="form-control"
                placeholder="https://..."
              />
            </div>
          </div>
          <button className="btn btn-primary mt-4" disabled={loading}>
            {loading ? "Đang tạo..." : "Tạo hợp đồng"}
          </button>
        </div>
      </form>
    </div>
  );
}
export default ContractCreatePage;
