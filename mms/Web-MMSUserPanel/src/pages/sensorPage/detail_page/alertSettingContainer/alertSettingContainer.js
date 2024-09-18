import { Button, Col, Row } from "antd";
import { useState } from "react";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import SensorUpdateModal from "../../sensorUpdateModal/sensorUpdateModal";
import "./alertSettingContainer.css";
function AlertSettingContainer(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { sensorInfo } = props;
  const [isShowUpdateModal, setIsShowUpdateModal] = useState(false);
  return (
    <div>
      {isShowUpdateModal && (
        <SensorUpdateModal
          sensorInfo={sensorInfo}
          setIsShowUpdateModal={setIsShowUpdateModal}
          isShowUpdateModal={isShowUpdateModal}
        />
      )}

      <Row align="center">
        <Col className="sensor-alert-setting-container">
          <div className="sensor-alert-setting-limit">
            <span className="sensor-alert-setting-title">
              {lang(langIndex, "iiot_alertsetting")}
              {"   "}
            </span>
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-limit-title">
              {lang(langIndex, "iiot_minlimit")}
            </span>
            <span className="sensor-alert-setting-backspace">:</span>
            {sensorInfo.min_limit},
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-limit-title">
              {lang(langIndex, "iiot_maxlimit")}
            </span>
            <span className="sensor-alert-setting-backspace">:</span>
            {sensorInfo.max_limit},
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-limit-title">
              {lang(langIndex, "iiot_alertto")}
            </span>
            <span className="sensor-alert-setting-backspace">:</span>
            {sensorInfo.alert_emails}
            <span className="sensor-alert-setting-backspace" />
            <span className="sensor-alert-setting-backspace" />
            <Button
              className="sensor-alert-setting-update-button"
              onClick={() => setIsShowUpdateModal(true)}
              ghost
            >
              {lang(langIndex, "iiot_updatesetting")}
            </Button>
          </div>
        </Col>
      </Row>
    </div>
  );
}

export default AlertSettingContainer;
