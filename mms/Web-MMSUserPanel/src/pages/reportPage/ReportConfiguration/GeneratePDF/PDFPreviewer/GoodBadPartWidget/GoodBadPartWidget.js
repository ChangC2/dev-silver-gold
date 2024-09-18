// @flow strict

import { Table } from "antd";
import React from "react";
import "./GoodBadPartWidget.css";
function GoodBadPartWidget(props) {
  const { oeeData } = props;
  let totalGoodParts = 0;
  let totalBadParts = 0;
  for (var i = 0; i < oeeData.length; i++) {
    totalGoodParts += parseInt(oeeData[i]["goodParts"]);
    totalBadParts += parseInt(oeeData[i]["badParts"]);
  }
  const columns = [
    {
      title: "Job ID",
      dataIndex: "machine_id",
      key: "machine_id",
    },
    {
      title: "Good Parts",
      dataIndex: "goodParts",
      key: "goodParts",
    },
    {
      title: "Bad Parts",
      dataIndex: "badParts",
      key: "badParts",
    },
  ];

  return (
    <div>
      <div style={{ textAlign: "center", fontSize: 16 }}>
        <span style={{ color: "green" }}>
          Total Good Parts: {totalGoodParts}&nbsp;&nbsp;:&nbsp;&nbsp;
        </span>
        <span style={{ color: "#FF1B00" }}>
          Total Bad Parts: {totalBadParts}
        </span>
      </div>
      <div>
        <Table
          dataSource={oeeData.map((x, index) => ({ ...x, key: index }))}
          columns={columns}
        />
      </div>
    </div>
  );
}

export default GoodBadPartWidget;
