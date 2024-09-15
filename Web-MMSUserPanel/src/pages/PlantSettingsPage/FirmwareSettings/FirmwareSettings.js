// @flow strict

import { SettingOutlined, UploadOutlined } from "@ant-design/icons";
import { Button, Col, message, Modal, Row, Select, Spin, Upload } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getFirmwares, updateFirmware } from "services/common/cnc_apis";
import Urls, { postRequest } from "../../../services/common/urls";
import lang from "../../../services/lang";
import "./FirmwareSettings.css";

const { Option } = Select;

function FirmwareSettings(props) {
  const dispatch = useDispatch();
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal } = props;
  const [isBusy, setIsBusy] = useState(false);
  const [mmsVersion, setMmsVersion] = useState(1.0);
  const [iotVersion, setIotVersion] = useState(1.0);

  const onUpdatefirmware = () => {
    setIsBusy(true);
    updateFirmware(mmsVersion, iotVersion, (res) => {
      setIsBusy(false);
      if (res == true) {
        message.success("Success to save.");
      } else {
        message.error("Fail to save");
      }
    });
  };

  useEffect(() => {
    if (!showModal) return;
    setIsBusy(true);
    getFirmwares((res) => {
      setIsBusy(false);
      if (res != null && res["status"] == true) {
        setMmsVersion(res["data"]["mms_version"]);
        setIotVersion(res["data"]["iot_version"]);
      }
    });
  }, [showModal]);

  const onCancel = () => {
    setShowModal(false);
  };

  const uploadMmsFirmware = {
    name: "file",
    action: Urls.UPLOAD_FIRMWARE,
    beforeUpload: (file) => {
      var formData = new FormData();
      formData.append("file", file);
      formData.append("filename", "mms");
      setIsBusy(true);
      postRequest(Urls.UPLOAD_FIRMWARE, formData, (response) => {
        setIsBusy(false);
        //window.location.reload(false);
      });
      return false;
    },
    onChange: (info) => {},
  };

  const uploadIotFirmware = {
    name: "file",
    action: Urls.UPLOAD_FIRMWARE,
    beforeUpload: (file) => {
      var formData = new FormData();
      formData.append("file", file);
      formData.append("filename", "iot");
      setIsBusy(true);
      postRequest(Urls.UPLOAD_FIRMWARE, formData, (response) => {
        setIsBusy(false);
        //window.location.reload(false);
      });
      return false;
    },
    onChange: (info) => {},
  };

  return (
    <Modal
      centered
      visible={showModal}
      title={null}
      onCancel={() => onCancel()}
      onOk={() => onCancel()}
      okText={lang(langIndex, "plant_update")}
      closable={true}
      className="customer-setting-dialog-style"
      destroyOnClose={true}
      footer={null}
      width={700}
    >
      <div className="customer-setting-page-title">
        <SettingOutlined />
        <span style={{ marginLeft: 10 }} />
        {lang(langIndex, "plant_firmwaresetting")}
      </div>

      <div style={{ overflow: "auto" }}>
        <Spin spinning={isBusy}>
          <div className="firmware-setting-container">
            <div className="firmware-setting-container-style">
              <Row style={{ marginTop: 20 }}>
                <Col flex={"100px"} className="standard-firmware-title">
                  MMS :
                </Col>
                <Col flex={"60px"} className="firmware-version-title">
                  Version
                </Col>
                <Col flex={"auto"}>
                  <input
                    className={"app-setting-text-input-value"}
                    value={mmsVersion}
                    onChange={(e) => setMmsVersion(e.target.value)}
                    type="number"
                    step="0.1"
                  />
                </Col>
                <Col flex={"auto"}>
                  <div className="firmware-upload-button">
                    <Upload
                      {...uploadMmsFirmware}
                      style={{
                        marginLeft: 16,
                        display: "inline",
                      }}
                    >
                      <UploadOutlined
                        className="firmware-upload-icon"
                        size={50}
                      />
                      <span className="apk-uploader-title">Upload</span>
                    </Upload>
                  </div>
                </Col>
              </Row>

              <Row style={{ marginTop: 20 }}>
                <Col flex={"100px"} className="standard-firmware-title">
                  IOT :
                </Col>
                <Col flex={"60px"} className="firmware-version-title">
                  Version
                </Col>
                <Col flex={"auto"}>
                  <input
                    className={"app-setting-text-input-value"}
                    value={iotVersion}
                    onChange={(e) => setIotVersion(e.target.value)}
                    type="number"
                    step="0.1"
                  />
                </Col>
                <Col flex={"auto"}>
                  <div className="firmware-upload-button">
                    <Upload
                      {...uploadIotFirmware}
                      style={{
                        marginLeft: 16,
                        display: "inline",
                      }}
                    >
                      <UploadOutlined
                        className="firmware-upload-icon"
                        size={50}
                      />
                      <span className="apk-uploader-title">Upload</span>
                    </Upload>
                  </div>
                </Col>
              </Row>

              <div style={{ textAlign: "center" }}>
                <Button
                  ghost
                  className="firmware-update-button"
                  onClick={onUpdatefirmware}
                >
                  Save
                </Button>
              </div>
            </div>
          </div>
        </Spin>
      </div>
    </Modal>
  );
}

export default FirmwareSettings;
