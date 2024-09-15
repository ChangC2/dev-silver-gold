import { Col, Row, message } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { STATUS_COLORS } from "services/CONSTANTS";
import {
  GetCustomerCurrentTime,
  appData,
  isValid,
  secondToTime,
  timeToSecond,
} from "services/global";
import "./BottomLayout.css";

const BottomLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const {
    customer_id,
    machine_id,
    customer_details,
    machine_status,
    current_ganttdata,
    last_ganttdata,
  } = appDataStore;

  const { setShowDowntimeReasonView, updateMachineStatus } = props;

  const [elapsedTime, setElapsedTime] = useState("00:00:00");

  const [currentTime, setCurrentTime] = useState(new Date());
  const [customerTime, setCustomerTime] = useState(new Date());

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
    setCurrentTime(new Date());
    let timezone =
      customer_details.timezone != undefined ? customer_details.timezone : 0;
    setCustomerTime(GetCustomerCurrentTime(timezone));

    if (customer_id == "" || machine_id == "") {
      setElapsedTime("00:00:00");
      return;
    }

    let currentTimestamp = Math.floor(new Date().getTime() / 1000);
    setElapsedTime(
      secondToTime((currentTimestamp - last_ganttdata.start) * 1000)
    );

    let isTimeLimit =
      currentTimestamp - current_ganttdata.start >
      timeToSecond(appDataStore.time_stop) / 1000;
    let isInCyle = current_ganttdata.status == machine_status[9];
    let isOffline = current_ganttdata.status == machine_status[10];

    if (isTimeLimit && isInCyle && !isOffline) {
      updateMachineStatus(machine_status[8]);
      return;
    }

    if (isTimeLimit && elapsedTime % timeToSecond(appData.time_stop) == 0) {
      setShowDowntimeReasonView(true);
      return;
    }
  }, [tick]);

  const onClickInCyle = () => {
    message.info("onClickInCyle");
  };

  const formattedTime = currentTime.toLocaleString("en-US", {
    year: "2-digit",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    weekday: "short",
    //week: 'numeric'
  });

  const formattedCustomerTime = customerTime.toLocaleString("en-US", {
    year: "2-digit",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    weekday: "short",
    //week: 'numeric'
  });

  const onClickIdle = () => {
    // if not in cycle and not offline
    let isInCyle = last_ganttdata["status"] == machine_status[9];
    let isOffline = last_ganttdata["status"] == machine_status[10];
    if (!isInCyle && !isOffline) setShowDowntimeReasonView(true);
  };

  if (customer_id == "" || machine_id == "") return <div></div>;

  return (
    <div className="bottom-layout">
      <Row align="middle" className="bottom-container">
        <Col flex="auto">
          <span
            className="bottom-status"
            style={{ color: STATUS_COLORS[9] }}
            onClick={onClickInCyle}
          >
            {machine_status[9]} :{" "}
            {last_ganttdata["status"] == machine_status[9]
              ? elapsedTime
              : "00:00:00"}
          </span>

          <span
            className="bottom-status"
            style={{
              color:
                isValid(last_ganttdata["color"]) &&
                last_ganttdata["status"] !== machine_status[9]
                  ? last_ganttdata["color"]
                  : "#ff0000",
              cursor: "pointer",
            }}
            onClick={onClickIdle}
          >
            {isValid(last_ganttdata["status"]) &&
            last_ganttdata["status"] !== machine_status[9]
              ? last_ganttdata["status"]
              : "Idle Uncategorized"}{" "}
            :{" "}
            {last_ganttdata["status"] !== machine_status[9] &&
            isValid(elapsedTime)
              ? elapsedTime
              : "00:00:00"}
          </span>
        </Col>
        <Col flex="255px">
          <div>
            <span className="bottom-time">
              Factory Time : {formattedCustomerTime}
            </span>
          </div>
          <div>
            <span className="bottom-time">Current Time : {formattedTime}</span>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default BottomLayout;
