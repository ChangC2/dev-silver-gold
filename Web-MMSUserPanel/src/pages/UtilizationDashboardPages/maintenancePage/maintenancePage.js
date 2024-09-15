import { Button, message } from "antd";
import { useEffect, useState } from "react";
import "./maintenancePage.css";

import MaintenanceFilesModal from "./maintenanceFilesModal/maintenanceFilesModal";
import MaintenanceModal from "./maintenanceModal/maintenanceModal";
import MaintenanceTable from "./maintenanceTable/maintenanceTable";

import {
  AddMaintenanceEntry,
  ExecuteQuery,
  GetIncycleTimes,
  GetMaintenanceEntry,
} from "../../../../src/services/common/cnc_apis";

import { useSelector } from "react-redux";
import lang from "../../../../src/services/lang";

function MaintenancePage(props) {
  const authData = useSelector((x) => x.authService);
  const { security_level } = authData;
  const { langIndex } = useSelector((x) => x.app);
  const { selCustomerId, machineList } = props;
  const [entryList, setEntryList] = useState([]);
  const [selectedEntry, setSelectedEntry] = useState(undefined);
  const [isBusy, setIsBusy] = useState(false);
  const [isShowModal, setIsShowModal] = useState(false);

  const [isShowFiles, setIsShowFiles] = useState(false);
  const [files, setFiles] = useState("");
  const [RecordId, setRecordId] = useState("");

  useEffect(() => {
    GetMaintenanceEntry(selCustomerId, (entryList) => {
      if (entryList != null || entryList.length > 0) {
        var workingItemList = [];
        entryList.forEach(function (entry) {
          entry.key = entry.id;
          if (entry.start > 0) {
            workingItemList.push(entry);
          }
        });
        setEntryList(entryList);
        if (workingItemList.length > 0)
          calcRemainTimes(workingItemList, entryList);
      } else {
      }
    });
  }, [machineList]);
  const calcRemainTimes = (workingItemList, initEntryList) => {
    var params = [];
    var machineIds = [];
    var startTimes = [];
    for (var i = 0; i < workingItemList.length; i++) {
      machineIds.push(workingItemList[i].machine_id);
      startTimes.push(workingItemList[i].start);
    }
    params = {
      customer_id: selCustomerId,
      machine_ids: machineIds,
      start_times: startTimes,
    };
    GetIncycleTimes(params, (res) => {
      var updateEntryList = initEntryList;
      for (var i = 0; i < res.length; i++) {
        var id = res[i].machine_id;
        var incycle_time = res[i].incycle_time;
        var entry = updateEntryList.filter((x) => x.machine_id === id);
        if (entry.length > 0) {
          entry = entry[0];
          entry.completed_hours = entry.frequency * 3600 - incycle_time;
        }
      }
      setEntryList([...updateEntryList]);
    });
  };

  const updateEntry = (data) => {
    const newData = [...entryList];

    const index = newData.findIndex((item) => item.id === data.id);

    if (index > -1) {
      const item = newData[index];
      newData.splice(index, 1, { ...item, ...data });

      const newItem = newData[index];
      let query =
        "UPDATE " +
        "`" +
        selCustomerId +
        "_maintenance` " +
        "SET `machine_id`='" +
        newItem.machine_id +
        "', " +
        "`task_name`='" +
        newItem.task_name +
        "', " +
        "`task_category`='" +
        newItem.task_category +
        "', " +
        "`picture`='" +
        newItem.picture +
        "', " +
        "`task_instruction`='" +
        newItem.task_instruction +
        "', " +
        "`frequency`=" +
        newItem.frequency +
        ", " +
        "`interlock`=" +
        newItem.interlock +
        " " +
        "WHERE `id`=" +
        newItem.id;
      ExecuteQuery(query, (res) => {
        message.success(lang(langIndex, "msg_update_success"));
      });

      setEntryList(newData);
    }
    setIsShowModal(false);
  };

  const addEntry = (data) => {
    setIsBusy(true);
    data = { ...data, customer_id: selCustomerId };
    AddMaintenanceEntry(data, (res) => {
      setIsBusy(false);
      if (res.status === true) {
        var newItem = { ...res.data[0], key: res.data[0].id };
        const index = entryList.findIndex((item) => item.id === newItem.id);
        if (index > -1) {
          message.error(lang(langIndex, "msg_something_wrong"));
        } else {
          setEntryList([...entryList, newItem]);
          message.success(lang(langIndex, "msg_update_success"));
        }
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
      setIsShowModal(false);
    });
  };

  const onClickEditRecord = (entry) => {
    setSelectedEntry(entry);
    setIsShowModal(true);
  };
  const onDeleteRecord = (entry) => {
    const newData = entryList.filter((element) => element != entry);
    setEntryList(newData);

    let query =
      "DELETE FROM " +
      "`" +
      selCustomerId +
      "_maintenance` " +
      "WHERE `id`=" +
      entry.id;

    ExecuteQuery(query, (res) => {
      message.success(lang(langIndex, "msg_delete_success"));
    });
  };

  return (
    <div style={{ margin: 15 }}>
      {isShowFiles && (
        <MaintenanceFilesModal
          files={files}
          setFiles={setFiles}
          addEntry={addEntry}
          updateEntry={updateEntry}
          Id={RecordId}
          security_level={security_level}
          selCustomerId={selCustomerId}
          isShowFiles={isShowFiles}
          setIsShowFiles={setIsShowFiles}
        />
      )}

      {isShowModal && (
        <MaintenanceModal
          addEntry={addEntry}
          updateEntry={updateEntry}
          isShowModal={isShowModal}
          setIsShowModal={setIsShowModal}
          machineList={machineList}
          initEntryInfo={selectedEntry}
        />
      )}
      <div style={{ textAlign: "right" }}>
        <Button
          loading={isBusy}
          onClick={() => {
            setSelectedEntry(undefined);
            setIsShowModal(true);
          }}
          type="primary"
          style={{
            marginBottom: 10,
            width: 120,
            height: 35,
            borderRadius: 5,
          }}
        >
          {lang(langIndex, "maintenance_addnew")}
        </Button>
      </div>
      <div>
        <MaintenanceTable
          machineList={machineList}
          matintenanceEntryList={entryList}
          onClickEditRecord={onClickEditRecord}
          onDeleteRecord={onDeleteRecord}
          setRecordId={setRecordId}
          setFiles={setFiles}
          setIsShowFiles={setIsShowFiles}
        />
      </div>
    </div>
  );
}

export default MaintenancePage;
