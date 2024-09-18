import { Col, Row } from "antd";
import "./AmbientPaintWidget.css";

const AmbientPaintWidget = (props) => {
  const { title, temp, hum, dew } = props;

  return (
    <div className="ambient-paint-layout">
      <div className="ambient-paint-title">{title}</div>
      <Row style={{ height: "65px", paddingTop:"5px" }}>
        <Col span={8} style={{ textAlign: "center" }}>
          <div className="ambient-paint-value">{`${temp.toFixed(1)}°F`}</div>
          <div className="ambient-paint-name">{"Temp"}</div>
        </Col>
        <Col span={8} style={{ textAlign: "center" }}>
          <div className="ambient-paint-value">{`${hum.toFixed(1)}°F`}</div>
          <div className="ambient-paint-name">{"Humidity"}</div>
        </Col>
        <Col span={8} style={{ textAlign: "center" }}>
          <div className="ambient-paint-value">{`${dew.toFixed(1)}°F`}</div>
          <div className="ambient-paint-name">{"Dew Point"}</div>
        </Col>
      </Row>
    </div>
  );
};

export default AmbientPaintWidget;
