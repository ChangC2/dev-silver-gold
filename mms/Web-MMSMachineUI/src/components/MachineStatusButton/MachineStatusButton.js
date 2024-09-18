// @flow strict

import { Col, Row } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { secondToTime } from "services/global";
import "./MachineStatusButton.css";

function MachineStatusButton(props) {
  const dispatch = useDispatch();
  const { status, marginLeft, updateMachineStatus } = props;
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { last_ganttdata } = appDataStore;
  const [elapsedTime, setElapsedTime] = useState("00:00:00");

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);

  useEffect(() => {
    clearInterval(intervalId);
    setIntervalID(setInterval(timer, 1000));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    let currentTimestamp = Math.floor(new Date().getTime() / 1000);
    setElapsedTime(
      secondToTime((currentTimestamp - last_ganttdata.start) * 1000)
    );
  }, [tick]);

  return (
    <Row
      className="machine-status-button"
      style={{
        backgroundColor:
          status === last_ganttdata.status
            ? "rgba(206, 15, 253, 0.7)"
            : "rgba(255, 255, 255, 0.6)",
        marginLeft: marginLeft !== undefined ? marginLeft : "0px",
      }}
      align="middle"
      onClick={() => updateMachineStatus(status)}
    >
      <Col span={24} style={{ textAlign: "center" }}>
        <div style={{ display: "block" }}>
          <div
            className="machine-status-text"
            style={{
              color: status === last_ganttdata.status ? "white" : "black",
            }}
            align="middle"
          >
            {status}
          </div>
          {status === last_ganttdata.status && (
            <div
              className="machine-status-text"
              style={{
                color: status === last_ganttdata.status ? "white" : "black",
              }}
              align="middle"
            >
              {elapsedTime}
            </div>
          )}
        </div>
      </Col>
    </Row>
  );
}

export default MachineStatusButton;
