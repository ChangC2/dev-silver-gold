// @flow strict

import { RingProgress } from "@ant-design/plots";
import { Progress } from "antd";
import { useEffect, useState } from "react";
import { readVariableViewIndex } from "../../../../../services/common/functions";
import "./VariablesWidgetTile.css";

const variables = {
  utilization: "24hr Utilization",
  oee: "OEE",
  availability: "Availability",
  quality: "Quality",
  performance: "Performance",
};

function VariablesWidgetTile(props) {
  const variableKeyList = Object.keys(variables);
  const { machineInfo, langIndex, isMobile, customer_id } = props;
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    setCurrentIndex(
      readVariableViewIndex(customer_id, machineInfo["machine_id"])
    );
  }, []);
  let percent = parseInt(Math.round(parseFloat(machineInfo["utilization"])));

  console.log(percent);

  const config = {
    height: 85,
    width: 85,
    autoFit: false,
    percent: percent,
    color: ["#fdc200", "#E8EDF3"],
    innerRadius: 0.85,
    statistic: {
      title: {
        style: {
          color: "#fff",
          fontSize: 26,
          fontStyle: "bold",
        },
      },
      content: {
        style: {
          text: `${percent * 100}%`,
          color: "#fff",
          fontSize: 26,
          fontStyle: "bold",
        },
      },
    },
  };

  return (
    <Progress
      type="circle"
      percent={percent}
      width={80}
      className="machine-grid-ring-container"
    />
  );
}

export default VariablesWidgetTile;
