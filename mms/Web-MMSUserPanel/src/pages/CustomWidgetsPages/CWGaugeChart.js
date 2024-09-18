import { Gauge } from "@ant-design/charts";
import { Spin, Tooltip } from "antd";
import { useEffect, useState } from "react";
import lang from "../../services/lang";

import { cwDataPoints } from "services/common/constants";
import { getShiftList } from "services/common/cnc_apis";
import "./CWGaugeChart.css";

const CWGaugeChart = (props) => {
  const { widget_info, darkmode, customerInfo } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [shiftList, setShiftList] = useState([]);

  const [data, setData] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

  const dataPoints = widget_info.data_points.split(",");

  const config = {
    id: "gauge_" + widget_info.id,
    percent:
      data.length > currentIndex
        ? parseFloat(data[currentIndex].value) / 100.0
        : 0,
    // type: "meter",
    // innerRadius: 0.8,
    // startAngle: (-6 / 6) * Math.PI,
    // endAngle: (0 / 6) * Math.PI,
    axis: {
      label: {
        formatter(v) {
          return Number(v) * 100;
        },
      },
      subTickLine: {
        count: 3,
      },
    },
    range: {
      ticks: [0, 100],
      color: ["l(0) 0:#F4664A 0.5:#FAAD14 1:#30BF78"],
    },
    indicator: {
      pointer: {
        style: {
          stroke: "#D0D0D0",
        },
      },
      pin: {
        style: {
          stroke: "#D0D0D0",
        },
      },
    },
    statistic: {
      content: {
        offsetY: 0,
        style: {
          fontSize: "14px",
          color: "#fff",
        },
        formatter: ({ percent }) =>
          // `${variables[variableKeyList[currentIndex]]}: ${(
          //   percent * 100
          // ).toFixed(0)}%`,
          "",
      },
    },
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

    total = 0;
    for (let i = 0; i < cwDataPoints.length; i++) {
      if (dataPoints[i] === "1") {
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
        total += value;
      }
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
          value: (value / total) * 100,
        });
      }
    }
    setData(chartData);
  };

  return (
    <Spin spinning={isLoading}>
      <div
        onClick={() => {
          const changeIndex = (currentIndex + 1) % data.length;
          setCurrentIndex(changeIndex);
        }}
        style={{
          paddingTop: 40,
          paddingLeft: 10,
          paddingRight: 30,
        }}
      >
        <Tooltip title="Click to check other variables">
          <Gauge
            {...config}
            style={{
              paddingBottom: 10,
              textAlign: "center",
              height: 200,
            }}
          />
          <h3 style={{ color:  darkmode ? "black" : "white", textAlign: "center" }}>
            {data.length > currentIndex ? data[currentIndex].data_point : ""}
            <span className="backspace" />:<span className="backspace" />
            {data.length > currentIndex
              ? parseInt(data[currentIndex].value)
              : 0}
            %
          </h3>
        </Tooltip>
      </div>
    </Spin>
  );
};

export default CWGaugeChart;
