import { Col, Row, message } from "antd";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  isValid,
  userData
} from "services/global";
import editIcon from "../../assets/icons/ic_edit.png";
import MachineIDModal from "../MachineIDModal/MachineIDModal";
import "./MachineInfoLayout.css";

const MachineInfoLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { machine_id, machine_picture_url } = appDataStore;
  const [showMachineID, setShowMachineID] = useState(false);

  const [appSetting, setAppSetting] = useState({ ...appDataStore });

  const onClickEditMachineID = () => {
    if (!isValid(userData.id)) {
      message.error("Please login with User Id");
      return;
    }
    setShowMachineID(true);
  };

  return (
    <Row align="middle" className="machine-info-layout">
      <MachineIDModal
        showModal={showMachineID}
        setShowModal={setShowMachineID}
        setAppSetting={setAppSetting}
        appSetting={appSetting}
      />
      {isValid(machine_id) ? (
        <Col span={16} className="machine-info">
          <img
            className="machine-info-photo"
            src={
              machine_picture_url.includes("http")
                ? machine_picture_url
                : "https://slymms.com/backend/images/machine/" +
                  machine_picture_url
            }
            alt="user_picture"
          />
          {machine_id}
        </Col>
      ) : (
        <Col span={16} className="machine-info">
          {"Machine ID: UnAttended"}
        </Col>
      )}
      <Col span={8} style={{ textAlign: "right" }}>
        <img
          className="machine-info-edit"
          src={editIcon}
          onClick={() => onClickEditMachineID()}
          alt="edit"
        />
      </Col>
    </Row>
  );
};

export default MachineInfoLayout;
