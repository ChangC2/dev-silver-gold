import { Col, Radio, Row, message } from "antd";
import "./SettingProcessMonitorLayout.css";

const SettingProcessMonitorLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const processMonitorTypes = [
    { value: 0, title: "None" },
    { value: 1, title: "Time Logger" },
    { value: 2, title: "Cleaning Station" },
    { value: 3, title: "Blast Station" },
    { value: 4, title: "Paint Station" },
    { value: 5, title: "Assembly Station 137" },
    { value: 6, title: "Assembly Station 136" },
    { value: 7, title: "Assembly Station 3" },
    { value: 8, title: "Quality" },
  ];

  const setProcessType = (value) => {
    var newSetting = { ...appSetting, process: value };
    setAppSetting(newSetting);
  };

  const processMonitorTypeOptions = processMonitorTypes.map((pmType) => {
    return (
      <Col
        className="app-setting-process-monitor-option-item"
        span={6}
        key={processMonitorTypes.indexOf(pmType)}
      >
        <Radio value={pmType.value} style={{ color: "white" }}>
          {pmType.title}
        </Radio>
      </Col>
    );
  });

  const onEditJobID = () => {
    message.info("onEditJobID Clicked");
  };

  return (
    <div className="settings-process-monitor-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-process-monitor-title">
            Choose Type For Process Monitor
          </span>
        </Col>
      </Row>

      <Radio.Group
        // onChange={updateValue}
        value={appSetting.process}
        className="app-setting-process-monitor-option"
        onChange={(e) => {
          setProcessType(e.target.value);
        }}
      >
        <Row>{processMonitorTypeOptions}</Row>
      </Radio.Group>
    </div>
  );
};

export default SettingProcessMonitorLayout;
