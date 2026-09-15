export function formatCurrency(value) {
  return `${Number(value || 0).toLocaleString("vi-VN")} VNĐ`;
}

export function formatDate(value) {
  if (!value) return "—";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleDateString("vi-VN");
}

export function statusLabel(status) {
  const labels = {
    PENDING: "Chờ xử lý",
    APPROVED: "Đã duyệt",
    REJECTED: "Từ chối",
    CANCELLED: "Đã hủy",
    AVAILABLE: "Còn phòng",
    RENTED: "Đang thuê",
    DRAFT: "Bản nháp",
    ACTIVE: "Đang hiệu lực",
    EXPIRED: "Đã hết hạn",
    TERMINATED: "Đã chấm dứt",
    SUCCESS: "Thành công",
    FAILED: "Thất bại",
    COMPLETED: "Hoàn thành",
  };
  return labels[status] || status || "—";
}

export function statusClass(status) {
  if (
    ["AVAILABLE", "APPROVED", "ACTIVE", "SUCCESS", "COMPLETED"].includes(status)
  )
    return "success";
  if (["PENDING", "DRAFT"].includes(status)) return "warning";
  if (["REJECTED", "FAILED", "CANCELLED", "TERMINATED"].includes(status))
    return "danger";
  return "secondary";
}
