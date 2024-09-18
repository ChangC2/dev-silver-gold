import "./DashboardChartLayout.css";
import cholesterolIcon from "../../assets/icons/ic_fab_cholesterol.png";
import { Col, Row } from "antd";
import OneGantt from "components/OneGantt/OneGantt";

const DashboardChartLayout = (props) => {
  return (
    <div className="dashboard-chart-layout">
      <div>
        <Row align="middle" className="dashboard-chart-container">
          <Col flex={"100px"}>
            {" "}
            <img className="dashboard-chart-icon" src={cholesterolIcon} alt="chart-icon"/>
          </Col>
          <Col flex={"auto"}>
            <OneGantt />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DashboardChartLayout;
