import { Col, Row } from "antd";
import "./UsageWidget.css";
import { useState } from "react";
import TextArea from "antd/lib/input/TextArea";

const UsageWidget = (props) => {
  const { title, before, setBefore, after, setAfter, used, setUsed, unit } = props;
  
  return (
    <div className="usage-layout">
      <div className="usage-title">{title}</div>
      <Row align={"middle"} style={{ height: "80px" }}>
        <Col span={8} className="usage-input">
          {"Before"}
          <div>
            <div className="usage-value">
              <input
                className="text-input"
                value={before}
                type="number"
                onChange={(e) => setBefore(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </div>
            <div className="usage-unit">{unit}</div>
          </div>
        </Col>
        <Col span={8} className="usage-input">
          {"After"}
          <div>
            <div className="usage-value">
              <input
                className="text-input"
                value={after}
                type="number"
                onChange={(e) => setAfter(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </div>
            <div className="usage-unit">{unit}</div>
          </div>
        </Col>
        <Col span={8} className="usage-input">
          {"Used"}
          <div>
            <div className="usage-value">
              <input
                className="text-input"
                value={used}
                type="number"
                onChange={(e) => setUsed(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </div>
            <div className="usage-unit">{unit}</div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default UsageWidget;
