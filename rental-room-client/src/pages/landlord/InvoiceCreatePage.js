import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import contractService from "../../services/contractService";
import catalogService from "../../services/catalogService";
import invoiceService from "../../services/invoiceService";
import { getErrorMessage } from "../../services/api";
import { formatCurrency } from "../../utils/format";

function InvoiceCreatePage() {
  const navigate = useNavigate();
  const [contracts, setContracts] = useState([]);
  const [services, setServices] = useState([]);
  const [form, setForm] = useState({
    contractId: "",
    billingDate: "",
    dueDate: "",
    details: [{ serviceId: "", quantity: 1, unitPrice: 0 }],
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    Promise.all([contractService.landlordList(), catalogService.getServices()])
      .then(([c, s]) => {
        setContracts((c || []).filter((x) => x.status === "ACTIVE"));
        setServices(
          (s || []).filter((x) => !x.status || x.status === "ACTIVE"),
        );
      })
      .catch((e) => setError(getErrorMessage(e)));
  }, []);
  function setField(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  function detailChange(index, field, value) {
    const details = [...form.details];
    details[index] = { ...details[index], [field]: value };
    if (field === "serviceId") {
      const svc = services.find((x) => String(x.id) === String(value));
      if (svc?.price !== undefined) details[index].unitPrice = svc.price;
    }
    setForm({ ...form, details });
  }
  function addDetail() {
    setForm({
      ...form,
      details: [...form.details, { serviceId: "", quantity: 1, unitPrice: 0 }],
    });
  }
  function removeDetail(i) {
    setForm({ ...form, details: form.details.filter((_, idx) => idx !== i) });
  }
  async function submit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await invoiceService.create({
        ...form,
        contractId: Number(form.contractId),
        details: form.details.map((d) => ({
          serviceId: Number(d.serviceId),
          quantity: Number(d.quantity),
          unitPrice: Number(d.unitPrice),
        })),
      });
      navigate("/landlord/invoices");
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  const serviceTotal = form.details.reduce(
    (s, d) => s + Number(d.quantity || 0) * Number(d.unitPrice || 0),
    0,
  );
  return (
    <div>
      <h2 className="fw-bold">Tạo hóa đơn</h2>
      <p className="text-secondary">Tạo hóa đơn cho hợp đồng đang hoạt động.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit} className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="mb-3">
            <label className="form-label">Hợp đồng</label>
            <select
              name="contractId"
              value={form.contractId}
              onChange={setField}
              className="form-select"
              required
            >
              <option value="">Chọn hợp đồng</option>
              {contracts.map((x) => (
                <option key={x.id} value={x.id}>
                  #{x.id} - {x.roomNumber || x.roomId} -{" "}
                  {x.username || x.customerUsername || x.userId}
                </option>
              ))}
            </select>
          </div>
          <div className="row g-3 mb-4">
            <div className="col-md-6">
              <label className="form-label">Ngày tính</label>
              <input
                type="date"
                name="billingDate"
                value={form.billingDate}
                onChange={setField}
                className="form-control"
                required
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Hạn thanh toán</label>
              <input
                type="date"
                name="dueDate"
                value={form.dueDate}
                onChange={setField}
                className="form-control"
                required
              />
            </div>
          </div>
          <h5 className="fw-bold">Chi tiết dịch vụ</h5>
          {form.details.map((d, i) => (
            <div className="row g-2 align-items-end mb-2" key={i}>
              <div className="col-md-5">
                <label className="form-label">Dịch vụ</label>
                <select
                  className="form-select"
                  value={d.serviceId}
                  onChange={(e) => detailChange(i, "serviceId", e.target.value)}
                  required
                >
                  <option value="">Chọn dịch vụ</option>
                  {services.map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-2">
                <label className="form-label">SL</label>
                <input
                  type="number"
                  min="1"
                  className="form-control"
                  value={d.quantity}
                  onChange={(e) => detailChange(i, "quantity", e.target.value)}
                />
              </div>
              <div className="col-md-3">
                <label className="form-label">Đơn giá</label>
                <input
                  type="number"
                  min="0"
                  className="form-control"
                  value={d.unitPrice}
                  onChange={(e) => detailChange(i, "unitPrice", e.target.value)}
                />
              </div>
              <div className="col-md-2">
                <button
                  type="button"
                  className="btn btn-outline-danger w-100"
                  disabled={form.details.length === 1}
                  onClick={() => removeDetail(i)}
                >
                  Xóa
                </button>
              </div>
            </div>
          ))}
          <button
            type="button"
            className="btn btn-outline-secondary mt-2"
            onClick={addDetail}
          >
            + Thêm dịch vụ
          </button>
          <div className="alert alert-light border mt-4 mb-0">
            Tổng dịch vụ: <strong>{formatCurrency(serviceTotal)}</strong>. Tiền
            phòng sẽ do backend tính theo hợp đồng.
          </div>
          <button className="btn btn-primary mt-4" disabled={loading}>
            {loading ? "Đang tạo..." : "Tạo hóa đơn"}
          </button>
        </div>
      </form>
    </div>
  );
}
export default InvoiceCreatePage;
