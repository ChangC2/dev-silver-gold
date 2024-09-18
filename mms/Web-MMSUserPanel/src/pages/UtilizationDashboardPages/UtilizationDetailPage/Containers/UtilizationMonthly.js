import { Col, Row } from "antd";
import ReactApexChart from "react-apexcharts";
import { useSelector } from "react-redux";

function UtilizationMonthly(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { monthlyTotal, monthlyUtilizations, screenSize } = props;

  const series = [
    {
      data: monthlyUtilizations.map((x) => x.utilization),
    },
  ];
  const options = {
    chart: {
      type: "area",
      toolbar: { show: false },
      width: 100,
    },
    dataLabels: {
      enabled: false,
    },
    labels: monthlyUtilizations.map((x) => x.day),
    xaxis: {
      show: false,
      labels: {
        show: false,
      },
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
    },
    yaxis: {
      show: false,
      labels: {
        show: false,
      },
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
    },
    tooltip: {
      fixed: {
        enabled: false,
      },
      x: {
        show: false,
      },
      y: {
        title: {
          formatter: function (seriesName) {
            return "";
          },
        },
      },
      marker: {
        show: false,
      },
    },
  };

  return (
    <Row className="utilization-detail-chart-container">
      <Col span={24} className="utilization-detail-chart-title">
        Monthly Spindle Utilization
      </Col>
      <Col span={24} className="utilization-detail-chart-subtitle">
        Trailing 30 Days Total
      </Col>
      <Col span={8} className="utilization-detail-chart-left">
        <div>
          <div className="utilization-detail-chart-hrs">{monthlyTotal} hrs</div>
          <div className="utilization-detail-chart-percentage">
            {((monthlyTotal / 24 / 30) * 100).toFixed(1)} %
          </div>
        </div>
      </Col>
      <Col span={16} className="utilization-detail-chart-right">
        <ReactApexChart
          options={options}
          series={series}
          type="area"
          height={180}
        />
      </Col>
    </Row>
  );
}

export default UtilizationMonthly;
