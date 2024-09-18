import { Input } from "antd";
import React from "react";
import ReactInputMask from "react-input-mask";

import "./TextInput.css";
function TextInput(props) {
  const {
    initValue,
    field,
    updateValue,
    title,
    input_type,
    isCenter,
    disabled,
  } = props;
  return (
    <div className="app-setting-text-input-container">
      <div className="app-setting-text-input-title">{title}</div>
      {input_type === "time-setting" ? (
        <ReactInputMask
          className={
            isCenter == true
              ? "app-setting-text-input-value centered-text"
              : "app-setting-text-input-value"
          }
          value={initValue[field]}
          onChange={(e) => updateValue(field, e.target.value)}
          mask="99:99:99"
        />
      ) : (
        <input
          className={
            isCenter == true
              ? "app-setting-text-input-value centered-text"
              : "app-setting-text-input-value"
          }
          value={
            initValue[field] == undefined
              ? input_type == "number"
                ? 0
                : ""
              : initValue[field]
          }
          onChange={(e) => updateValue(field, e.target.value)}
          type={input_type == undefined ? "string" : input_type}
          disabled={disabled ? "disabled" : ""}
        />
      )}
    </div>
  );
}

export default TextInput;
