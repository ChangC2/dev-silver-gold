import { Col, Row } from "antd";
import "./CurrentPartProcessingTimeWidget.css";
import { useState } from "react";

const CurrentPartProcessingTimeWidget = (props) => {
  const {time, setTime, onStartClick, onStopClick} = props;

  return (
    <div className="current-part-processing-time-layout">
      <div className="current-part-processing-time-title">
        {"Current Part Processing Time"}
      </div>
      <Row align={"middle"} style={{ height: "80px" }}>
        <Col
          flex="120px"
          className="current-part-processing-time-start-button"
          onClick={() => {
            onStartClick();
          }}
        >
          {"Start"}
        </Col>
        <Col flex="auto" className="current-part-processing-time-value">
          {time}
        </Col>
        <Col
          flex="120px"
          className="current-part-processing-time-stop-button"
          onClick={() => {
            onStopClick();
          }}
        >
          {"Stop"}
        </Col>
      </Row>
    </div>
  );
};

export default CurrentPartProcessingTimeWidget;
