import React, { useEffect, useState } from "react";
import "./MachineHstGraphPreviewWidget.css";
import { Spin } from "antd";
import { Column } from "@ant-design/charts";

const MachineHstGraphPreviewWidget = (props) => {
  const { chartData, throughPutRef, daysofhst } = props;

  const [data, setData] = useState([]);

  useEffect(() => {
    setData(chartData);
  }, [chartData]);

  const config = {
    data,
    // theme: theme,
    xField: "date",
    yField: "value",
    seriesField: "type",
    isGroup: true,
    colorField: "type", // or seriesField in some cases
    color: ["#2ca02c", "#d62728"],
    columnStyle: {
      radius: [0, 0, 0, 0],
    },
    slider: {
      start: 0.0,
      end: 1,
      textStyle: {
        fill: "white",
      },
    },
    legend: {
      offsetY: -8,
    },
    // scrollbar: {
    //   type: 'horizontal',
    // },
  };

  return (
    <div>
      <div className="machine-hst-preview-title">
        Trailing {daysofhst} Days Throughput
      </div>
      {chartData == undefined ? (
        <div style={{ paddingTop: 30, textAlign: "center" }}>
          <Spin />
        </div>
      ) : (
        <div ref={throughPutRef} style={{ background: "white", padding: 10 }}>
          <Column
            {...config}
            style={{
              paddingTop: 20,
              paddingBottom: 20,
              paddingLeft: 50,
              paddingRight: 50,
              textAlign: "center",
              height: 350,
            }}
          />
        </div>
      )}
    </div>
  );
};
export default MachineHstGraphPreviewWidget;
