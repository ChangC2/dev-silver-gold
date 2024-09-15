import { Select } from "antd";
import React from "react";
import { HENNIG_TYPE } from "../../../services/common/constants";
import "./HennigTypeSelector.css";
const { Option } = Select;
function HennigTypeSelector(props) {
  const { optionList, initValue, updateValue, title, field, valueType } = props;

  const optionUIList =
    optionList == undefined || optionList.length == 0
      ? null
      : optionList.map((info, index) => {
          return (
            <Option
              key={`hennig_type_selector_key_${index}`}
              value={info.value}
              className="hennig-type-selector-value"
            >
              {info.title}
            </Option>
          );
        });
  return (
    <div className="hennig-type-selector-container">
      <div className="hennig-type-selector-title">{title}</div>
      <Select
        onChange={(v) => updateValue(field, v)}
        value={
          initValue[field] == undefined
            ? ""
            : valueType == "number"
            ? parseInt(initValue[field])
            : initValue[field]
        }
        className="hennig-type-selector"
        dropdownClassName="hennig-type-selector-dropdown"
      >
        {optionUIList}
      </Select>
    </div>
  );
}

export default HennigTypeSelector;
