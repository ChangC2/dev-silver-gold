import { Modal } from "antd";
import { useState } from "react";
import { Chart } from "react-google-charts";

import { useSelector } from "react-redux";
import {
  ConvertTimespanToDateBasedOnTimezone, getBetweenTime, GetCustomerCurrentTime,
  GetTimeWithStyle
} from "../../../../services/common/cnc_apis";
import lang from "../../../../services/lang";
import CommentMgrDlg from "../../CommentMgrDlg/CommentMgrDlg";

function DetailGanttModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { showDetailGanttModal, setShowDetailGanttModal, security_level, customer_id } = props;
  const { ganttInfo } = props;
  const { machineInfo } = props;
  const { customerInfo } = props;
  const { myGanttList, setMyGanttList } = props;
  const [selectedChatItem, setSelectedChatItem] = useState("");
  const [isVisibleModal, setIsVisibleModal] = useState(false);

  let ganttData = [];
  let color_array = [];
  let status_array = [];
  let identificationList = [];

  const columns = [
    { type: "string", id: "Position" },
    { type: "string", id: "dummy bar label" },
    { type: "string", role: "tooltip", id: "tmp", p: { html: true } },
    { type: "date", id: "Start" },
    { type: "date", id: "End" },
  ];
  const timezone = customerInfo["timezone"];
  const startDate = GetCustomerCurrentTime(timezone);
  let endDate = GetCustomerCurrentTime(timezone);
  endDate.setDate(endDate.getDate() + 1);


  if (ganttInfo !== undefined && ganttInfo.length > 0) {
    var startTime = ganttInfo[0]["start"];
    var startStatus = ganttInfo[0]["status"].toUpperCase();
    for (var i = 0; i < ganttInfo.length; i++) {
      ganttData.push([
        ganttInfo[i]["status"],
        null,
        // ganttInfo[i]['status'] + "  " + ganttInfo[i]['color'],
        createCustomHTMLContent(
          ganttInfo[i]["status"],
          ConvertTimespanToDateBasedOnTimezone(ganttInfo[i]["start"], timezone),
          ConvertTimespanToDateBasedOnTimezone(ganttInfo[i]["end"], timezone),
          ganttInfo[i]["Operator"],
          getBetweenTime(ganttInfo[i]["start"], ganttInfo[i]["end"]),
          ganttInfo[i]["job_id"] === undefined ? "" : ganttInfo[i]["job_id"],
          ganttInfo[i]["comment"] === null ||
            ganttInfo[i]["comment"] === undefined
            ? ""
            : ganttInfo[i]["comment"]
        ),
        ConvertTimespanToDateBasedOnTimezone(ganttInfo[i]["start"], timezone),
        ConvertTimespanToDateBasedOnTimezone(ganttInfo[i]["end"], timezone),
      ]);

      if (
        status_array.filter(
          (item) => item.toLowerCase() === ganttInfo[i]["status"].toLowerCase()
        ).length === 0
      ) {
        status_array.push(ganttInfo[i]["status"]);
        color_array.push(ganttInfo[i]["color"]);
      }
    }
    identificationList = identificationList.concat(
      ganttInfo.map((item) => item)
    );
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
      "<span>"+lang(langIndex, 'cnc_status')+": <b>" +
      status +
      "</b></span><br>" +
      "<span>"+lang(langIndex, 'cnc_time')+": <b>" +
      GetTimeWithStyle(from) +
      " - " +
      GetTimeWithStyle(to) +
      "</b></span><br>" +
      "<span>"+lang(langIndex, 'cnc_duration')+": <b>" +
      duration +
      "</b></span><br>" +
      "<span>"+lang(langIndex, "plant_operator")+": <b>" +
      operator +
      "</b></span><br>" +
      "<span>"+lang(langIndex, 'cnc_jobid')+": <b>" +
      jobId +
      "</b></span><br>" +
      '<span>'+lang(langIndex, 'cnc_comment')+': </span><div style="margin-left:20px"><b>' +
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
  const height = status_array.length * 41 + 50;
  return (
    <Modal
      className="detail-gantt-modal"
      backgroundColor={"#1e1e1e"}
      visible={showDetailGanttModal}
      title={null}
      onCancel={() => setShowDetailGanttModal(false)}
      closable={true}
      width={1000}
      height={height + 40}
      footer={null}
      centered
      // style={{ padding: 20 }}
    >
      <div>
        {isVisibleModal && (
          <CommentMgrDlg
            selectedChatItem={selectedChatItem}
            isVisibleModal={isVisibleModal}
            setIsVisibleModal={setIsVisibleModal}
            security_level={security_level}
            customer_id={customer_id}
            myGanttList={myGanttList}
            setMyGanttList={setMyGanttList}
            machineInfo={machineInfo}
            timezone={timezone}
          />
        )}
        <div className="detailed-chart-container">
          {ganttInfo === undefined || ganttInfo.length === 0 ? (
            <h2 style={{ color: "white", textAlign: "center" }}>
              {" "}
              {lang(langIndex, "cnc_nodata")}{" "}
            </h2>
          ) : (
            <Chart
              className="detailed-line-timeline"
              // height={100}
              width={"100%"}
              chartType="Timeline"
              data={[columns, ...ganttData]}
              height={height}
              chartEvents={chartEvents}
              options={{
                showRowNumber: false,
                showBarLabels: false,
                showName: false,
                timeline: { showRowLabels: true, showBarLabels: false },
                backgroundColor: "transparent",
                legend: "none",
                colors: color_array,
                height: { height },
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
                chartArea: {
                  width: "50%",
                  height: "100%",
                },
                explorer: { actions: ["dragToPan"] },
              }}
            />
          )}
        </div>
      </div>
    </Modal>
  );
}

export default DetailGanttModal;
