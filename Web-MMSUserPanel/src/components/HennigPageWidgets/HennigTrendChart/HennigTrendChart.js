import React, { useEffect, useState } from "react";
import { Line } from "@ant-design/plots";
import "./HennigTrendChart.css";
function HennigTrendChart(props) {
  const { hennigInfo, valueList, chartColor, yAxisLabel, yAxisUnit } = props;
  const [data, setData] = useState([]);
  const config = {
    data,
    xField: "time",
    yField: "value",
    seriesField: "hennig",
    yAxis: {
      line: { style: { lineWidth: 1, color: "red" } },
      grid: { line: { style: { lineWidth: 0 } } },
      label: {
        formatter: (v) => `${v} ${yAxisUnit}`,
      },
    },
    xAxis: { line: { style: { lineWidth: 1 } } },
    // legend: {
    //   position: "top",
    // },
    legend: false,
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
    color: chartColor,
    // scrollbar: {
    //   type: "horizontal",
    // },
  };

  useEffect(() => {
    let chartData = [];
    valueList.map((item) => {
      chartData.push({
        time: item[0],
        hennig: yAxisLabel,
        value: item[1],
      });
    });
    setData(chartData);
  }, [valueList]);

  return (
    <div className="hennig-trend-chart-container">
      <div className="hennig-trend-chart-content">
        {data == undefined && <div>No valid data</div>}
        <div className="henig-detail-page-trend-title">{yAxisLabel}</div>
        {data != undefined && (
          <Line
            {...config}
            style={{
              paddingTop: 20,
              paddingBottom: 20,
              paddingLeft: 20,
              paddingRight: 20,
              textAlign: "center",
              height: 350,
            }}
          />
        )}
      </div>
    </div>
  );
}

export default HennigTrendChart;
