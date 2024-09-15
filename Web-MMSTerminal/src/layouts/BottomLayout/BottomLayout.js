import { Col, Row, message } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { STATUS_COLORS } from "services/CONSTANTS";
import { appData } from "services/global";
import "./BottomLayout.css";

const BottomLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;

  const [idleStatus, setIdleStatus] = useState("Clear Chips");
  const [idleStatusColor, setIdleStatusColor] = useState("#ffffff");
  const [inCyleStatusTime, setInCyleStatusTime] = useState("00:00:00");
  const [idleStatusTime, setIdleStatusTime] = useState("00:00:00");

  const [uncategorizedStatusTime, setUncategorizedStatusTime] =
    useState("00:00:00");
  const [currentTime, setCurrentTime] = useState(new Date());

  let inCyleStatus = "In Cycle";
  let cColor = STATUS_COLORS.find((item) => item["status"] === inCyleStatus);
  let inCyleStatulColor =
    cColor["color"] === undefined ? "#46c392" : cColor["color"];

  let uncategorizedStatus = "Uncategorized";
  cColor = STATUS_COLORS.find((item) => item["status"] === uncategorizedStatus);
  let uncategorizedStatusColor =
    cColor["color"] === undefined ? "#ff0000" : cColor["color"];

  useEffect(() => {
    let cColor = STATUS_COLORS.find((item) => item["status"] === idleStatus);
    setIdleStatusColor(
      cColor["color"] === undefined ? "#ffffff" : cColor["color"]
    );
  }, [idleStatus]);

  const onClickInCyle = () => {
    message.info("onClickInCyle");
  };

  const formattedTime = currentTime.toLocaleString("en-US", {
    year: "2-digit",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    weekday: "short",
    //week: 'numeric'
  });

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const onClickIdle = () => {
    let newPages = pages;
    newPages.push(6); // page_index : 6 => show MachineStatusPage
    appData.pages = newPages;
    dispatch(setAppDataStore(appData));
  };

  const onClickUncategorized = () => {
    message.info("onClickUncategorized");
  };

  return (
    <div
      className={
        pages[pages.length - 1] === 0 || pages[pages.length - 1] === 1
          ? "bottom-layout"
          : "display-none"
      }
    >
      <Row align="middle" className="bottom-container">
        <Col flex="auto">
          <span
            className="bottom-status"
            style={{ color: inCyleStatulColor }}
            onClick={onClickInCyle}
          >
            {inCyleStatus} : {inCyleStatusTime}
          </span>

          <span
            className="bottom-status"
            style={{ color: idleStatusColor }}
            onClick={onClickIdle}
          >
            {"Idle"} : {idleStatusTime}
          </span>

          <span
            className="bottom-status"
            style={{ color: uncategorizedStatusColor }}
            onClick={onClickUncategorized}
          >
            {uncategorizedStatus} : {uncategorizedStatusTime}
          </span>
        </Col>
        <Col flex="300px" style={{ textAlign: "right" }}>
          <span className="bottom-time">{formattedTime}</span>
        </Col>
      </Row>
    </div>
  );
};

export default BottomLayout;
