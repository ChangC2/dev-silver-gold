// @flow strict

import { Col, Row } from "antd";
import MachineStatusButton from "components/MachineStatusButton/MachineStatusButton";
import { useDispatch, useSelector } from "react-redux";
import "./MachineStatus.css";

function MachineStatus(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { machine_status } = appDataStore;
  const { setShowDowntimeReasonView, updateMachineStatus } = props;

  return (
    <div className="machine-status-page">
      <div className="machine-status-content">
        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              status={machine_status[0]}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              marginLeft={"10px"}
              status={machine_status[1]}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              status={machine_status[2]}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              marginLeft={"10px"}
              status={machine_status[3]}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              status={machine_status[4]}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              marginLeft={"10px"}
              status={machine_status[5]}
            />
          </Col>
        </Row>

        <Row className="machine-status-button-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              status={machine_status[6]}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <MachineStatusButton
              updateMachineStatus={updateMachineStatus}
              marginLeft={"10px"}
              status={machine_status[7]}
            />
          </Col>
        </Row>
      </div>

      <Row
        align="middle"
        className="machine-status-close"
        onClick={() => {
          setShowDowntimeReasonView(false);
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
