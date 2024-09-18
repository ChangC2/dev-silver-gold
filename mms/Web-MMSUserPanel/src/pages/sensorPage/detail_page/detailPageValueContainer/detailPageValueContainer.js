import { Col, Row } from "antd";
import React from "react";
// import './detailPageValueContainer.css'
function DetailPageValueContainer(props) {
  const { title, valueList, unit, color } = props;
  return (
    <Row
      style={{
        fontSize: 17,
        color: color === undefined ? "white" : color,
        textAlign: "center",
      }}
    >
      <Col flex={"170px"} style={{ textAlign: "left", marginLeft: 15 }}>
        {title} :{" "}
      </Col>
      <Col flex={"auto"}>
        <Row>
          <Col xs={24} sm={8}>
            {valueList.length === 0
              ? "-"
              : valueList[valueList.length - 1] + " " + unit}
          </Col>
          <Col xs={24} sm={8}>
            {valueList.length === 0 ? "-" : Math.max(...valueList) + " " + unit}
          </Col>
          <Col xs={24} sm={8}>
            {valueList.length === 0 ? "-" : Math.min(...valueList) + " " + unit}
          </Col>
        </Row>
      </Col>
    </Row>
  );
}

export default DetailPageValueContainer;
