import { Link } from "react-router-dom";
import { formatCurrency, statusClass, statusLabel } from "../../utils/format";

function RoomCard({ room }) {
    return <div className="card border-0 shadow-sm h-100 room-card"><div className="position-relative"><img src={room.thumbnailUrl || "https://placehold.co/900x550?text=Rental+Room"} className="card-img-top room-card-image" alt={room.title || "Phòng trọ"}/><span className={`badge text-bg-${statusClass(room.status)} position-absolute top-0 end-0 m-3`}>{statusLabel(room.status)}</span></div><div className="card-body d-flex flex-column"><h5 className="card-title fw-bold">{room.title || `Phòng ${room.roomNumber||""}`}</h5><p className="text-secondary mb-2"><i className="bi bi-geo-alt me-1"></i>{room.address || room.areaName || "Chưa cập nhật địa chỉ"}</p><div className="d-flex flex-wrap gap-3 text-secondary small mb-3">{room.areaSize&&<span><i className="bi bi-rulers me-1"></i>{room.areaSize} m²</span>}{room.roomTypeName&&<span><i className="bi bi-house me-1"></i>{room.roomTypeName}</span>}</div><p className="text-primary fw-bold fs-5 mb-3">{formatCurrency(room.price)} / tháng</p><Link to={`/rooms/${room.id}`} className="btn btn-outline-primary mt-auto">Xem chi tiết<i className="bi bi-arrow-right ms-2"></i></Link></div></div>;
}
export default RoomCard;
