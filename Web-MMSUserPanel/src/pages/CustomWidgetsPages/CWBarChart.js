import { Column } from "@ant-design/plots";
import { Spin } from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import {
  GetCustomerCurrentTime,
  getShiftList,
} from "services/common/cnc_apis";

import { cwDataPoints } from "services/common/constants";
import "./CWBarChart.css";

const CWBarChart = (props) => {
  const { widget_info, customerInfo, darkmode } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [shiftList, setShiftList] = useState([]);

  const [data, setData] = useState([]);

  const dataPoints = widget_info.data_points.split(",");

  const config = {
    data,
    // theme: theme,
    isStack: true,
    xField: "time",
    yField: "value",
    seriesField: "data_point",
    color: [
      "#00CED1",
      "#F4664A",
      "#BA55D3",
      "#7FFFD4",
      "#9ACD32",
      "#00BFFF",
      "#E0FFFF",
      "#FFDAB9",
    ],
    // isGroup: true,
    // colorField: "type", // or seriesField in some cases
    // color: ["#2ca02c", "#d62728"],
    // columnStyle: {
    //   radius: [0, 0, 0, 0],
    // },

    slider: darkmode
      ? null
      : {
          start: 0.0,
          end: 1,
          textStyle: {
            fill: "white",
          },
        },

    legend: {
      position: "top",
    },
    // scrollbar: {
    //   type: 'horizontal',
    // },
  };

  useEffect(() => {
    setIsLoading(true);
    const param = {
      customer_id: widget_info.customer_id,
      machine: widget_info.machine,
      operator: widget_info.operator,
      jobID: widget_info.jobID,
      days: widget_info.days,
      timezone: customerInfo["timezone"],
    };

    getShiftList(param, (res) => {
      setIsLoading(false);
      if (res !== null) {
        setShiftList(res.shifts);
      }
    });
  }, [widget_info]);

  useEffect(() => {
    setChartData();
  }, [shiftList]);

  const setChartData = () => {
    const customerCurrentTime = GetCustomerCurrentTime(
      customerInfo["timezone"]
    );
    const startTime = moment(customerCurrentTime).add(
      -widget_info.days,
      "days"
    );
    const endTime = moment(customerCurrentTime);

    let chartData = [];

    while (startTime <= endTime) {
      const time = startTime.format("YYYY-MM-DD");
      for (let i = 0; i < cwDataPoints.length; i++) {
        if (dataPoints[i] === "1") {
          const dataPoint = cwDataPoints[i];
          let value = 0;
          const shift = shiftList.find((x) => x.date.includes(time));
          if (shift !== undefined) {
            switch (i) {
              case 0:
                value = parseInt(shift["goodParts"]);
                break;
              case 1:
                value = parseInt(shift["badParts"]);
                break;
              case 2:
                value = parseInt(shift["oee"]);
                break;
              case 3:
                value = parseInt(shift["availability"]);
                break;
              case 4:
                value = parseInt(shift["quality"]);
                break;
              case 5:
                value = parseInt(shift["performance"]);
                break;
              case 6:
                value = parseInt(parseInt(shift["inCycle"]) / 3600000);
                break;
              default:
                break;
            }
          }
          chartData.push({
            time: time,
            data_point: dataPoint,
            value: value,
          });
        }
      }
      startTime.add(1, "days");
    }
    setData(chartData);
  };

  useEffect(() => {}, [shiftList]);

  return (
    <Spin spinning={isLoading}>
      <Column
        {...config}
        style={{
          paddingTop: 20,
          paddingBottom: 0,
          paddingLeft: 30,
          paddingRight: 50,
          textAlign: "center",
          height: 280,
        }}
      />
    </Spin>
  );
};

export default CWBarChart;
