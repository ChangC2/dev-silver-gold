import { PrinterOutlined, SettingOutlined } from "@ant-design/icons";
import { Button, Modal, Spin, message } from "antd";
import { useEffect, useState } from "react";
import "./UserSettings.css";
import UserTable from "./UserTable/UserTable";

import { useSelector } from "react-redux";
import {
  AddUser,
  DeleteUser,
  ExecuteQuery,
  GetUserList,
  UpdateUser,
  getUsersReport,
} from "../../../services/common/cnc_apis";
import lang from "../../../services/lang";
import { ShowPDFPreviewModal } from "./ShowPDFPreviewModal";
import UserManageDialog from "./UserManageDialog/UserManageDialog";
const dateFormat = "YYYY-MM-DD HH:mm:ss";
function UserSettings(props) {
  const { langIndex } = useSelector((state) => state.app);
  const { showModal, setShowModal, selCustomerId, customerInfo } = props;
  const [isShowModal, setIsShowModal] = useState(false);
  const [showSpin, setShowSpin] = useState(false);
  const [userList, setUserList] = useState([]);
  const [initUserInfo, setInitUserInfo] = useState(null);
  useEffect(() => {
    GetUserList(selCustomerId, (userList) => {
      if (userList != null || userList.length > 0) {
        userList.forEach(function (entry) {
          entry.key = entry.id;
        });
        setUserList(userList);
      } else {
      }
    });
  }, [selCustomerId]);

  const deleteUser = (id) => {
    DeleteUser({ customer_id: selCustomerId, id: id }, (res) => {
      if (res.status == true) {
        const newData = userList.filter((element) => element.id != id);
        setUserList(newData);
        message.success(lang(langIndex, "msg_delete_success"));
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  const onClickEditRecord = (record) => {
    setInitUserInfo(record);
    setIsShowModal(true);
  };
  const onClickAddUser = () => {
    setInitUserInfo(null);
    setIsShowModal(true);
  };
  const addUser = (info) => {
    info["customer_id"] = selCustomerId;
    AddUser(info, (res) => {
      if (res.status == true) {
        setUserList([...userList, res.data]);
        message.success(lang(langIndex, "msg_add_success"));
        setIsShowModal(false);
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  const editUser = (info) => {
    info["customer_id"] = selCustomerId;
    const newData = [...userList];
    const index = newData.findIndex((item) => item.key === info.key);
    if (index > -1) {
      const item = newData[index];
      newData.splice(index, 1, { ...item, ...info });
      UpdateUser(info, (res) => {
        if (res.status == true) {
          setUserList(newData);
          message.success(lang(langIndex, "msg_update_success"));
          setIsShowModal(false);
        } else {
          message.error(lang(langIndex, "msg_something_wrong"));
        }
      });
    } else {
      message.error(lang(langIndex, "msg_something_wrong"));
    }
  };

  const onCancel = () => {
    setShowModal(false);
  };

  const onClickCreatePDF = async () => {
    if (userList.length == 0 || customerInfo == null) return;
    const result = await ShowPDFPreviewModal({
      userList: userList,
      customerInfo: customerInfo,
    });

    if (result["userData"] != null && result["userData"] != undefined) {
      setShowSpin(true);
      getUsersReport(result["userData"], (res) => {
        setShowSpin(false);
        if (res == null) {
          return;
        }
        window.open(res);
      });
    }
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
        {lang(langIndex, "plant_usersettings")}
      </div>

      <div
        className="content-scrollbar-style user-setting-container"
        style={{ overflow: "auto" }}
      >
        {isShowModal === true && (
          <UserManageDialog
            initUserInfo={initUserInfo}
            addUser={addUser}
            editUser={editUser}
            isShowModal={isShowModal}
            setIsShowModal={setIsShowModal}
          />
        )}
        <div className="user-setting-button-container">
          <Button
            ghost
            style={{ width: 100 }}
            onClick={() => {
              onClickCreatePDF();
            }}
          >
            <PrinterOutlined />
            Print{" "}
            {showSpin && <Spin style={{ marginLeft: 10, marginRight: 10 }} />}
          </Button>
          <span className="backspace" />
          <Button
            ghost
            onClick={() => {
              onClickAddUser();
            }}
          >
            Add New User +{" "}
          </Button>
        </div>
        <div className="user-setting-user-table-style">
          <UserTable
            userList={userList}
            deleteUser={deleteUser}
            onClickEditRecord={onClickEditRecord}
          />
        </div>
      </div>
    </Modal>
  );
}

export default UserSettings;
