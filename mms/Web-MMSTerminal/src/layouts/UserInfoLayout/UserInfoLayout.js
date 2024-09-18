import { Col, Row, message } from "antd";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setUserDataStore } from "redux/actions/userActions";
import { LS_ITEMS } from "services/CONSTANTS";
import accountIcon from "../../assets/icons/ic_account.png";
import logoutIcon from "../../assets/icons/ic_logout_app.png";
import qrCodeIcon from "../../assets/icons/ic_qrcode_scan.png";
import LoginIDModal from "../LoginIDModal/LoginIDModal";
import LogoutModal from "../LogoutModal/LogoutModal";
import "./UserInfoLayout.css";
import { setUserData, userData } from "services/global";

const UserInfoLayout = (props) => {
  const dispatch = useDispatch();
  const reduceUserData = useSelector((x) => x.userDataStore);
  const { userDataStore, test } = useSelector((x) => x.userDataStore);
  const [showLoginID, setShowLoginID] = useState(false);
  const [showLogout, setShowLogout] = useState(false);
  const [showInputMode, setShowInputMode] = useState(false);

  const guestUser = {
    id: "",
    username: "",
    password: "",
    username_full: "UnAttended",
    user_picture: "",
    security_level: "",
    customer_id: "",
  };

  const onQrCode = () => {
    setShowInputMode(true);
  };

  const onLogout = () => {
    setUserData(guestUser);
    dispatch(setUserDataStore(userData));
    message.info("Logout Clicked");
  };

  return (
    <Row align="middle" className="user-info-layout">
      <InputModeModal
        title={"Please Select Login Mode"}
        showModal={showInputMode}
        setShowModal={setShowInputMode}
        setShowInput={setShowLoginID}
      />
      <LoginIDModal showModal={showLoginID} setShowModal={setShowLoginID} />
      <LogoutModal
        showModal={showLogout}
        setShowModal={setShowLogout}
        onLogout={onLogout}
      />
      <Col span={4}>
        <img
          className="user-info-qrcode"
          src={qrCodeIcon}
          onClick={() => onQrCode()}
          alt="qrcode"
        />
      </Col>
      <Col span={16} className="user-info">
        {userDataStore === undefined ||
        userDataStore.user_picture === "" ||
        userDataStore.user_picture === undefined ||
        userDataStore.user_picture === null ? (
          <img
            className="user-info-no-photo"
            src={accountIcon}
            alt="user_picture"
          />
        ) : (
          <img
            className="user-info-photo"
            src={userDataStore.user_picture}
            alt="user_picture"
          />
        )}
        {userDataStore === undefined ||
        userDataStore.username_full === "" ||
        userDataStore.username_full === undefined ||
        userDataStore.username_full === null
          ? "Unattended"
          : userDataStore.username_full}
      </Col>
      <Col span={4} style={{ textAlign: "right" }}>
        <img
          className="user-info-logout"
          src={logoutIcon}
          onClick={() => setShowLogout(true)}
          alt="logout"
        />
      </Col>
    </Row>
  );
};

export default UserInfoLayout;
