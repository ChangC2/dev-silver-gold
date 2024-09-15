import { Select } from "antd";
import React from "react";
import { SENSOR_TYPE } from "../../../services/common/constants";
import "./sensorTypeSelector.css";
const { Option } = Select;
function SensorTypeSelector(props) {
  const { optionList, initValue, updateValue, title, field, valueType } = props;

  const optionUIList =
    optionList == undefined || optionList.length == 0
      ? null
      : optionList.map((info, index) => {
          return (
            <Option
              key={`sensor_type_selector_key_${index}`}
              value={info.value}
              className="sensor-type-selector-value"
            >
              {info.title}
            </Option>
          );
        });
  return (
    <div className="sensor-type-selector-container">
      <div className="sensor-type-selector-title">{title}</div>
      <Select
        onChange={(v) => updateValue(field, v)}
        value={
          initValue[field] == undefined
            ? ""
            : valueType == "number"
            ? parseInt(initValue[field])
            : initValue[field]
        }
        className="sensor-type-selector"
        dropdownClassName="sensor-type-selector-dropdown"
      >
        {optionUIList}
      </Select>
    </div>
  );
}

export default SensorTypeSelector;
