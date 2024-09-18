import { message, Spin } from "antd";
import { useEffect, useState } from "react";
import "./jobEntryPage.css";

import {
  AddEntry,
  ExecuteQuery,
  GetJobEntryList,
} from "../../services/common/cnc_apis";

import { connect, useSelector } from "react-redux";
import lang from "../../services/lang";
import JobEntryTable from "./jobEntryTable/jobEntryTable";

function JobEntryPage(props) {
  const { langIndex } = useSelector((state) => state.app);

  const authData = useSelector((x) => x.authService);
  const { security_level } = authData;
  const selCustomerId = props.app.customer_id;

  const [entryList, setEntryList] = useState([]);
  const [isBusy, setIsBusy] = useState(false);
  const [searchKey, setSearchKey] = useState("");
  const [searchDate, setSearchDate] = useState("");
  const [maxJobId, setMaxJobId] = useState(0);

  useEffect(() => {
    setIsBusy(true);
    GetJobEntryList(selCustomerId, (entryList) => {
      setIsBusy(false);
      if (entryList !== null || entryList.length > 0) {
        setEntryList(entryList);
      }
    });
  }, [selCustomerId]);

  useEffect(() => {
    let jobId = 0;
    entryList.forEach(function (entry) {
      entry.key = entry.Id;
      let val = parseInt(entry.jobID);
      if (val != NaN && jobId < val) {
        jobId = val;
      }
    });
    setMaxJobId(jobId + 1);
  }, [entryList]);

  const updateEntry = (key, row) => {
    const newData = [...entryList];
    const index = newData.findIndex((item) => key === item.key);
    if (index > -1) {
      const item = newData[index];
      newData.splice(index, 1, { ...item, ...row });

      const newItem = newData[index];
      let query =
        "UPDATE " +
        "`" +
        selCustomerId +
        "_jobdata` " +
        "SET `jobID`='" +
        newItem.jobID +
        "', " +
        "`customer`='" +
        newItem.customer +
        "', " +
        "`partNumber`='" +
        newItem.partNumber +
        "', " +
        "`programNumber`='" +
        newItem.programNumber +
        "', " +
        "`description`='" +
        newItem.description +
        "', " +
        "`partsPerCycle`=" +
        newItem.partsPerCycle +
        ", " +
        "`targetCycleTime`=" +
        newItem.targetCycleTime +
        ", " +
        "`qtyRequired`=" +
        newItem.qtyRequired +
        ", " +
        "`qtyCompleted`='" +
        newItem.qtyCompleted +
        "', " +
        "`orderDate`='" +
        newItem.orderDate +
        "', " +
        "`dueDate`='" +
        newItem.dueDate +
        "', " +
        "`aux1data`='" +
        newItem.aux1data +
        "', " +
        "`aux2data`='" +
        newItem.aux2data +
        "', " +
        "`aux3data`='" +
        newItem.aux3data +
        "' " +
        "WHERE `Id`=" +
        newItem.Id;
      ExecuteQuery(query, (res) => {
        message.success(lang(langIndex, "msg_update_success"));
      });
      setEntryList(newData);
    }
  };

  const addEntry = (data) => {
    setIsBusy(true);
    data = { ...data, customer_id: selCustomerId };
    AddEntry(data, (res) => {
      setIsBusy(false);
      if (res.status == true) {
        var newItem = { ...res.data[0], key: res.data[0].Id };
        const index = entryList.findIndex((item) => item.Id == newItem.Id);
        if (index > -1) {
          message.error(lang(langIndex, "msg_something_wrong"));
        } else {
          setEntryList([...entryList, newItem]);
          message.success(lang(langIndex, "msg_update_success"));
        }
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  const deleteEntry = (id) => {
    const newData = entryList.filter((element) => element.Id !== id);
    setEntryList(newData);

    let query =
      "DELETE FROM " + "`" + selCustomerId + "_jobdata` " + "WHERE `Id`=" + id;

    ExecuteQuery(query, (res) => {
      message.success(lang(langIndex, "msg_delete_success"));
    });
  };


  if (isBusy) {
    return (
      <div style={{ textAlign: "center", paddingTop: 100 }}>
        {" "}
        <Spin tip="Loading ..." size="large" />
      </div>
    );
  }

  return (
    <div style={{ margin: 15 }}>
      <JobEntryTable
        entryList={entryList.filter(
          (x) => x.jobID.includes(searchKey) && x.dueDate.includes(searchDate)
        )}
        updateEntry={updateEntry}
        selCustomerId={selCustomerId}
        addEntry={addEntry}
        setSearchKey={setSearchKey}
        setSearchDueDate={setSearchDate}
        isBusy={isBusy}
        setIsBusy={setIsBusy}
        security_level={security_level}
        deleteEntry={deleteEntry}
        maxJobId={maxJobId}
      />
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  cncService: state.cncService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(JobEntryPage);
