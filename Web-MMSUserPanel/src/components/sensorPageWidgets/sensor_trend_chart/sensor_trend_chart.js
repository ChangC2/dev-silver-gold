import React, { useEffect, useState } from "react";
import { Line } from "@ant-design/plots";
import "./sensor_trend_chart.css";
function SensorTrendChart(props) {

  const { valueList, chartColor, screenSize } = props;
  const [data, setData] = useState([]);

  const config = {
    data,
    xField: "time",
    yField: "value",
    seriesField: "sensor",
    // yAxis: {
    //   label: {
    //     formatter: (v) => `${(v / 10e8).toFixed(1)} B`,
    //   },
    // },
    legend: {
      position: "top",
    },
    smooth: true,
    // @TODO 后续会换一种动画方式
    // animation: {
    //   appear: {
    //     animation: "path-in",
    //     duration: 5000,
    //   },
    // },
    animation: false,
    slider: {
      start: 0.0,
      end: 1,
      textStyle: {
        fill: "white",
      },
    },
    color: ["#da1c1c", "#1093f1", "#0fa30f"],
    // scrollbar: {
    //   type: "horizontal",
    // },
  };


  useEffect(() => {
    let chartData1 = [];
    valueList.map((item) => {
      chartData1.push({
        time: item[0],
        sensor: "Data1",
        value: item[1],
      });
      chartData1.push({
        time: item[0],
        sensor: "Data2",
        value: item[2],
      });
      chartData1.push({
        time: item[0],
        sensor: "Data3",
        value: item[3],
      });
    });
    setData(chartData1);
  }, [valueList]);

  return (
    <div
      className="sensor-trend-chart-container"
    >
      {data == undefined && <div>No valid data</div>}

      {data != undefined && (
        <Line
          {...config}
          style={{
            paddingTop: 20,
            paddingBottom: 20,
            paddingLeft: 50,
            paddingRight: 50,
            textAlign: "center",
            height: 400,
          }}
        />
      )}
    </div>
  );
}

export default SensorTrendChart;
