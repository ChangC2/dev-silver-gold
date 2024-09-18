import { Col, Row, message } from "antd";
import TextInput from "components/TextInput/TextInput";
import { useState } from "react";
import "./SettingsAppInfoLayout.css";

const SettingsAppInfoLayout = (props) => {
  const [appVersion, setAppVersion] = useState("V8.75");

  const onEditJobID = () => {
    message.info("onEditJobID Clicked");
  };

  return (
    <div className="settings-app-info-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-app-info-title">App Information</span>
        </Col>
      </Row>
      <Row className="settings-app-info-content" align={"middle"}>
        <Col flex="200px">
          <TextInput
            value={appVersion}
            title="Current Version"
            disabled={true}
          />
        </Col>
        <Col flex="auto">
          <span className="settings-app-version-desc">
            Your version is up to date.
          </span>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsAppInfoLayout;
