import React, { useState, useEffect } from "react";
import "./UserManageDialog.css";
import { Modal, Input, Row, Col, Select } from "antd";
import ImageUploader from "./ImageUploader/ImageUploader";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import { BASE_URL } from "services/common/urls";

const { Option } = Select;
function UserManageDialog(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    initUserInfo,
    isShowModal,
    setIsShowModal,
    addUser,
    editUser,
  } = props;
  const [userInfo, setUserInfo] = useState({
    security_level: "0",
    user_picture: BASE_URL + "images/photo/blank.jpg",
  });
  useEffect(() => {
    if (initUserInfo != null) {
      setUserInfo({ ...initUserInfo, password:"" });
    }
  }, []);
  const onClickOk = () => {
    if (initUserInfo == null) {
      addUser(userInfo);
    } else {
      editUser(userInfo);
    }
  };
  const onClickCancel = () => {
    setIsShowModal(false);
  };
  const onSelectSecurityLevel = (value) => {
    setUserInfo({ ...userInfo, security_level: value });
  };
  const updateImage = (url) => {
    setUserInfo({ ...userInfo, user_picture: url });
  };

  return (
    <div>
      <Modal
        title={initUserInfo == null ? "User Add Dialog" : "User Edit Dialog"}
        visible={isShowModal}
        onOk={onClickOk}
        onCancel={onClickCancel}
        destroyOnClose={true}
        className="user-setting-modal-style"
      >
        <div style={{ textAlign: "center" }}>
          <ImageUploader
            user_picture={userInfo.user_picture}
            updateImage={updateImage}
          />
        </div>
        <div>
          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_userid")}</span>
            </Col>
            <Col span={16}>
              <Input
                type={"number"}
                disabled={initUserInfo != null}
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={userInfo.barcode == undefined ? "" : userInfo.barcode}
                onChange={(e) =>
                  setUserInfo({ ...userInfo, barcode: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_username")}</span>
            </Col>
            <Col span={16}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={userInfo.username == undefined ? "" : userInfo.username}
                onChange={(e) =>
                  setUserInfo({ ...userInfo, username: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_password")}</span>
            </Col>
            <Col span={16}>
              <Input.Password
                className="user-password-input-style"
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={userInfo.password == undefined ? "" : userInfo.password}
                onChange={(e) =>
                  setUserInfo({ ...userInfo, password: e.target.value })
                }
              />
              {initUserInfo !== null && (
                <span className="user-password-desc">
                  *Leave the password field empty if you don't want to change.
                </span>
              )}
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_fullusername")}</span>
            </Col>
            <Col span={16}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                value={
                  userInfo.username_full == undefined
                    ? ""
                    : userInfo.username_full
                }
                onChange={(e) =>
                  setUserInfo({ ...userInfo, username_full: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <span>{lang(langIndex, "plant_securitylevel")}</span>
            </Col>
            <Col span={16}>
              <Select
                value={userInfo.security_level}
                className="page-changer-style"
                dropdownClassName="page-changer-style-dropdown"
                onChange={onSelectSecurityLevel}
                style={{ width: 150 }}
              >
                <Option className="page-changer-item" value="4">
                  {lang(langIndex, "plant_administrator")}
                </Option>
                <Option className="page-changer-item" value="2">
                  {lang(langIndex, "plant_client")}
                </Option>
                <Option className="page-changer-item" value="0">
                  {lang(langIndex, "plant_operator")}
                </Option>
              </Select>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default UserManageDialog;
