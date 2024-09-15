import { Col, Row } from "antd";
import TextInput from "components/TextInput/TextInput";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import deviceBluetoothIcon from "../../assets/icons/ic_device_bluetooth.png";
import deviceSelectIcon from "../../assets/icons/ic_device_select.png";
import machinenNameIcon from "../../assets/icons/ic_machine_name.png";
import AccountIDModal from "./AccountIDModal/AccountIDModal";
import "./SettingMachineInfoLayout.css";

const SettingMachineInfoLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  const { factoryDataStore } = useSelector((x) => x.factoryDataStore);
  const [showAccountID, setShowAccountID] = useState(false);
  const [deviceAddress, setDeviceAddress] = useState("79:56:CE:25:30:55");

  const { accountId } = factoryDataStore;

  const onClickBlueTooth = () => {};

  const onClickAccountId = () => {
    setShowAccountID(true);
  };

  const setMachineName = (value) => {
    var newSetting = { ...appSetting, machineName: value };
    setAppSetting(newSetting);
  };

  return (
    <div className="settings-machine-info-layout">
      <AccountIDModal
        showModal={showAccountID}
        setShowModal={setShowAccountID}
      />
      <Row align="middle">
        <Col span={24}>
          <span className="settings-machine-info-title">
            Machine Information
          </span>
        </Col>
      </Row>

      <Row className="settings-machine-info-content" align={"middle"}>
        <Col span={12}>
          <div>
            <TextInput
              title={"Machine Name"}
              value={appSetting.machineName}
              setValue={setMachineName}
            />
            <img
              src={machinenNameIcon}
              className="app-setting-machine-info-icon"
              alt="machine-name"
            />
          </div>
        </Col>

        {/* This is not used */}
        <Col span={8} style={{ paddingLeft: "20px", display:"none" }}>
          <div onClick={onClickBlueTooth}>
            <TextInput
              title={"Device Address"}
              value={deviceAddress}
              setValue={setDeviceAddress}
              disabled={true}
            />
            <img
              src={deviceBluetoothIcon}
              className="app-setting-machine-info-icon"
              alt="bluetooth"
            />
          </div>
        </Col>

        <Col span={12} style={{ paddingLeft: "20px" }}>
          <div onClick={onClickAccountId}>
            <TextInput title={"Account ID"} value={accountId} disabled={true} />
            <img
              src={deviceSelectIcon}
              className="app-setting-machine-info-icon"
              alt="machine-setting"
            />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default SettingMachineInfoLayout;
