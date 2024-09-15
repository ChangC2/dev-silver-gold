// @flow strict

import { SettingOutlined } from "@ant-design/icons";
import { Button, message, Modal, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { UpdateTrelloSetting } from "services/common/auth_apis";
import { trelloColors } from "services/common/constants";
import lang from "../../../services/lang";
import "./TrelloSettings.css";
import {
  apiCallForGetCards,
  apiCallForUpdateCard,
  apiCallForUpdateLabel,
} from "services/common/trello_apis";

const { Option } = Select;

function TrelloSettings(props) {
  const dispatch = useDispatch();
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal } = props;
  const [isBusy, setIsBusy] = useState(false);
  const [color, setColor] = useState("green_dark");
  const [utilization, setUtilization] = useState("");

  const authData = useSelector((x) => x.authService);

  const onUpdateTrello = () => {
    setIsBusy(true);
    apiCallForUpdateCard(utilization, (res) => {
      if (res == true) {
        apiCallForUpdateLabel(color, (res) => {
          setIsBusy(false);
          if (res == true) {
            message.success("Success to save");
          } else {
            message.error("Fail to save");
          }
        });
      } else {
        message.error("Fail to save");
      }
    });
  };

  useEffect(() => {
    if (showModal) {
      apiCallForGetCards((res) => {
        if (res.length > 0) {
          setUtilization(res[0].name);
          let labels = res[0].labels;
          if (labels.length > 0) {
            setColor(labels[0].color);
          }
        }
      });
    }
  }, [showModal]);

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
        {"Trello Settings"}
      </div>

      <div style={{ overflow: "auto" }}>
        <Spin spinning={isBusy}>
          <div className="trello-setting-container">
            <div className="customer-setting-container-style">
              <div>
                <div className="app-setting-text-input-title">{"Color"}</div>
                <Select
                  className="trello-select"
                  dropdownClassName="trello-select-dropdown"
                  value={color}
                  onChange={(value) => {
                    setColor(value);
                  }}
                >
                  {trelloColors.map((color) => (
                    <Option className="trello-select-value" key={color}>
                      {color}
                    </Option>
                  ))}
                </Select>
              </div>

              <div>
                <div
                  className="app-setting-text-input-title"
                  style={{ marginTop: 20 }}
                >
                  {"Utilization"}
                </div>
                <input
                  style={{ marginTop: 5 }}
                  className={"app-setting-text-input-value"}
                  value={utilization}
                  onChange={(e) => setUtilization(e.target.value)}
                  type={"text"}
                />
              </div>

              <Button
                type="primary"
                className="trello-update-button"
                onClick={onUpdateTrello}
              >
                Save
              </Button>
            </div>
          </div>
        </Spin>
      </div>
    </Modal>
  );
}

export default TrelloSettings;
