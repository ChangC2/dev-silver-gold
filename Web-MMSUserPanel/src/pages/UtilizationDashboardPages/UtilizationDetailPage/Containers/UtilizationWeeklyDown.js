import { Col, Row } from "antd";
import ReactApexChart from 'react-apexcharts';

function UtilizationWeeklyDown(props) {
  const { downtimes, screenSize } = props;

  const series = downtimes.map((x) => parseFloat(x.duration))
  const options = {
    chart: {
      type: 'donut',

    },
    labels: downtimes.map((x) => x.status),
    colors: downtimes.map((x) => x.color),
    legend: {
      position: 'top',
      labels: {
        colors: '#ffffff',
        useSeriesColors: false
      },
    },
  };

  return (
    <Row className="utilization-detail-chart-container">
      <Col span={24} className="utilization-detail-chart-title">
        Weekly Downtime Categories
      </Col>
      <Col span={24} className="utilization-detail-chart-subtitle">
        Trailing 7 Days Total
      </Col>
      <Col span={24} className="utilization-detail-chart-right">
        <ReactApexChart
          className="utilization-detail-chart-weekly-pie"
          options={options}
          series={series}
          type="donut"
          height={440}
        ></ReactApexChart>
      </Col>
    </Row>
  );
}

export default UtilizationWeeklyDown;
