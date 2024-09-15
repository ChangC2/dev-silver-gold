import { Col, Row } from "antd";
import React from "react";
// import './detailPageValueContainer.css'
function DetailPageValueContainer(props) {
  const { title, value, min, max, unit, color } = props;
  return (
    <Row
      style={{
        fontSize: 17,
        color: color === undefined ? "white" : color,
        textAlign: "center",
      }}
    >
      <Col flex={"210px"} style={{ textAlign: "left", marginLeft: 15 }}>
        {title} :{" "}
      </Col>
      <Col flex={"auto"}>
        <Row>
          <Col xs={24} sm={8}>
            {value + " " + unit}
          </Col>
          <Col xs={24} sm={8}>
            {max + " " + unit}
          </Col>
          <Col xs={24} sm={8}>
            {min + " " + unit}
          </Col>
        </Row>
      </Col>
    </Row>
  );
}

export default DetailPageValueContainer;
