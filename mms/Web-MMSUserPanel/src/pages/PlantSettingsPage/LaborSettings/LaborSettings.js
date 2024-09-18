// @flow strict

import { SettingOutlined } from "@ant-design/icons";
import { Button, message, Modal, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { UpdateLaborSetting } from "services/common/auth_apis";
import { laborTimeFormats } from "services/common/constants";
import lang from "../../../services/lang";
import LaborCategories from "./LaborCategories";
import "./LaborSettings.css";

const { Option } = Select;

function LaborSettings(props) {
  const dispatch = useDispatch();
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal } = props;
  const [isBusy, setIsBusy] = useState(false);
  const [timeFormat, setTimeFormat] = useState("24hr");

  const authData = useSelector((x) => x.authService);

  const onUpdateLabor = () => {
    setIsBusy(true);
    UpdateLaborSetting(
      authData["user_id"],
      timeFormat,
      0,
      "",
      dispatch,
      (res) => {
        setIsBusy(false);
        if (res == true) {
          message.success("Success to save.");
        } else {
          message.error("Fail to save");
        }
      }
    );
  };

  useEffect(() => {
    setTimeFormat(authData["time_format"]);
  }, []);

  const onCancel = () => {
    setShowModal(false);
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
        {lang(langIndex, "plant_laborsetting")}
      </div>

      <div style={{ overflow: "auto" }}>
        <Spin spinning={isBusy}>
          <div className="labor-setting-container">
            <div className="customer-setting-container-style">
              <span className="labor-select-title">Time Format</span>
              <Select
                className="labor-select"
                dropdownClassName="labor-select-dropdown"
                value={timeFormat}
                onChange={(value) => {
                  setTimeFormat(value);
                }}
              >
                {laborTimeFormats.map((format) => (
                  <Option className="labor-select-value" key={format}>
                    {format}
                  </Option>
                ))}
              </Select>

              <Button
                ghost
                className="labor-update-button"
                onClick={onUpdateLabor}
              >
                Save
              </Button>
            </div>

            <div className="customer-setting-container-style">
              <LaborCategories user_id={authData["user_id"]} />
            </div>
          </div>
        </Spin>
      </div>
    </Modal>
  );
}

export default LaborSettings;
