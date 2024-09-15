// @flow strict

import { message, Button, Col, Divider, Input, Row, Drawer } from "antd";
import { useAuth } from "navigation/ProvideAuth";
import React, { useState } from "react";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./MachineStatus.css";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import MachineStatusButton from "components/MachineStatusButton/MachineStatusButton";
import { STATUS_COLORS } from "services/CONSTANTS";
import { appData, isValidTime, setAppData } from "services/global";
import { setAppDataStore } from "redux/actions/appActions";

function MachineStatus(props) {
  const dispatch = useDispatch();
  const [isBusy, setIsBusy] = useState(false);
  const [elapsedIdleTime, setElapsedIdleTime] = useState("00:10:05");
  const [statusTime, setStausTime] = useState("00:04:05");
  const [selectedStatus, setSelectedStatus] = useState("Clear Chips");

  const history = useHistory();

  const onClick = () => {};

  return (
    <div className="machine-status-page">
      <Row align="middle" className="machine-status-top">
        <Col span={24}>
          <span className="machine-status-title">
            Elapsed Idle Time : {elapsedIdleTime}
          </span>
        </Col>
      </Row>

      <div className="machine-status-content">
        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              status={STATUS_COLORS[0]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              marginLeft={"10px"}
              status={STATUS_COLORS[1]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              status={STATUS_COLORS[2]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              marginLeft={"10px"}
              status={STATUS_COLORS[3]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              status={STATUS_COLORS[4]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              marginLeft={"10px"}
              status={STATUS_COLORS[5]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              status={STATUS_COLORS[6]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              marginLeft={"10px"}
              status={STATUS_COLORS[7]["status"]}
              selectedStatus={selectedStatus}
              statusTime={statusTime}
              setSelectedStatus={setSelectedStatus}
            />
          </Col>
        </Row>
      </div>

      <Row
        align="middle"
        className="machine-status-close"
        onClick={() => {
          let newPages = [...appData.pages];
          newPages.splice(-1);
          appData.pages = [... newPages];
          dispatch(setAppDataStore(appData));
        }}
      >
        <Col span={24}>
          <span className="machine-status-close-text">Close</span>
        </Col>
      </Row>
    </div>
  );
}

export default MachineStatus;
