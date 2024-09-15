import { Select } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import OptionInput from "../../../../../src/components/cncPageComponents/OptionInput/OptionInput";
import TextInput from "../../../../../src/components/cncPageComponents/TextInput/TextInput";
import Urls, { postRequest } from "../../../../../src/services/common/urls";
import "./AppSettingModal.css";

const { Option } = Select;

function AppSettingCalc(props) {
  const { appSetting, setAppSettings } = props;

  const [formulas, setFormulas] = useState([]);


  useEffect(() => {
    var url = Urls.GET_APP_SETTING_FORMULA;
    var param = {
    };
    postRequest(url, param, (res) => {
      if (res.status === true) {
        setFormulas(res.data);
      }
    });
  }, []);

  return (
    <div>
      <TextInput
        initValue={appSetting}
        field="calc_chart_title"
        updateValue={setAppSettings}
        title="Title"
        input_type="text"
      />
      <div className="app-setting-text-input-container">
        <div className="app-setting-text-input-title">Formula</div>

        <Select
          style={{ width: "100%" }}
          className="page-changer-style"
          dropdownClassName="page-changer-style-dropdown"
          value={appSetting["calc_chart_formula"]}
          onChange={(e) => setAppSettings("calc_chart_formula", e)}
        >
          {formulas.map((x) => {
            return (
              <Option
                className="page-changer-item"
                key={`formula-${x["id"]}`}
                value={x["id"]}
              >
                {x["name"]}
              </Option>
            );
          })}
        </Select>
      </div>

      <div className="app-setting-text-input-container">
        <div className="app-setting-text-input-title">Parameter</div>
        <Select
          style={{ width: "100%" }}
          className="page-changer-style"
          dropdownClassName="page-changer-style-dropdown"
          value={appSetting["calc_chart_option"]}
          onChange={(e) => setAppSettings("calc_chart_option", e)}
        >
          <Option className="page-changer-item" key={`option-0`} value="0">
            None
          </Option>

          <Option className="page-changer-item" key={`option-1`} value="1">
            Current Operator
          </Option>

          <Option className="page-changer-item" key={`option-2`} value="2">
            Current Job ID
          </Option>
        </Select>
      </div>

      <TextInput
        initValue={appSetting}
        field="calc_chart_unit"
        updateValue={setAppSettings}
        title="Display Units"
        input_type="text"
      />
      <OptionInput
        span={12}
        initValue={appSetting}
        field="calc_chart_disp_mode"
        updateValue={setAppSettings}
        title={["Show Unit", "Show %"]}
      />
      <TextInput
        initValue={appSetting}
        field="calc_chart_max_value"
        updateValue={setAppSettings}
        title="Daily Target"
        input_type="number"
      />
    </div>
  );
}

export default AppSettingCalc;
