import { Col, Row } from "antd";
import ReactApexChart from "react-apexcharts";
import { useSelector } from "react-redux";

function UtilizationWeeklyBar(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { screenSize, weeklyTotal, weeklyUtilizations } = props;

  const series = [
    {
      data: weeklyUtilizations.map((x) => x.utilization),
    },
  ];

  const options = {
    chart: {
      type: "bar",
      toolbar: { show: false },
      width: 100,
    },

    plotOptions: {
      bar: {
        columnWidth: "60%",
      },
    },
    dataLabels: {
      enabled: false,
    },

    labels: weeklyUtilizations.map((x) => x.day),
    xaxis: {
      show:true,
      labels: {
        style: {
          colors: "#fff",
          cssClass: "apexcharts-xaxis-label",
        },
        formatter: function (val, timestamp) {
          return val;
        },
      },
    },

    yaxis: {
      show: false,
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

    // categories: weeklyUtilizations.map((x) => x.day),
    // xaxis: {
    //   title: {
    //     text: "xaxisTitle",
    //     style: {
    //       fontSize: 20,
    //       color: "#ffffff",
    //     },
    //   },
    //   labels: {
    //     formatter: (value) => {
    //       return value;
    //     },
    //     style: {
    //       fontSize: 20,
    //     },
    //   },
    //   tickAmount: 5,
    //   tickPlacement: "between",
    // },
  };

  return (
    <Row className="utilization-detail-chart-container">
      <Col span={24} className="utilization-detail-chart-title">
        Weekly Spindle Utilization
      </Col>
      <Col span={24} className="utilization-detail-chart-subtitle">
        Trailing 7 Days Total
      </Col>
      <Col span={8} className="utilization-detail-chart-left">
        <div>
          <div className="utilization-detail-chart-hrs">{weeklyTotal} hrs</div>
          <div className="utilization-detail-chart-percentage">
            {(weeklyTotal / 24 / 7 * 100).toFixed(1)} %
          </div>
        </div>
      </Col>
      <Col span={16} className="utilization-detail-chart-right">
        <ReactApexChart
          options={options}
          series={series}
          type="bar"
          height={180}
        />
      </Col>
    </Row>
  );
}

export default UtilizationWeeklyBar;
