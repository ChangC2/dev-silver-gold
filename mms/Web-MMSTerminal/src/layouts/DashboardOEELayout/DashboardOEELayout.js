import { Col, Row } from "antd";
import IndicatorItem from "components/IndicatorItem/IndicatorItem";
import "./DashboardOEELayout.css";

const DashboardOEELayout = (props) => {
  return (
    <div className="dashboard-oee-layout">
      <Row align="middle" className="dashboard-oee-top">
        <Col span={24} style={{ textAlign: "center" }}>
          <span className="dashboard-oee-title">OEE</span>
        </Col>
      </Row>
      <div className="dashboard-oee-content">
        <Row style={{ height: "50%" }}>
          <Col span={12} style={{ textAlign: "center" }}>
            <IndicatorItem id="oee_1" title="Availability" value={0.5} />
          </Col>
          <Col span={12} style={{ textAlign: "center" }}>
            <IndicatorItem id="oee_2" title="OEE" value={0.5} />
          </Col>
        </Row>
        <Row style={{ height: "50%" }}>
          <Col span={12} style={{ textAlign: "center" }}>
            <IndicatorItem id="oee_3" title="Perfermance" value={0.5} />
          </Col>
          <Col span={12} style={{ textAlign: "center" }}>
            <IndicatorItem id="oee_4" title="Quality" value={0.5} />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DashboardOEELayout;
