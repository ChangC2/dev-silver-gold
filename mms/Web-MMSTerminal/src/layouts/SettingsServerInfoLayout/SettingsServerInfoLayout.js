import { Col, Row, message } from "antd";
import RadialInput from "components/RadialInput/RadialInput";
import TextInput from "components/TextInput/TextInput";
import { useState } from "react";
import "./SettingsServerInfoLayout.css";

const SettingsServerInfoLayout = (props) => {
  const [useCustomServer, setUseCustomServer] = useState(false);
  const [customServerInfo, setCustomServerInfo] = useState("192.168.1.36");

  return (
    <div className="settings-server-info-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-server-info-title">Server Information</span>
        </Col>
      </Row>
      <Row className="settings-server-info-content" align={"middle"}>
        <Col style={{ width: "300px" }}>
          <RadialInput
            value={useCustomServer}
            title="User Custom Server"
            setValue={setUseCustomServer}
          />
        </Col>
        {useCustomServer ? (
          <Col style={{ width: "300px" }}>
            <TextInput
              value={customServerInfo}
              title="Custom Server Info(domain or IP)"
              setValue={setCustomServerInfo}
            />
          </Col>
        ) : (
          <Col></Col>
        )}
      </Row>
    </div>
  );
};

export default SettingsServerInfoLayout;
