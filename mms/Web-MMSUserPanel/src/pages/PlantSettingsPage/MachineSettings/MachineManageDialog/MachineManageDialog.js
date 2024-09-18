import React, { useState, useEffect } from "react";
import "./MachineManageDialog.css";
import { Modal, Row, Col, Input, Select, Button } from "antd";
import MachineImageUploader from "./MachineImageUploader/MachineImageUploader";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
const { Option } = Select;

function MachineManageDialog(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    initMachineInfo,
    isShowModal,
    setIsShowModal,
    addMachine,
    editMachine,
  } = props;
  const [machineType, setMachineType] = useState(0);
  const [machineInfo, setMachineInfo] = useState({
    machine_picture_url: "blank machine.png",
  });
  useEffect(() => {
    if (initMachineInfo != null) {
      setMachineInfo({ ...initMachineInfo });
    }
  }, []);

  const onClickOk = () => {
    if (initMachineInfo == null) {
      let newMachineInfo = { ...machineInfo, machine_type: machineType };
      addMachine(newMachineInfo);
    } else {
      editMachine(machineInfo);
    }
  };
  const onClickCancel = () => {
    setIsShowModal(false);
  };
  const updateImage = (url) => {
    setMachineInfo({ ...machineInfo, machine_picture_url: url });
  };

  const onClickKeyGenerate = () => {
    const characters =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let deviceID = "";
    const charactersLength = characters.length;

    for (let i = 0; i < 10; i++) {
      const randomIndex = Math.floor(Math.random() * charactersLength);
      deviceID += characters.charAt(randomIndex);
    }
    let newMachineInfo = { ...machineInfo, device_id: deviceID };
    setMachineInfo(newMachineInfo);
  };

  return (
    <div>
      <Modal
        title={
          initMachineInfo == null
            ? lang(langIndex, "plant_machineadddialog")
            : lang(langIndex, "plant_machineeditdialog")
        }
        visible={isShowModal}
        onOk={onClickOk}
        onCancel={onClickCancel}
        destroyOnClose={true}
        className="machine-setting-modal-style"
      >
        <div style={{ textAlign: "center" }}>
          <MachineImageUploader
            machine_picture={machineInfo.machine_picture_url}
            updateImage={updateImage}
          />
        </div>
        <div>
          <Row style={{ marginTop: 20 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_machineid")}</span>
            </Col>
            <Col span={16}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={
                  machineInfo.machine_id == undefined
                    ? ""
                    : machineInfo.machine_id
                }
                onChange={(e) =>
                  setMachineInfo({ ...machineInfo, machine_id: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 20 }}>
            <Col span={8}>
              <span>{"Device ID"}</span>
            </Col>
            <Col span={10}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={
                  machineInfo.device_id == undefined
                    ? ""
                    : machineInfo.device_id
                }
                onChange={(e) =>
                  setMachineInfo({
                    ...machineInfo,
                    device_id: e.target.value,
                  })
                }
              />
            </Col>
            <Col span={6}>
              <Button
                style={{ width: "100%" }}
                ghost
                onClick={() => {
                  onClickKeyGenerate();
                }}
              >
                {"Generate"}
              </Button>
            </Col>
          </Row>
          {initMachineInfo == null && (
            <Row style={{ marginTop: 10 }}>
              <Col span={8}>
                <span>{"Data Source"}</span>
              </Col>
              <Col span={16}>
                <Select
                  onChange={(value) => {
                    setMachineType(value);
                  }}
                  defaultValue={0}
                  value={machineType}
                  className="labor-selector"
                  dropdownClassName="labor-selector-dropdown"
                >
                  <Option
                    key={`machine_type_0`}
                    value={0}
                    className="labor-selector-value"
                  >
                    {"Android"}
                  </Option>
                  <Option
                    key={`machine_type_1`}
                    value={1}
                    className="labor-selector-value"
                  >
                    {"ESP32"}
                  </Option>
                </Select>
              </Col>
            </Row>
          )}
        </div>
      </Modal>
    </div>
  );
}

export default MachineManageDialog;
