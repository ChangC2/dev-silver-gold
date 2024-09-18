import { Col, Row, Select } from "antd";
import IndicatorItem from "components/IndicatorItem/IndicatorItem";
import { useSelector } from "react-redux";
import "./DashboardOEELayout.css";
const { Option } = Select;

const DashboardOEELayout = (props) => {
  const { hstInfo } = props;

  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id, machine_id, customer_details } = appDataStore;
  if (customer_id == "" || machine_id == "") return <div></div>;

  return (
    <div className="dashboard-oee-layout">
      <div className="dashboard-oee-container">
        <Row align="middle" className="dashboard-oee-top">
          <Col span={24}>
            <span className="dashboard-oee-title">
              {"Key Performance Indicators"}
            </span>
          </Col>
        </Row>
        <div className="dashboard-oee-content">
          <Row style={{ height: "80%" }}>
            <Col span={8} style={{ textAlign: "center" }}>
              <IndicatorItem
                id="oee_1"
                title="Utilization"
                value={Math.round(hstInfo["utilization"]) / 100.0}
              />
            </Col>
            <Col span={8} style={{ textAlign: "center" }}>
              <IndicatorItem
                id="oee_2"
                title="Availability"
                value={Math.round(hstInfo["availability"]) / 100.0}
              />
            </Col>
            <Col span={8} style={{ textAlign: "center" }}>
              <IndicatorItem
                id="oee_3"
                title="OEE"
                value={Math.round(hstInfo["oee"]) / 100.0}
              />
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default DashboardOEELayout;
