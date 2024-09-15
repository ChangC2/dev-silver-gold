import { Col, Radio, Row } from "antd";
import React from "react";
import "./OptionInput.css";
function OptionInput(props) {
  const { initValue, field, updateValue, title, span } = props;
  return (
    <div className="app-setting-option-input-container">
      <Radio.Group
        // onChange={updateValue}
        value={initValue[field]}
        className="app-setting-option"
        onChange={(e) => {
          updateValue(field, e.target.value);
        }}
      >
        <Row>
          <Col className="app-setting-option-item" span={span}>
            <Radio value={"0"}>{title[0]}</Radio>
          </Col>
          <Col className="app-setting-option-item" span={span}>
            <Radio value={"1"}>{title[1]}</Radio>
          </Col>
          {title.length > 2 && (
            <Col className="app-setting-option-item" span={span}>
              <Radio value={"2"}>{title[2]}</Radio>
            </Col>
          )}
        </Row>
      </Radio.Group>
    </div>
  );
}

export default OptionInput;
