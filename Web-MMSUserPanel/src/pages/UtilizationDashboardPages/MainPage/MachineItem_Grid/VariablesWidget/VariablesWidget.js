// @flow strict

import React from "react";
import "./VariablesWidget.css";
import GaugeChart from "react-gauge-chart";
import { Gauge } from "@ant-design/plots";
import lang from "../../../../../../src/services/lang";
import { useState } from "react";
import { useEffect } from "react";
import {
  readVariableViewIndex,
  writeVariableViewIndex,
} from "../../../../../../src/services/common/functions";

const variables = {
  utilization: "24hr Utilization",
  oee: "OEE",
  availability: "Availability",
  quality: "Quality",
  performance: "Performance",
};

function VariablesWidget(props) {
  const variableKeyList = Object.keys(variables);
  const { machineInfo, langIndex, isMobile, customer_id } = props;
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    setCurrentIndex(
      readVariableViewIndex(customer_id, machineInfo["machine_id"])
    );
  }, []);


  const config = {
    id: "gauge_" + machineInfo["id"],
    percent: parseFloat(machineInfo[variableKeyList[currentIndex]]) / 100.0,
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
  return (
    <div
      onClick={() => {
        const changeIndex = (currentIndex + 1) % variableKeyList.length;
        setCurrentIndex(changeIndex);
        writeVariableViewIndex(
          customer_id,
          machineInfo["machine_id"],
          changeIndex
        );
      }}
    >
      <Gauge {...config} className="machine-grid-gauge-container" />
      {isMobile == true ? (
        <h5 style={{ color: "white", textAlign: "center" }}>
          {lang(langIndex, "cnc_utilization")}
        </h5>
      ) : (
        <h4 style={{ color: "white", textAlign: "center" }}>
          {variables[variableKeyList[currentIndex]]} :{" "}
          {Math.round(machineInfo[variableKeyList[currentIndex]])}%
        </h4>
      )}
    </div>
  );
}

export default VariablesWidget;
