import { Pie } from "@ant-design/plots";
import { Col, Row } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./DashboardUtilizationLayout.css";

const DashboardUtilizationLayout = (props) => {
  const { additionalHstInfo, shift, shifts } = props;
  const [title, setTitle] = useState("Utilization");

  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id, machine_id } = appDataStore;

  useEffect(() => {
    setTitle("Utilization");
  }, [shift, shifts]);

  additionalHstInfo.sort(function (a, b) {
    if (a.duration < b.duration) {
      return 1;
    }
    if (a.duration > b.duration) {
      return -1;
    }
    return 0;
  });

  const data = [];
  for (var i = 0; i < additionalHstInfo.length; i++) {
    data.push({
      task: additionalHstInfo[i].status,
      hours:
        additionalHstInfo[i].duration <= 0 ? 0 : additionalHstInfo[i].duration,
    });
  }

  const config = {
    appendPadding: 10,
    data,
    angleField: "hours",
    colorField: "task",
    radius: 0.9,
    legend: {
      layout: "vertical",
      position: "right",
      offsetX: -40,
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

  if (customer_id == "" || machine_id == "") return <div></div>;

  return (
    <div className="dashboard-utilization-layout">
      <div className="dashboard-utilization-container">
        <Row align="middle" className="dashboard-utilization-top">
          <Col span={24}>
            <span className="dashboard-utilization-title">{title}</span>
          </Col>
        </Row>
        <Row className="dashboard-utilization-content" align={"middle"}>
          <Col span={24} style={{ textAlign: "center" }}>
            <Pie {...config} style={{ height: 235, marginTop: -10 }} />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DashboardUtilizationLayout;
