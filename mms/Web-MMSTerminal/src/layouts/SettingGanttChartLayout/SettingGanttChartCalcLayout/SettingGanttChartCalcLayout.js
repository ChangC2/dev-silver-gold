import { Select } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./SettingGanttChartCalcLayout.css";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import OptionInput from "components/OptionInput/OptionInput";
import { gdtFomulars } from "services/CONSTANTS"

const { Option } = Select;

function SettingGanttChartCalcLayout(props) {
  const { appSetting, setAppSetting } = props;
  
  const setGDTSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div>
      <TextInputGroup
        initValue={appSetting}
        field="calc_chart_title"
        updateValue={setGDTSettings}
        title="Title"
        input_type="text"
      />
      <div className="settings-gantt-chart-calc-option-container">
        <div>Formula</div>
        <Select
          style={{ width: "100%" }}
          popupClassName="settings-gantt-chart-calc-dropdown"
          value={appSetting["calc_chart_formula"]}
          onChange={(e) => setGDTSettings("calc_chart_formula", e)}
        >
          {gdtFomulars.map((x) => {
            return (
              <Option
                className="settings-gantt-chart-calc-dropdown-item"
                key={`formula-${x["id"]}`}
                value={x["id"]}
              >
                {x["name"]}
              </Option>
            );
          })}
        </Select>
      </div>

      <div className="settings-gantt-chart-calc-option-container">
        <div>Parameter</div>
        <Select
          style={{ width: "100%" }}
          className="page-changer-style"
          popupClassName="settings-gantt-chart-calc-dropdown"
          value={appSetting["calc_chart_option"]}
          onChange={(e) => setGDTSettings("calc_chart_option", e)}
        >
          <Option
            className="settings-gantt-chart-calc-dropdown-item"
            key={`option-0`}
            value="0"
          >
            None
          </Option>

          <Option
            className="settings-gantt-chart-calc-dropdown-item"
            key={`option-1`}
            value="1"
          >
            Current Operator
          </Option>

          <Option
            className="settings-gantt-chart-calc-dropdown-item"
            key={`option-2`}
            value="2"
          >
            Current Job ID
          </Option>
        </Select>
      </div>

      <TextInputGroup
        initValue={appSetting}
        field="calc_chart_unit"
        updateValue={setGDTSettings}
        title="Display Units"
        input_type="text"
      />

      <div className="settings-gantt-chart-calc-option-container">
        <OptionInput
          span={12}
          initValue={appSetting}
          field="calc_chart_disp_mode"
          updateValue={setGDTSettings}
          title={["Show Unit", "Show %"]}
        />
      </div>

      <TextInputGroup
        initValue={appSetting}
        field="calc_chart_max_value"
        updateValue={setGDTSettings}
        title="Daily Target"
        input_type="number"
      />
    </div>
  );
}

export default SettingGanttChartCalcLayout;
