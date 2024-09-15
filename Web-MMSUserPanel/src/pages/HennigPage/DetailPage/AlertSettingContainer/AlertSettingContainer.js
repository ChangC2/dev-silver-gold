import { Button, Col, Row } from "antd";
import { useState } from "react";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import HennigUpdateModal from "../../HennigUpdateModal/HennigUpdateModal";
import "./AlertSettingContainer.css";
function AlertSettingContainer(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { hennigInfo } = props;
  const [isShowUpdateModal, setIsShowUpdateModal] = useState(false);
  return (
    <div>
      {isShowUpdateModal && (
        <HennigUpdateModal
          hennigInfo={hennigInfo}
          setIsShowUpdateModal={setIsShowUpdateModal}
          isShowUpdateModal={isShowUpdateModal}
        />
      )}

      <Row align="center">
        <Col className="hennig-alert-setting-container">
          <div className="hennig-alert-setting-limit">
            <span className="hennig-alert-setting-title">
              {lang(langIndex, "iiot_alertsetting")}
              {"   "}
            </span>
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-limit-title">
              {lang(langIndex, "iiot_minlimit")}
            </span>
            <span className="hennig-alert-setting-backspace">:</span>
            {hennigInfo.min_limit},
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-limit-title">
              {lang(langIndex, "iiot_maxlimit")}
            </span>
            <span className="hennig-alert-setting-backspace">:</span>
            {hennigInfo.max_limit},
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-limit-title">
              {lang(langIndex, "iiot_alertto")}
            </span>
            <span className="hennig-alert-setting-backspace">:</span>
            {hennigInfo.alert_emails}
            <span className="hennig-alert-setting-backspace" />
            <span className="hennig-alert-setting-backspace" />
            <Button
              className="hennig-alert-setting-update-button"
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
