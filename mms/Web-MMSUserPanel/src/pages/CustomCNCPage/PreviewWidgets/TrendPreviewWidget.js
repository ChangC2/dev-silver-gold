import { Line } from "@ant-design/charts";
import { Spin } from "antd";
import { useEffect, useState } from "react";
import "./TrendPreviewWidget.css";

const TrendPreviewWidget = (props) => {
  const { chartData, tankRef, daysoftemp } = props;

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
    // scrollbar: {
    //   type: "horizontal",
    // },
  };

  useEffect(() => {
    setData(chartData);
  }, [chartData]);

  return (
    <div>
      <div className="trend-chart-preview-title">
        Trend ( {daysoftemp} Days)
      </div>
      {chartData == undefined ? (
        <div style={{ textAlign: "center", paddingTop: 20 }}>
          <Spin />
        </div>
      ) : (
        <div ref={tankRef} style={{ background: "white", padding: 10 }}>
          <Line
            {...config}
            style={{
              paddingTop: 20,
              paddingBottom: 0,
              paddingLeft: 50,
              paddingRight: 50,
              textAlign: "center",
              height: 400,
            }}
          />
        </div>
      )}
    </div>
  );
};
export default TrendPreviewWidget;
