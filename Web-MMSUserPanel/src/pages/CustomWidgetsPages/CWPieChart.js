import { Pie, G2 } from "@ant-design/plots";
import { Spin } from "antd";
import { useEffect, useState } from "react";

import { cwDataPoints } from "services/common/constants";
import { getShiftList } from "services/common/cnc_apis";
import "./CWPieChart.css";

const CWPieChart = (props) => {
  const { widget_info, customerInfo } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [shiftList, setShiftList] = useState([]);

  const [data, setData] = useState([]);

  const dataPoints = widget_info.data_points.split(",");
  const G = G2.getEngine("canvas");
  const config = {
    data,
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
    angleField: "value",
    colorField: "data_point",
    appendPadding: 10,
    radius: 0.75,
    legend: false,
    label: {
      type: "spider",
      labelHeight: 40,
      formatter: (data, mappingData) => {
        const group = new G.Group({});
        group.addShape({
          type: "circle",
          attrs: {
            x: 0,
            y: 0,
            width: 40,
            height: 50,
            r: 5,
            fill: mappingData.color,
          },
        });
        group.addShape({
          type: "text",
          attrs: {
            x: 10,
            y: 8,
            text: `${data.data_point}`,
            fill: mappingData.color,
          },
        });
        group.addShape({
          type: "text",
          attrs: {
            x: 0,
            y: 25,
            color: "white",
            // text: `${data.value} (${(data.percent * 100).toFixed(0)}%)`,
            text: `${(data.percent * 100).toFixed(0)}%`,
            fill: "rgba(100, 100, 100, 1.0)",
            fontWeight: 700,
          },
        });
        return group;
      },
    },
    interactions: [
      {
        type: "element-selected",
      },
      {
        type: "element-active",
      },
    ],
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

  useEffect(() => {
  }, [data]);

  const setChartData = () => {
    let chartData = [];
    let goodParts = 0;
    let badParts = 0;
    let oee = 0;
    let availability = 0;
    let quality = 0;
    let performance = 0;
    let total = 0;

    for (let i = 0; i < shiftList.length; i++) {
      const shift = shiftList[i];
      goodParts += parseInt(shift["goodParts"]);
      badParts += parseInt(shift["badParts"]);
      oee += parseInt(shift["oee"]);
      availability += parseInt(shift["availability"]);
      quality += parseInt(shift["quality"]);
      performance += parseInt(shift["performance"]);
    }

    for (let i = 0; i < cwDataPoints.length; i++) {
      if (dataPoints[i] === "1") {
        const dataPoint = cwDataPoints[i];
        let value = 0;
        switch (i) {
          case 0:
            value = goodParts;
            break;
          case 1:
            value = badParts;
            break;
          case 2:
            value = oee;
            break;
          case 3:
            value = availability;
            break;
          case 4:
            value = quality;
            break;
          case 5:
            value = performance;
            break;
          default:
            break;
        }
        chartData.push({
          data_point: dataPoint,
          value: ~~(value),
        });
      }
    }
    setData(chartData);
  };

  useEffect(() => {}, [shiftList]);

  return (
    <Spin spinning={isLoading}>
      <Pie
        {...config}
        style={{
          paddingTop: 20,
          paddingBottom: 0,
          paddingLeft: 10,
          paddingRight: 30,
          textAlign: "center",
          height: 280,
        }}
      />
    </Spin>
  );
};

export default CWPieChart;
