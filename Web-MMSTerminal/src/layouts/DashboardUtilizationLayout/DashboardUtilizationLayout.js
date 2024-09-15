import { Pie } from "@ant-design/plots";
import { Col, Row } from "antd";
import "./DashboardUtilizationLayout.css";

const DashboardUtilizationLayout = (props) => {

  const data = [
    {
      task: "No Operator",
      hours: 14.395,
    },
    {
      task: "Waiting Material",
      hours: 5.092222222222222,
    },
    {
      task: "Other",
      hours: 1.8580555555555556,
    },
    {
      task: "In Cycle",
      hours: 1.705,
    },
    {
      task: "Panels",
      hours: 0.39361111111111113,
    },
    {
      task: "Idle-Uncategorized",
      hours: 0.2972222222222222,
    },
    {
      task: "Dry Out",
      hours: 0.2588888888888889,
    },
    {
      task: "Offline",
      hours: 0,
    },
  ];

  const additionalHstInfo = [
    {
        "status": "No Operator",
        "color": "#B0E0E6",
        "duration": 14.395
    },
    {
        "status": "Waiting Material",
        "color": "#c000db",
        "duration": 5.092222222222222
    },
    {
        "status": "Other",
        "color": "#808080",
        "duration": 1.8580555555555556
    },
    {
        "status": "In Cycle",
        "color": "#46c392",
        "duration": 1.705
    },
    {
        "status": "Panels",
        "color": "#9898db",
        "duration": 0.39361111111111113
    },
    {
        "status": "Idle-Uncategorized",
        "color": "#ff0000",
        "duration": 0.2972222222222222
    },
    {
        "status": "Dry Out",
        "color": "#ffec00",
        "duration": 0.2588888888888889
    },
    {
        "status": "Offline",
        "color": "#000000",
        "duration": -48
    }
]
  
  const config = {
    appendPadding: 10,
    data,
    angleField: "hours",
    colorField: "task",
    radius: 0.9,
    // label: {
    //   type: "outer",
    //   offset: "30%",
    //   content: ({ percent }) => {
    //     if (percent > 0.05) {
    //       return `${(percent * 100).toFixed(0)}%`;
    //     } else {
    //       return "";
    //     }
    //   },
    //   style: {
    //     fontSize: 12,
    //     textAlign: "center",
    //     fill: "rgba(255, 255, 255, 1)",
    //   },
    // },
    legend: {
      itemName: {
        style: (item, index) => {
          return {
            fill: "rgba(255, 255, 255, 0.9)",
          };
        },
      },
    },
    label: false,
    interactions: [
      {
        type: "element-active",
      },
    ],
    color: ({ task }) => {
      for (var i = 0; i < additionalHstInfo.length; i++) {
        if (task === additionalHstInfo[i].status) {
          return additionalHstInfo[i].color;
        }
      }
      return "#d62728";
    },
    tooltip: {
      formatter: (data) => {
        return { name: data.task, value: data.hours.toFixed(3) };
      },
    },
    animation: false,
  };
  

  return (
    <div className="dashboard-utilization-layout">
      <Row align="middle" className="dashboard-utilization-top">
        <Col span={24} style={{ textAlign: "center" }}>
          <span className="dashboard-utilization-title">Utilization</span>
        </Col>
      </Row>
      <Row className="dashboard-utilization-content" align={"middle"}>
        <Col span={24} style={{ textAlign: "center" }}>
          <Pie {...config} />
        </Col>
      </Row>
    </div>
  );
};

export default DashboardUtilizationLayout;
