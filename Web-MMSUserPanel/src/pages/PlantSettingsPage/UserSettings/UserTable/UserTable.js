import React from "react";
import "./UserTable.css";
import { Table, Popconfirm } from "antd";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import avarIcon from "../../../../assets/icons/ic_avatar.png";

const dateFormat = "YYYY-MM-DD HH:mm:ss";
function UserTable(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { userList, deleteUser, onClickEditRecord } = props;
  const columns = [
    {
      title: lang(langIndex, "plant_userid"),
      dataIndex: "barcode",
      key: "barcode",
    },
    {
      title: lang(langIndex, "plant_username"),
      dataIndex: "username",
      key: "username",
    },
    {
      title: lang(langIndex, "plant_avatar"),
      dataIndex: "user_picture",
      key: "user_picture",
      render: (user_picture) => {
        return user_picture == "" || user_picture == null ? (
          <img className="user-picture-style" src={avarIcon} />
        ) : (
          <img className="user-picture-style" src={user_picture} />
        );
      },
    },
    {
      title: lang(langIndex, "plant_securitylevel"),
      dataIndex: "security_level",
      key: "security_level",
      render: (security_level) => {
        switch (security_level) {
          case "4":
            return lang(langIndex, "plant_administrator");
          case "2":
            return lang(langIndex, "plant_client");
          case "0":
            return lang(langIndex, "plant_operator");
          default:
            return lang(langIndex, "plant_operator");
        }
      },
    },
    {
      title: lang(langIndex, "jobentry_operation"),
      dataIndex: "operation",
      render: (_, record) => {
        return (
          <div>
            <a onClick={() => editRecord(record)}>
              {lang(langIndex, "jobentry_edit")}
            </a>
            <Popconfirm
              title={lang(langIndex, "jobentry_suretodelete")}
              onConfirm={() => deleteRecord(record)}
            >
              <a style={{ marginLeft: 10, color: "red" }}>
                {lang(langIndex, "jobentry_delete")}
              </a>
            </Popconfirm>
          </div>
        );
      },
    },
  ];
  const editRecord = (record) => {
    onClickEditRecord(record);
  };
  const deleteRecord = (record) => {
    deleteUser(record.id);
  };

  return (
    <div>
      <Table
        className="user-table-style"
        dataSource={userList}
        columns={columns}
        pagination={{ pageSize: 5 }}
      />
    </div>
  );
}

export default UserTable;
