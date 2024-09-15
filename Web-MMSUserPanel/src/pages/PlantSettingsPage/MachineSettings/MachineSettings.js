import { Button, Modal, message, Spin } from "antd";
import { useState } from "react";
import { useSelector } from "react-redux";
import Urls, { postRequest } from "../../../services/common/urls";
import { AddMachine, ExecuteQuery } from "../../../services/common/cnc_apis";
import { SettingOutlined } from "@ant-design/icons";
import lang from "../../../services/lang";
import MachineManageDialog from "./MachineManageDialog/MachineManageDialog";
import "./MachineSettings.css";
import MachineTable from "./MachineTable/MachineTable";
function MachineSettings(props) {
  const { langIndex } = useSelector((state) => state.app);
  const {
    showModal,
    setShowModal,
    selCustomerId,
    machineList,
    setMachineInfoList,
    setMachineList,
  } = props;

  const [isShowModal, setIsShowModal] = useState(false);
  const [initMachineInfo, setInitMachineInfo] = useState(null);
  const [isBusy, setBusy] = useState(false);

  const onClickEditRecord = (record) => {
    setInitMachineInfo(record);
    setIsShowModal(true);
  };

  const onClickAddMachine = () => {
    setInitMachineInfo(null);
    setIsShowModal(true);
  };

  const onCancel = () => {
    setShowModal(false);
  };

  const addMachine = (machineInfo) => {
    // selCustomerId
    machineInfo["customer_id"] = selCustomerId;
    var id =
      machineList.length == 0
        ? 1
        : Math.max.apply(
            Math,
            machineList.map(function (o) {
              return o.id;
            })
          ) + 1;

    machineInfo["id"] = id;

    AddMachine(machineInfo, (res) => {
      if (res == null) return;
      if (res.status == true) {
        var newItem = { ...machineInfo, key: machineInfo.id };
        newItem.order = machineList.length + 1;
        setMachineList([...machineList, newItem]);
        message.success(lang(langIndex, "msg_update_success"));
        setIsShowModal(false);
        setMachineInfoList({ selCustomerId: machineList });
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });

    var param = {
      customer_id: selCustomerId,
      machine_id: machineInfo.machine_id,
      detail: {
        factory_id: selCustomerId,
        machine_id: machineInfo.machine_id,
        device_id: machineInfo.device_id,
      },
    };
    postRequest(Urls.UPDATE_APP_SETTING, param, (res) => {});
  };

  const moveMachine = (machineInfo, dir) => {
    let position = parseInt(machineInfo.order);
    if (dir == -1 && position > 1) {
      let query1 =
        "UPDATE " +
        "`" +
        selCustomerId +
        "_status` " +
        "SET " +
        "`order`=" +
        "'" +
        position +
        "' WHERE `order`='" +
        (position - 1) +
        "'";
      setBusy(true);
      ExecuteQuery(query1, (res) => {
        let query2 =
          "UPDATE " +
          "`" +
          selCustomerId +
          "_status` " +
          "SET " +
          "`order`=" +
          "'" +
          (position - 1) +
          "' WHERE `id`='" +
          machineInfo.id +
          "'";
        ExecuteQuery(query2, (res) => {
          setBusy(false);
        });
      });

      let newMachineList = [...machineList];
      newMachineList[position - 2].order = position;
      newMachineList[position - 1].order = position - 1;
      newMachineList.sort((a, b) => a.order - b.order);
      setMachineList(newMachineList);
    }

    if (dir == 1 && position < machineList.length) {
      let query =
        "UPDATE " +
        "`" +
        selCustomerId +
        "_status` " +
        "SET " +
        "`order`=" +
        "'" +
        position +
        "' WHERE `order`='" +
        (position + 1) +
        "'";
      setBusy(true);
      ExecuteQuery(query, (res) => {
        query =
          "UPDATE " +
          "`" +
          selCustomerId +
          "_status` " +
          "SET " +
          "`order`=" +
          "'" +
          (position + 1) +
          "' WHERE `id`='" +
          machineInfo.id +
          "'";
        ExecuteQuery(query, (res) => {
          setBusy(false);
        });
      });

      let newMachineList = [...machineList];
      newMachineList[position].order = position;
      newMachineList[position - 1].order = position + 1;
      newMachineList.sort((a, b) => a.order - b.order);
      setMachineList(newMachineList);
    }
  };

  const editMachine = (machineInfo) => {
    const newData = [...machineList];
    const index = newData.findIndex((item) => item.key === machineInfo.key);
    if (index > -1) {
      const item = newData[index];
      newData.splice(index, 1, { ...item, ...machineInfo });
      const newItem = newData[index];
      let query1 =
        "UPDATE " +
        "`" +
        selCustomerId +
        "_status` " +
        "SET " +
        "`machine_id`='" +
        newItem.machine_id +
        "', " +
        "`machine_picture_url`='" +
        newItem.machine_picture_url +
        "' " +
        "WHERE `id`='" +
        newItem.id +
        "'";

      let query2 =
        "UPDATE " +
        "`buffer_ganttdata` " +
        "SET " +
        "`machine_id`='" +
        newItem.machine_id +
        "' WHERE `machine_id`='" +
        item.machine_id +
        "' AND `customer_id`='" +
        selCustomerId +
        "'";

      ExecuteQuery(query1, (res) => {
        ExecuteQuery(query2, (res) => {
          var param = {
            customer_id: selCustomerId,
            machine_id: item.machine_id,
            detail: {
              factory_id: selCustomerId,
              machine_id: newItem.machine_id,
              device_id: newItem.device_id,
            },
          };
          postRequest(Urls.UPDATE_APP_SETTING, param, (res) => {});
        });
      });

      var tmpMachineInfoList = newData;

      tmpMachineInfoList[selCustomerId] = newData;
      setMachineInfoList(tmpMachineInfoList);
      setMachineList(newData);
      message.success(lang(langIndex, "msg_update_success"));
      setIsShowModal(false);
    } else {
      message.error(lang(langIndex, "msg_something_wrong"));
    }
  };

  return (
    <Modal
      centered
      visible={showModal}
      title={null}
      onCancel={() => onCancel()}
      onOk={() => onCancel()}
      closable={true}
      className="customer-setting-dialog-style"
      destroyOnClose={true}
      width={700}
      footer={null}
    >
      <div className="customer-setting-page-title">
        <SettingOutlined />
        <span style={{ marginLeft: 10 }} />
        {lang(langIndex, "plant_machinesetting")}
      </div>

      <div
        className="content-scrollbar-style user-setting-container"
        style={{ overflow: "auto" }}
      >
        {isShowModal === true && (
          <MachineManageDialog
            initMachineInfo={initMachineInfo}
            addMachine={addMachine}
            editMachine={editMachine}
            isShowModal={isShowModal}
            setIsShowModal={setIsShowModal}
          />
        )}
        <div className="user-setting-button-container">
          <Button
            ghost
            onClick={() => {
              onClickAddMachine();
            }}
          >
            {lang(langIndex, "plant_addmachine")} +{" "}
          </Button>
        </div>
        <div className="user-setting-user-table-style">
          <Spin spinning={isBusy}>
            <MachineTable
              machineList={machineList}
              onClickEditRecord={onClickEditRecord}
              moveMachine={moveMachine}
            />
          </Spin>
        </div>
      </div>
    </Modal>
  );
}

export default MachineSettings;
