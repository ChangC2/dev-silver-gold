import { Col, Row } from "antd";
import OneGantt from "../../MainPage/MachineItem_Grid/OneGantt";

import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { humanitizeDuration } from "../../../../../src/services/common/cnc_apis";
import lang from "../../../../../src/services/lang";

function TimelineContainer(props) {
  const { langIndex } = useSelector((x) => x.app);
  const [title, setTitle] = useState("Timeline");
  const {
    ganttInfo,
    machineInfo,
    customerInfo,
    setShowDetailGanttModal,
    currentDate,
    security_level,
    screenSize,
    shift,
    shifts,
  } = props;
  const { myGanttList, setMyGanttList } = props;
  const { customer_id } = props;
  let statusName = machineInfo["status"];
  let duration = humanitizeDuration(machineInfo["elapsed"]);

  useEffect(() => {
    if (shift == 0) {
      setTitle("Timeline  " + "(24 Hours)");
    } else {
      if (shifts.length > shift) {
        setTitle(
          "Timeline  " +
            "(Shift " +
            shift.toString() +
            " (" +
            shifts[shift] +
            "))"
        );
      }
    }
  }, [shift, shifts]);

  function infoStatusUI(title, value) {
    return (
      <Row style={{ textAlign: "left", color: "#eeeeee" }}>
        <Col span={6}>{title}</Col>
        <Col span={18}>{value}</Col>
      </Row>
    );
  }

  // compare last gantt and current time, and set the status of machine
  if (ganttInfo != undefined && ganttInfo.length > 0) {
    // get last gantt data
    var lastGanttTime = ganttInfo[ganttInfo.length - 1].end;
    var currentTime = Math.round(new Date().getTime() / 1000);
    if (currentTime - lastGanttTime > 60 * 5) {
      statusName = lang(langIndex, "cnc_offline");
    }
  }
  // if(ganttInfo.length > 0)
  return (
    <Row className="utilization-detail-chart-container">
      <Col span={24} className="utilization-detail-chart-title">
        {title}
      </Col>
      <Col
        span={24}
        className="utilization-detail-chart-timeline"
      >
        <OneGantt
          ganttInfo={ganttInfo}
          machineInfo={machineInfo}
          customerInfo={customerInfo}
          currentDate={currentDate}
          customer_id={customer_id}
          myGanttList={myGanttList}
          security_level={security_level}
          setMyGanttList={setMyGanttList}
        />
      </Col>
    </Row>
  );
}

export default TimelineContainer;
