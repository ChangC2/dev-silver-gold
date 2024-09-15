import React from "react";
import "./RadialInput.css";
import { Switch } from "antd";
function RadialInput(props) {
  const { value, setValue, title } = props;
  return (
    <div className="radial-input-container">
      <Switch
        className="radial-input"
        checked={value === 1 ? true : false}
        onChange={(e) => setValue(e === true ? 1 : 0)}
      />
      <div className="radial-input-title">{title}</div>
    </div>
  );
}

export default RadialInput;
