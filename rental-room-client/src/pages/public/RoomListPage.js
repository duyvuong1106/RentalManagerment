import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

import RoomCard from "../../components/room/RoomCard";
import roomService from "../../services/roomService";
import catalogService from "../../services/catalogService";
import { getErrorMessage } from "../../services/api";

const EMPTY_FILTERS = {
  areaId: "",
  roomTypeId: "",
  minPrice: "",
  maxPrice: "",
  minArea: "",
  maxArea: "",
  amenityId: "",
};

function RoomListPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const [rooms, setRooms] = useState([]);
  const [areas, setAreas] = useState([]);
  const [types, setTypes] = useState([]);
  const [amenities, setAmenities] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [filters, setFilters] = useState(() => ({
    areaId: searchParams.get("areaId") || "",
    roomTypeId: searchParams.get("roomTypeId") || "",
    minPrice: searchParams.get("minPrice") || "",
    maxPrice: searchParams.get("maxPrice") || "",
    minArea: searchParams.get("minArea") || "",
    maxArea: searchParams.get("maxArea") || "",
    amenityId: searchParams.get("amenityId") || "",
  }));

  useEffect(() => {
    loadCatalogs();
    load(filters);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  async function loadCatalogs() {
    try {
      const [areaData, typeData, amenityData] = await Promise.all([
        catalogService.getAreas(),
        catalogService.getRoomTypes(),
        catalogService.getAmenities(),
      ]);

      setAreas(areaData || []);
      setTypes(typeData || []);
      setAmenities(amenityData || []);
    } catch (err) {
      console.error("Không thể tải dữ liệu bộ lọc:", err);
    }
  }

  function buildQueryFilters(values) {
    return Object.fromEntries(
      Object.entries(values).filter(([, value]) => value !== ""),
    );
  }

  async function load(values) {
    try {
      setLoading(true);
      setError("");

      const queryFilters = buildQueryFilters(values);
      setSearchParams(queryFilters);

      const data = await roomService.getRooms(queryFilters);
      setRooms(data || []);
    } catch (err) {
      console.error("Không thể tải danh sách phòng:", err);
      setError(getErrorMessage(err, "Không thể tải danh sách phòng."));
    } finally {
      setLoading(false);
    }
  }

  function change(event) {
    const { name, value } = event.target;

    setFilters((current) => ({
      ...current,
      [name]: value,
    }));
  }

  function submit(event) {
    event.preventDefault();
    load(filters);
  }

  function reset() {
    setFilters(EMPTY_FILTERS);
    load(EMPTY_FILTERS);
  }

  return (
    <div className="bg-light min-vh-100">
      <section className="bg-white border-bottom">
        <div className="container py-4">
          <h1 className="fw-bold mb-2">Tìm phòng trọ</h1>
          <p className="text-secondary mb-0">
            Lọc theo khu vực, giá, diện tích và tiện ích.
          </p>
        </div>
      </section>

      <section className="py-4">
        <div className="container">
          <div className="row g-4">
            <div className="col-lg-3">
              <div className="card border-0 shadow-sm room-filter-card">
                <div className="card-body p-4">
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="fw-bold mb-0">Bộ lọc</h5>

                    <button
                      type="button"
                      className="btn btn-sm btn-link"
                      onClick={reset}
                    >
                      Xóa lọc
                    </button>
                  </div>

                  <form onSubmit={submit}>
                    <div className="mb-3">
                      <label className="form-label fw-semibold">Khu vực</label>

                      <select
                        className="form-select"
                        name="areaId"
                        value={filters.areaId}
                        onChange={change}
                      >
                        <option value="">Tất cả khu vực</option>

                        {areas.map((area) => (
                          <option value={area.id} key={area.id}>
                            {area.name}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-semibold">
                        Loại phòng
                      </label>

                      <select
                        className="form-select"
                        name="roomTypeId"
                        value={filters.roomTypeId}
                        onChange={change}
                      >
                        <option value="">Tất cả loại phòng</option>

                        {types.map((type) => (
                          <option value={type.id} key={type.id}>
                            {type.name}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-semibold">Giá</label>

                      <div className="row g-2">
                        <div className="col-6">
                          <input
                            className="form-control"
                            type="number"
                            name="minPrice"
                            placeholder="Từ"
                            value={filters.minPrice}
                            onChange={change}
                            min="0"
                          />
                        </div>

                        <div className="col-6">
                          <input
                            className="form-control"
                            type="number"
                            name="maxPrice"
                            placeholder="Đến"
                            value={filters.maxPrice}
                            onChange={change}
                            min="0"
                          />
                        </div>
                      </div>
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-semibold">
                        Diện tích (m²)
                      </label>

                      <div className="row g-2">
                        <div className="col-6">
                          <input
                            className="form-control"
                            type="number"
                            step="0.1"
                            name="minArea"
                            placeholder="Từ"
                            value={filters.minArea}
                            onChange={change}
                            min="0"
                          />
                        </div>

                        <div className="col-6">
                          <input
                            className="form-control"
                            type="number"
                            step="0.1"
                            name="maxArea"
                            placeholder="Đến"
                            value={filters.maxArea}
                            onChange={change}
                            min="0"
                          />
                        </div>
                      </div>
                    </div>

                    <div className="mb-4">
                      <label className="form-label fw-semibold">Tiện ích</label>

                      <select
                        className="form-select"
                        name="amenityId"
                        value={filters.amenityId}
                        onChange={change}
                      >
                        <option value="">Tất cả tiện ích</option>

                        {amenities.map((amenity) => (
                          <option value={amenity.id} key={amenity.id}>
                            {amenity.name}
                          </option>
                        ))}
                      </select>
                    </div>

                    <button type="submit" className="btn btn-primary w-100">
                      <i className="bi bi-search me-2"></i>
                      Tìm phòng
                    </button>
                  </form>
                </div>
              </div>
            </div>

            <div className="col-lg-9">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <div>
                  <h5 className="fw-bold mb-1">Phòng phù hợp</h5>
                  <span className="text-secondary small">
                    {rooms.length} phòng
                  </span>
                </div>
              </div>

              {error && <div className="alert alert-danger">{error}</div>}

              {loading ? (
                <div className="text-center py-5">
                  <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Đang tải...</span>
                  </div>
                </div>
              ) : rooms.length === 0 ? (
                <div className="card border-0 shadow-sm">
                  <div className="card-body text-center py-5">
                    <i className="bi bi-house-slash fs-1 text-secondary"></i>

                    <h5 className="fw-bold mt-3">Không tìm thấy phòng</h5>

                    <p className="text-secondary mb-0">
                      Hãy thử thay đổi điều kiện tìm kiếm.
                    </p>
                  </div>
                </div>
              ) : (
                <div className="row g-4">
                  {rooms.map((room) => (
                    <div className="col-md-6 col-xl-4" key={room.id}>
                      <RoomCard room={room} />
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

export default RoomListPage;
