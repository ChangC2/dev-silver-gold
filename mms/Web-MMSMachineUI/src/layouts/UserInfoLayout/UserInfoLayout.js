import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setUserDataStore } from "redux/actions/userActions";
import accountIcon from "../../assets/icons/ic_account.png";
import logoutIcon from "../../assets/icons/logout_w.png";

import { setAppDataStore } from "redux/actions/appActions";
import {
  appData,
  isValid,
  setAppData,
  setUserData,
  userData,
} from "services/global";
import AlertDlg from "../AlertDlg/AlertDlg";
import LoginIDModal from "../LoginIDModal/LoginIDModal";
import "./UserInfoLayout.css";

const UserInfoLayout = (props) => {
  const dispatch = useDispatch();
  const { userDataStore } = useSelector((x) => x.userDataStore);
  const { id, username_full, user_picture } = userDataStore;
  const [showLoginID, setShowLoginID] = useState(false);
  const [showLogout, setShowLogout] = useState(false);

  const onLogout = () => {
    setUserData({
      id: "",
      username: "",
      password: "",
      username_full: "UnAttended",
      user_picture: "",
      security_level: "",
      customer_id: "",
    });
    dispatch(setUserDataStore(userData));
    setAppData({});
    dispatch(setAppDataStore(appData));
  };

  return (
    <div align="middle" className="user-info-layout">
      <LoginIDModal showModal={showLoginID} setShowModal={setShowLoginID} />
      <AlertDlg
        title={"Are you sure want to logout?"}
        showModal={showLogout}
        setShowModal={setShowLogout}
        onOK={onLogout}
      />
      <div className="user-info-inner">
        <>
          {isValid(id) ? (
            <img className="user-profile-image" src={user_picture} alt="" />
          ) : (
            <img className="user-avatar-icon" src={accountIcon} alt="" />
          )}

          {isValid(id) ? (
            <div className="txt-top-name">{username_full}</div>
          ) : (
            <div
              className="txt-top-name"
              style={{ cursor: "pointer" }}
              onClick={() => {
                setShowLoginID(true);
              }}
            >
              Login
            </div>
          )}
          {isValid(id) && (
            <img
              className="top-button"
              src={logoutIcon}
              onClick={() => setShowLogout(true)}
            />
          )}
        </>
      </div>
    </div>
  );
};

export default UserInfoLayout;
