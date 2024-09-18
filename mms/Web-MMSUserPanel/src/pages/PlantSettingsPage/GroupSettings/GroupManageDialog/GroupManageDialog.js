// @flow strict

import { Col, Input, message, Modal, Row } from "antd";
import React from "react";
import { useSelector } from "react-redux";
import "./GroupManageDialog.css";
import lang from "../../../../services/lang";
import { useEffect } from "react";
import { useState } from "react";
import { CheckOutlined } from "@ant-design/icons";
import { MACHINE_IMAGE_BASE_URL } from "../../../../services/common/urls";
function GroupManageDialog(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    machineList,
    initGroupInfo,
    addGroup,
    editGroup,
    isShowModal,
    setIsShowModal,
  } = props;
  const [groupInfo, setGroupInfo] = useState({ name: "", machine_list: [] });

  useEffect(() => {
    if (initGroupInfo != null) {
      setGroupInfo({
        ...initGroupInfo,
        machine_list: initGroupInfo.machine_list.split(","),
      });
    }
  }, [initGroupInfo]);

  const onClickOk = () => {
    if (groupInfo.name == "") {
      message.error(lang(langIndex, "plant_groupdialogerror"));
      return;
    }
    if (initGroupInfo == null) {
      addGroup({
        name: groupInfo.name,
        machine_list: groupInfo.machine_list.join(),
      });
    } else {
      editGroup({
        id: groupInfo.id,
        name: groupInfo.name,
        machine_list: groupInfo.machine_list.join(),
      });
    }
    setIsShowModal(false);
  };
  
  const onClickCancel = () => {
    setIsShowModal(false);
  };

  const machineListUI = machineList.map((machine, index) => {
    return (
      <div
        key={`group-dialog-machine-${index}`}
        className="group-dlg-machine-item"
        onClick={() => {
          if (groupInfo.machine_list.includes(machine.machine_id)) {
            setGroupInfo({
              ...groupInfo,
              machine_list: groupInfo.machine_list.filter(
                (x) => x != machine.machine_id
              ),
            });
          } else {
            setGroupInfo({
              ...groupInfo,
              machine_list: [...groupInfo.machine_list, machine.machine_id],
            });
          }
        }}
      >
        <Row gutter={16} align={"middle"}>
          <Col flex={"50px"}>
            <img
              src={MACHINE_IMAGE_BASE_URL + machine.machine_picture_url}
              style={{ maxWidth: 50 }}
            />
          </Col>
          <Col flex={"auto"}>{machine.machine_id}</Col>
          <Col flex={"30px"}>
            {groupInfo.machine_list.includes(machine.machine_id) && (
              <CheckOutlined style={{ color: "green" }} />
            )}
          </Col>
        </Row>
      </div>
    );
  });

  return (
    <div>
      <Modal
        title={
          initGroupInfo == null
            ? lang(langIndex, "plant_groupadddialog")
            : lang(langIndex, "plant_groupeditdialog")
        }
        visible={isShowModal}
        onOk={onClickOk}
        onCancel={onClickCancel}
        destroyOnClose={true}
        className="group-setting-modal-style"
      >
        <div style={{ marginBottom: 10 }}>
          Name:
          <Input
            placeholder={"Type the group name"}
            value={groupInfo.name}
            onChange={(e) =>
              setGroupInfo({ ...groupInfo, name: e.target.value })
            }
          />
        </div>
        {machineListUI}
      </Modal>
    </div>
  );
}

export default GroupManageDialog;
