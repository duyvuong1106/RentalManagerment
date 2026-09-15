import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import paymentService from "../../services/paymentService";
import invoiceService from "../../services/invoiceService";
import { getErrorMessage } from "../../services/api";
import { formatCurrency } from "../../utils/format";
function PaymentCreatePage() {
  const nav = useNavigate();
  const [sp] = useSearchParams();
  const invoiceId = sp.get("invoiceId") || "";
  const [invoice, setInvoice] = useState(null);
  const [form, setForm] = useState({
    invoiceId,
    amount: "",
    paymentMethod: "BANK_TRANSFER",
    transactionCode: "",
  });
  const [error, setError] = useState("");
  useEffect(() => {
    if (invoiceId)
      invoiceService
        .customerDetail(invoiceId)
        .then((x) => {
          setInvoice(x);
          setForm((f) => ({
            ...f,
            amount: x.remainingAmount ?? x.totalAmount ?? "",
          }));
        })
        .catch((e) => setError(getErrorMessage(e)));
  }, [invoiceId]);
  function c(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    try {
      await paymentService.create({
        ...form,
        invoiceId: Number(form.invoiceId),
        amount: Number(form.amount),
      });
      nav("/customer/payments");
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Thanh toán hóa đơn</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          {invoice && (
            <div className="alert alert-light border">
              Còn phải thanh toán:{" "}
              <strong>
                {formatCurrency(invoice.remainingAmount ?? invoice.totalAmount)}
              </strong>
            </div>
          )}
          <form onSubmit={submit}>
            <div className="mb-3">
              <label className="form-label">Invoice ID</label>
              <input
                name="invoiceId"
                value={form.invoiceId}
                onChange={c}
                className="form-control"
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Số tiền</label>
              <input
                type="number"
                name="amount"
                value={form.amount}
                onChange={c}
                className="form-control"
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Phương thức</label>
              <select
                name="paymentMethod"
                value={form.paymentMethod}
                onChange={c}
                className="form-select"
              >
                <option value="CASH">CASH</option>
                <option value="BANK_TRANSFER">BANK_TRANSFER</option>
                <option value="ONLINE">ONLINE</option>
              </select>
            </div>
            <div className="mb-3">
              <label className="form-label">Mã giao dịch</label>
              <input
                name="transactionCode"
                value={form.transactionCode}
                onChange={c}
                className="form-control"
                placeholder="Bắt buộc nếu ONLINE"
              />
            </div>
            <button className="btn btn-primary">Gửi thanh toán</button>
          </form>
        </div>
      </div>
    </div>
  );
}
export default PaymentCreatePage;
