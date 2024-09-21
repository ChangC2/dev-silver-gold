// @flow strict

import { useEffect, useState } from "react";
import { RingProgress } from "@ant-design/plots";
import {
  readVariableViewIndex,
  writeVariableViewIndex,
} from "../../../../../services/common/functions";
import "./VariablesWidgetTile.css";
import { Col, Row } from "antd";

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

  const config = {
    height: 85,
    width: 85,
    autoFit: false,
    percent: parseFloat(machineInfo["utilization"]) / 100.0,
    color: ["#fdc200", "#E8EDF3"],
    innerRadius: 0.85,
  };

  return <RingProgress {...config} className="machine-grid-ring-container" />;
}

export default VariablesWidgetTile;
