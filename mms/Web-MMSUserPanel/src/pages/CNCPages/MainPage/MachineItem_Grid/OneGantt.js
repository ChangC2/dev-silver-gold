import { Spin } from "antd";
import { useState } from "react";
import { Chart } from "react-google-charts";
import {
  ConvertTimespanToDateBasedOnTimezone,
  getBetweenTime,
  GetCustomerCurrentTime,
  GetTimeWithStyle,
} from "../../../../services/common/cnc_apis";

import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import CommentMgrDlg from "../../CommentMgrDlg/CommentMgrDlg";
import "./OneGantt.css";

function OneGantt(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { ganttInfo, security_level } = props;
  const { machineInfo, customer_id } = props;
  const { customerInfo, currentDate } = props;
  const { myGanttList, setMyGanttList } = props;

  const [selectedChatItem, setSelectedChatItem] = useState("");
  const [isVisibleModal, setIsVisibleModal] = useState(false);
  let ganttData = [];
  let identificationList = [];
  let color_array = [];
  let status_array = [];


  const columns = [
    { type: "string", id: "Position" },
    { type: "string", id: "President" },
    { type: "string", role: "tooltip", id: "tmp", p: { html: true } },
    { type: "datetime", id: "Start" },
    { type: "datetime", id: "End" },
  ];

  if (ganttInfo !== undefined && ganttInfo.length > 0) {
    const timezone = customerInfo["timezone"];
    let startDate = GetCustomerCurrentTime(timezone);
    if (currentDate !== undefined && currentDate !== "") {
      startDate = new Date(currentDate);
    } else {
      startDate.setHours(0, 0, 0);
    }

    let endDate = GetCustomerCurrentTime(timezone);
    if (currentDate !== undefined && currentDate !== "") {
      endDate = new Date(currentDate);
    } else {
      endDate.setHours(0, 0, 0);
    }

    endDate.setDate(endDate.getDate() + 1);

    if (startDate.getTime() / 1000 < parseInt(ganttInfo[0].start)) {
      status_array.push("start");
      color_array.push("white");
      ganttData.push([
        machineInfo["machine_id"],
        "start",
        "",
        startDate,
        startDate,
      ]);
      identificationList.push("");
    }

    ganttData = ganttData.concat(
      ganttInfo.map((item) => [
        machineInfo["machine_id"],
        item["status"],
        createCustomHTMLContent(
          item["status"],
          ConvertTimespanToDateBasedOnTimezone(item["start"], timezone),
          ConvertTimespanToDateBasedOnTimezone(item["end"], timezone),
          item["Operator"],
          getBetweenTime(item["start"], item["end"]),
          item["job_id"] === undefined ? "" : item["job_id"],
          item["comment"] === null || item["comment"] === undefined
            ? ""
            : item["comment"]
        ),
        ConvertTimespanToDateBasedOnTimezone(item["start"], timezone),
        ConvertTimespanToDateBasedOnTimezone(item["end"], timezone),
      ])
    );
    
    identificationList = identificationList.concat(
      ganttInfo.map((item) => item)
    );

    for (var i = 0; i < ganttInfo.length; i++) {
      if (
        status_array.filter(
          (item) => item.toLowerCase() === ganttInfo[i]["status"].toLowerCase()
        ).length === 0
      ) {
        status_array.push(ganttInfo[i]["status"]);
        color_array.push(ganttInfo[i]["color"]);
      }
    }

    if (ganttData.length > 0) {
      if (endDate > ganttData[ganttData.length - 1][3]) {
        status_array.push("end");
        color_array.push("white");
        ganttData.push([
          machineInfo["machine_id"],
          "end",
          "",
          endDate,
          endDate,
        ]);
        identificationList.push("");
      }
    }
  }


  function createCustomHTMLContent(
    status,
    from,
    to,
    operator,
    duration,
    jobId,
    comment
  ) {
    return (
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; ">' +
      "<span>" +
      lang(langIndex, "cnc_status") +
      ": <b>" +
      status +
      "</b></span><br>" +
      "<span>" +
      lang(langIndex, "cnc_time") +
      ": <b>" +
      GetTimeWithStyle(from) +
      " - " +
      GetTimeWithStyle(to) +
      "</b></span><br>" +
      "<span>" +
      lang(langIndex, "cnc_duration") +
      ": <b>" +
      duration +
      "</b></span><br>" +
      "<span>" +
      lang(langIndex, "plant_operator") +
      ": <b>" +
      operator +
      "</b></span><br>" +
      "<span>" +
      lang(langIndex, "cnc_jobid") +
      ": <b>" +
      jobId +
      "</b></span><br>" +
      "<span>" +
      lang(langIndex, "cnc_comment") +
      ': </span><div style="margin-left:20px"><b>' +
      comment +
      "</b></div>" +
      "</div>"
    );
  }

  const chartEvents = [
    {
      eventName: "select",
      callback({ chartWrapper }) {
        if (customer_id === undefined) return;
        var selectedRow = chartWrapper.getChart().getSelection()[0].row;
        var identification = identificationList[selectedRow];
        setSelectedChatItem(identification);
        setIsVisibleModal(true);
      },
    },
  ];

  
  return (
    <div style={{ textAlign: "center", paddingTop: 15 }}>
      {isVisibleModal && (
        <CommentMgrDlg
          selectedChatItem={selectedChatItem}
          isVisibleModal={isVisibleModal}
          setIsVisibleModal={setIsVisibleModal}
          customer_id={customer_id}
          myGanttList={myGanttList}
          security_level={security_level}
          setMyGanttList={setMyGanttList}
          timezone={customerInfo["timezone"]}
          ganttInfo={ganttInfo}
          machineInfo={machineInfo}
        />
      )}

      {ganttInfo === undefined || ganttData.length === 0 ? (
        <Spin size="medium"></Spin>
      ) : (
        <div>
          <Chart
            className="one-line-timeline"
            height={100}
            width={"100%"}
            chartType="Timeline"
            data={[columns, ...ganttData]}
            chartEvents={chartEvents}
            options={{
              showRowNumber: false,
              showBarLabels: false,
              showName: false,
              timeline: { showRowLabels: false, showBarLabels: false },
              backgroundColor: "transparent",
              legend: "none",
              colors: color_array,
              allowHtml: true,
              tooltip: { isHtml: true },
              hAxis: {
                textStyle: {
                  color: "#FFFFFF",
                },
                gridlines: {
                  color: "#FFFFFF",
                },
                baselineColor: "#FFFFFF",
              },
            }}
          />
        </div>
      )}
    </div>
  );
}

export default OneGantt;
