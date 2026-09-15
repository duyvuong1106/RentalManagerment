import { useEffect, useState } from "react";

import {
  getLandlordStatistics,
  getLandlordRevenue,
} from "../../services/statisticsService";

function StatisticsPage() {
  const currentYear = new Date().getFullYear();

  const [statistics, setStatistics] = useState(null);

  const [revenue, setRevenue] = useState([]);

  const [year, setYear] = useState(currentYear);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  useEffect(() => {
    loadStatistics();

    
  }, [year]);

  async function loadStatistics() {
    try {
      setLoading(true);
      setError("");

      const statisticsData = await getLandlordStatistics();

      const revenueData = await getLandlordRevenue(year);

      setStatistics(statisticsData);

      if (Array.isArray(revenueData)) {
        setRevenue(revenueData);
      } else if (Array.isArray(revenueData?.data)) {
        setRevenue(revenueData.data);
      } else {
        setRevenue([]);
      }
    } catch (error) {
      console.error("Không thể tải dữ liệu thống kê:", error);

      setError(
        error.response?.data?.message || "Không thể tải dữ liệu thống kê.",
      );
    } finally {
      setLoading(false);
    }
  }

  function formatCurrency(value) {
    return Number(value || 0).toLocaleString("vi-VN");
  }

  function getRevenueByMonth(month) {
    const period = `${year}-${String(month).padStart(2, "0")}`;

    const item = revenue.find((item) => item?.period === period);

    return Number(item?.revenue || 0);
  }

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Đang tải...</span>
        </div>

        <p className="text-secondary mt-3">Đang tải dữ liệu thống kê...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-5">
        <div className="alert alert-danger">{error}</div>

        <button
          type="button"
          className="btn btn-primary"
          onClick={loadStatistics}
        >
          Thử lại
        </button>
      </div>
    );
  }

  return (
    <div className="container py-4">
      {/* Header */}

      <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4">
        <div>
          <h1 className="fw-bold mb-1">Thống kê</h1>

          <p className="text-secondary mb-0">
            Tổng quan hoạt động kinh doanh năm {year}.
          </p>
        </div>

        <div className="mt-3 mt-md-0">
          <select
            className="form-select"
            value={year}
            onChange={(event) => setYear(Number(event.target.value))}
          >
            <option value={currentYear}>Năm {currentYear}</option>

            <option value={currentYear - 1}>Năm {currentYear - 1}</option>

            <option value={currentYear - 2}>Năm {currentYear - 2}</option>
          </select>
        </div>
      </div>

      {/* Statistics */}

      {statistics && (
        <div className="row g-3 mb-4">
          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Tổng phòng</div>

                <h3 className="fw-bold mb-0">{statistics.totalRooms || 0}</h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Còn trống</div>

                <h3 className="fw-bold mb-0">
                  {statistics.availableRooms || 0}
                </h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Đang thuê</div>

                <h3 className="fw-bold mb-0">{statistics.rentedRooms || 0}</h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Yêu cầu chờ</div>

                <h3 className="fw-bold mb-0">
                  {statistics.pendingRentalRequests || 0}
                </h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Hợp đồng hiệu lực</div>

                <h3 className="fw-bold mb-0">
                  {statistics.activeContracts || 0}
                </h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Doanh thu</div>

                <h3 className="fw-bold mb-0">
                  {formatCurrency(statistics.totalRevenue)} VNĐ
                </h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Đã thu</div>

                <h3 className="fw-bold mb-0">
                  {formatCurrency(statistics.paidAmount)} VNĐ
                </h3>
              </div>
            </div>
          </div>

          <div className="col-md-6 col-xl-3">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="text-secondary mb-2">Tỷ lệ lấp đầy</div>

                <h3 className="fw-bold mb-0">
                  {statistics.occupancyRate || 0}%
                </h3>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Revenue */}

      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="mb-4">
            <h4 className="fw-bold mb-1">Doanh thu theo tháng</h4>

            <p className="text-secondary mb-0">
              Doanh thu thanh toán thành công trong năm {year}.
            </p>
          </div>

          <div className="row g-3">
            {Array.from({ length: 12 }, (_, index) => {
              const month = index + 1;

              const amount = getRevenueByMonth(month);

              return (
                <div className="col-6 col-md-4 col-lg-3 col-xl-2" key={month}>
                  <div className="border rounded-3 p-3 h-100">
                    <div className="text-secondary small mb-2">
                      Tháng {month}
                    </div>

                    <div className="fw-bold">{formatCurrency(amount)} VNĐ</div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}

export default StatisticsPage;
