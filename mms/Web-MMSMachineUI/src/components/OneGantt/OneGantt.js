import { Spin } from "antd";
import { useEffect, useState } from "react";
import { Chart } from "react-google-charts";

import { useSelector } from "react-redux";
import {
  ConvertTimestampToDateBasedOnTimezone,
  GetCustomerCurrentTime
} from "services/global";
import "./OneGantt.css";

function OneGantt(props) {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { machine_id, customer_details } = appDataStore;
  const { ganttInfo } = props;

  let timezone =
    customer_details != undefined && customer_details.timezone != undefined
      ? customer_details.timezone
      : 0;

  const [chartData, setChartData] = useState([]);
  const [chartColors, setChartColors] = useState([]);

  useEffect(() => {
    setGanttInfo(ganttInfo);
  }, [ganttInfo]);

  const columns = [
    { type: "string", id: "Position" },
    { type: "string", id: "President" },
    { type: "string", role: "tooltip", id: "tmp", p: { html: true } },
    { type: "datetime", id: "Start" },
    { type: "datetime", id: "End" },
  ];

  const setGanttInfo = (ganttInfo) => {
    let ganttData = [];
    let identificationList = [];
    let color_array = [];
    let status_array = [];

    if (ganttInfo !== undefined && ganttInfo.length > 0) {
      let startDate = GetCustomerCurrentTime(timezone);
      startDate.setHours(0, 0, 0);

      let endDate = GetCustomerCurrentTime(timezone);
      endDate.setHours(0, 0, 0);
      endDate.setDate(endDate.getDate() + 1);

      if (startDate.getTime() / 1000 < parseInt(ganttInfo[0].start)) {
        status_array.push("start");
        color_array.push("white");
        ganttData.push([machine_id, "start", "", startDate, startDate]);
        identificationList.push("");
      }
      ganttData = ganttData.concat(
        ganttInfo.map((item) => [
          machine_id,
          item["status"],
          "",
          ConvertTimestampToDateBasedOnTimezone(item["start"], timezone),
          ConvertTimestampToDateBasedOnTimezone(item["end"], timezone),
        ])
      );

      identificationList = identificationList.concat(
        ganttInfo.map((item) => item)
      );

      for (var i = 0; i < ganttInfo.length; i++) {
        if (
          status_array.filter(
            (item) =>
              item.toLowerCase() === ganttInfo[i]["status"].toLowerCase()
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
          ganttData.push([machine_id, "end", "", endDate, endDate]);
          identificationList.push("");
        }
      }
      setChartData([columns, ...ganttData]);
      setChartColors(color_array);
    }
  };

  return (
    <div style={{ textAlign: "center", paddingTop: 60, marginLeft: 20 }}>
      {chartData.length === 0 || ganttInfo.length == 0 ? (
        <Spin size="medium"></Spin>
      ) : (
        <div>
          <Chart
            className="one-line-timeline"
            height={"90%"}
            width={"100%"}
            chartType="Timeline"
            data={chartData}
            options={{
              showRowNumber: false,
              showBarLabels: false,
              showName: false,
              timeline: { showRowLabels: false, showBarLabels: false },
              backgroundColor: "transparent",
              legend: "none",
              colors: chartColors,
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
