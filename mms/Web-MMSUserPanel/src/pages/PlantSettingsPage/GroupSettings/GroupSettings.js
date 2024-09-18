// @flow strict

import { Button, message, Modal, Spin } from "antd";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { ManageMachineGroup } from "../../../services/common/cnc_apis";
import lang from "../../../services/lang";
import { UpdateCustomerGroupInfo } from "../../../services/common/cnc_apis";
import GroupManageDialog from "./GroupManageDialog/GroupManageDialog";
import "./GroupSettings.css";
import GroupTableWidget from "./GroupTableWidget/GroupTableWidget";
import { PrinterOutlined, SettingOutlined } from "@ant-design/icons";

function GroupSettings(props) {
  const dispatch = useDispatch();
  const { langIndex } = useSelector((x) => x.app);
  const [isShowModal, setIsShowModal] = useState(false);
  const [initGroupInfo, setInitGroupInfo] = useState(null);
  const [isBusy, setIsBusy] = useState(false);
  const cncService = useSelector((x) => x.cncService);
  const { showModal, setShowModal, selCustomerId, machineList } = props;

  // sync with redux
  const customerInfoList = cncService.customerInfoList;

  const onClickAddGroup = () => {
    setInitGroupInfo(null);
    setIsShowModal(true);
  };

  const addGroup = (groupInfo) => {
    setIsBusy(true);
    ManageMachineGroup(selCustomerId, groupInfo, (res) => {
      setIsBusy(false);
      if (res == null) {
        message.error(lang(langIndex, "msg_something_wrong"));
        return;
      }
      UpdateCustomerGroupInfo(selCustomerId, res, dispatch);
    });

    // UpdateCustomerGroupInfo
  };
  const editGroup = (groupInfo) => {
    setIsBusy(true);
    ManageMachineGroup(selCustomerId, groupInfo, (res) => {
      setIsBusy(false);
      if (res == null) {
        message.error(lang(langIndex, "msg_something_wrong"));
        return;
      }
      UpdateCustomerGroupInfo(selCustomerId, res, dispatch);
    });
  };

  const onClickEditGroup = (groupInfo) => {
    setInitGroupInfo(groupInfo);
    setIsShowModal(true);
  };

  const onClickDeleteGroup = (groupInfo) => {
    ManageMachineGroup(
      selCustomerId,
      { id: groupInfo.id, is_delete: true },
      (res) => {}
    );
    const machineGroupList =
      cncService.customerInfoList[selCustomerId]["groupInfo"];
    UpdateCustomerGroupInfo(
      selCustomerId,
      machineGroupList.filter((x) => x.id != groupInfo.id),
      dispatch
    );
  };

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
        {lang(langIndex, "plant_groupsetting")}
      </div>

      <div
        className="content-scrollbar-style user-setting-container"
        style={{ overflow: "auto" }}
      >
        {isShowModal === true && (
          <GroupManageDialog
            machineList={machineList}
            initGroupInfo={initGroupInfo}
            addGroup={addGroup}
            editGroup={editGroup}
            isShowModal={isShowModal}
            setIsShowModal={setIsShowModal}
          />
        )}

        <div className="user-setting-button-container">
          <Button
            ghost
            onClick={() => {
              onClickAddGroup();
            }}
          >
            {lang(langIndex, "plant_addgroup")} +{" "}
          </Button>
        </div>
        <Spin spinning={isBusy}>
          <div>
            <div className="user-setting-user-table-style">
              <GroupTableWidget
                langIndex={langIndex}
                machineList={machineList}
                machineGroupList={
                  cncService.customerInfoList[selCustomerId]["groupInfo"]
                }
                customerInfoList={customerInfoList}
                onClickEditGroup={onClickEditGroup}
                onClickDeleteGroup={onClickDeleteGroup}
              />
            </div>
          </div>
        </Spin>
      </div>
    </Modal>
  );
}

export default GroupSettings;
