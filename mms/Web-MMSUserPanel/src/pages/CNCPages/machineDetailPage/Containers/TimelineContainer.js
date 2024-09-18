import { Col, Row } from "antd";
import OneGantt from "../../MainPage/MachineItem_Grid/OneGantt";

import { useSelector } from "react-redux";
import {
  ConvertTimespanToDateBasedOnTimezone,
  getBetweenTime,
  humanitizeDuration,
} from "../../../../services/common/cnc_apis";
import { sizePad } from "../../../../services/common/constants";
import lang from "../../../../services/lang";
import { useEffect, useState } from "react";

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
    <div className="timeline-container">
      <div style={{ borderBottom: "1px solid white", textAlign: "left" }}>
        <Row style={{ padding: 0, margin: 0 }}>
          <Col span={10} style={{ textAlign: "left" }}>
            <h4 style={{ color: "#eeeeee" }}>{title}</h4>
          </Col>
          <Col span={14} style={{ textAlign: "right" }}>
            <h4
              className="see-detail-gantt-modal"
              onClick={() => setShowDetailGanttModal(true)}
            >
              {screenSize.width >= sizePad
                ? lang(langIndex, "cnc_seedetail")
                : lang(langIndex, "cnc_detailchart")}
            </h4>
          </Col>
        </Row>
      </div>
      {infoStatusUI(`${lang(langIndex, "cnc_status")}:`, statusName)}
      {infoStatusUI(
        `${lang(langIndex, "cnc_starttime")}:`,
        statusName === lang(langIndex, "cnc_offline")
          ? "- - -"
          : ganttInfo.length
          ? ConvertTimespanToDateBasedOnTimezone(
              ganttInfo[ganttInfo.length - 1]["start"],
              customerInfo["timezone"]
            ).toLocaleString()
          : "- - -"
      )}
      {infoStatusUI(
        `${lang(langIndex, "cnc_duration")}:`,
        statusName === lang(langIndex, "cnc_offline") ? "- - -" : duration
      )}
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
    </div>
  );
}

export default TimelineContainer;
