import "./DashboardChartLayout.css";
import cholesterolIcon from "../../assets/icons/ic_fab_cholesterol.png";
import { Col, Row } from "antd";
import OneGantt from "components/OneGantt/OneGantt";

import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { getBetweenTime } from "services/global";

const DashboardChartLayout = (props) => {
  const [title, setTitle] = useState("Timeline");
  const { ganttInfo, shift, shifts } = props;

  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id, machine_id } = appDataStore;

  useEffect(() => {
    setTitle("Timeline");
  }, [shift, shifts]);

  if (customer_id == "" || machine_id == "") return <div></div>;

  return (
    <div className="dashboard-chart-layout">
      <div className="dashboard-chart-container">
        <Row align="middle" className="dashboard-chart-top">
          <Col span={24}>
            <span className="dashboard-chart-title">{title}</span>
          </Col>
        </Row>
        <Row className="dashboard-chart-content" align={"middle"}>
          <Col span={24} style={{ textAlign: "center" }}>
            <OneGantt ganttInfo={ganttInfo} />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DashboardChartLayout;
