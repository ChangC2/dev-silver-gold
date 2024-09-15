// @flow strict

import { Col, Input, Row, message } from "antd";
import React from "react";
import "./MachineStatusButton.css";

function MachineStatusButton(props) {
  const {
    statusTime,
    setStausTime,
    selectedStatus,
    setSelectedStatus,
    status,
    marginLeft,
    marginRight,
  } = props;

  const onClick = () => {
    setSelectedStatus(status)
  };

  return (
    <Row
      className="machine-status-button"
      style={{
        backgroundColor:
          status === selectedStatus
            ? "rgba(206, 15, 253, 0.7)"
            : "rgba(255, 255, 255, 0.6)",
        marginLeft: marginLeft !== undefined ? marginLeft : "0px",
      }}
      align="middle"
      onClick={()=>onClick()}
    >
      <Col span={24} style={{ textAlign: "center" }}>
        <div style={{ display: "block" }}>
          <div
            className="machine-status-text"
            style={{
              color: status === selectedStatus ? "white" : "black",
            }}
            align="middle"
          >
            {status}
          </div>
          <div
            className="machine-status-text"
            style={{
              color: status === selectedStatus ? "white" : "black",
            }}
            align="middle"
          >
            {statusTime}
          </div>
        </div>
      </Col>
    </Row>
  );
}

export default MachineStatusButton;
