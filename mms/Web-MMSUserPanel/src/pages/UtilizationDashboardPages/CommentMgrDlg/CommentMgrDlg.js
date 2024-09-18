import React, { useEffect, useState } from "react";
import { message, Modal, Button, Input, Select, Row, Col } from "antd";

import {
  onSelectMenu,
  SetLanguage,
} from "../../../../src/services/redux/reducers/app";
import { connect, useDispatch, useSelector } from "react-redux";

import "./CommentMgrDlg.css";
import {
  ConvertTimespanToDateBasedOnTimezone,
  ExecuteQuery,
  getBetweenTime,
  GetTimeWithStyle,
} from "../../../../src/services/common/cnc_apis";
import lang from "../../../../src/services/lang";
import Urls, { postRequest } from "../../../../src/services/common/urls";

const { TextArea } = Input;
const { Option } = Select;

function CommentMgrDlg(props) {

  const dispatch = useDispatch();
  const { langIndex } = useSelector((x) => x.app);

  const {
    customer_id,
    selectedChatItem,
    detailedGanttInfo,
    timezone,
    ganttInfo,
    security_level,
    machineInfo,
  } = props;

  const [appSetting, setAppSetting] = useState({
    downtime_reason1: "Clear Chips",
    downtime_reason2: "Wait Materials",
    downtime_reason3: "Wait Tooling",
    downtime_reason4: "Break",
    downtime_reason5: "No Operator",
    downtime_reason6: "P.M.",
    downtime_reason7: "Unplanned Repair",
    downtime_reason8: "Other",
  });

  useEffect(() => {
    var url = Urls.GET_APP_SETTING;
    var param = {
      factory_id: customer_id,
      machine_id: machineInfo.machine_id,
    };

    postRequest(url, param, (res) => {
      if (res.status == true) {
        setAppSetting({
          ...res.data,
        });
      }
    });
  }, []);

  const { isVisibleModal, setIsVisibleModal } = props;
  const [comment, setComment] = useState(selectedChatItem["comment"]);
  const [isUpdating, setIsUpdating] = useState(false);
  const [newSatus, setNewStatus] = useState(lang(langIndex, "select_status"));
  const [newSatusIndex, setNewStatusIndex] = useState(-1);
  const [newColor, setNewColor] = useState("");
  const { myGanttList, setMyGanttList } = props;

  const status = selectedChatItem["status"];
  var statusSelectClass = "show";
  if (status === "In Cycle" || security_level == 0)
    statusSelectClass = "hide";

  const startTime = ConvertTimespanToDateBasedOnTimezone(
    selectedChatItem["start"],
    timezone
  );
  const endTime = ConvertTimespanToDateBasedOnTimezone(
    selectedChatItem["end"],
    timezone
  );
  const operator = selectedChatItem["Operator"];
  const duration = getBetweenTime(
    selectedChatItem["start"],
    selectedChatItem["end"]
  );
  const job_id =
    selectedChatItem["job_id"] == undefined ? "" : selectedChatItem["job_id"];
  const mainProgram =
    selectedChatItem["main_program"] == undefined
      ? ""
      : selectedChatItem["main_program"];
  const currentProgram =
    selectedChatItem["current_program"] == undefined
      ? ""
      : selectedChatItem["current_program"];

  const onUpdateComment = (e) => {
    const query =
      "UPDATE `" +
      customer_id +
      "_ganttdata` SET `comment`='" +
      comment +
      "' WHERE `created_at`='" +
      selectedChatItem["created_at"] +
      "'";
    ExecuteQuery(query, (res) => {
      message.success(lang(langIndex, "msg_update_success"));

      var tmpGanttList = myGanttList;
      var selGanttData = tmpGanttList.filter(
        (x) => x.created_at == selectedChatItem.created_at
      );

      if (selGanttData.length > 0) {
        selGanttData = selGanttData[0];
        selGanttData.comment = comment;
        setMyGanttList([...tmpGanttList]);
      }
      setIsVisibleModal(false);
    });
  };

  const onCancelComment = (e) => {
    setIsVisibleModal(false);
  };

  const selectUI = (
    <Select
      value={newSatus}
      onChange={(v) => onSelectStatus(v)}
      className="status-select-menu"
      dropdownClassName="page-changer-style-dropdown"
    >
      {appSetting.downtime_reason1 !== "" && (
        <Option className="page-changer-item" value={0}>
          {appSetting.downtime_reason1}
        </Option>
      )}
      {appSetting.downtime_reason2 !== "" && (
        <Option className="page-changer-item" value={1}>
          {appSetting.downtime_reason2}
        </Option>
      )}
      {appSetting.downtime_reason3 !== "" && (
        <Option className="page-changer-item" value={2}>
          {appSetting.downtime_reason3}
        </Option>
      )}
      {appSetting.downtime_reason4 !== "" && (
        <Option className="page-changer-item" value={3}>
          {appSetting.downtime_reason4}
        </Option>
      )}
      {appSetting.downtime_reason5 !== "" && (
        <Option className="page-changer-item" value={4}>
          {appSetting.downtime_reason5}
        </Option>
      )}
      {appSetting.downtime_reason6 !== "" && (
        <Option className="page-changer-item" value={5}>
          {appSetting.downtime_reason6}
        </Option>
      )}
      {appSetting.downtime_reason7 !== "" && (
        <Option className="page-changer-item" value={6}>
          {appSetting.downtime_reason7}
        </Option>
      )}
      {appSetting.downtime_reason8 !== "" && (
        <Option className="page-changer-item" value={7}>
          {appSetting.downtime_reason8}
        </Option>
      )}
    </Select>
  );

  function onSelectStatus(index) {
    setNewStatusIndex(index);
    const statusValue = [];
    statusValue.push(appSetting.downtime_reason1);
    statusValue.push(appSetting.downtime_reason2);
    statusValue.push(appSetting.downtime_reason3);
    statusValue.push(appSetting.downtime_reason4);
    statusValue.push(appSetting.downtime_reason5);
    statusValue.push(appSetting.downtime_reason6);
    statusValue.push(appSetting.downtime_reason7);
    statusValue.push(appSetting.downtime_reason8);
    setNewStatus(statusValue[index]);
  }

  function onChangeStatus() {
    if (newSatusIndex > -1) {
      const statusValue = [];
      statusValue.push(appSetting.downtime_reason1);
      statusValue.push(appSetting.downtime_reason2);
      statusValue.push(appSetting.downtime_reason3);
      statusValue.push(appSetting.downtime_reason4);
      statusValue.push(appSetting.downtime_reason5);
      statusValue.push(appSetting.downtime_reason6);
      statusValue.push(appSetting.downtime_reason7);
      statusValue.push(appSetting.downtime_reason8);
      const statusColor = [];
      statusColor.push("#ffec00");
      statusColor.push("#549afc");
      statusColor.push("#c000db");
      statusColor.push("#9898db");
      statusColor.push("#B0E0E6");
      statusColor.push("#6aa786");
      statusColor.push("#c0a0c0");
      statusColor.push("#808080");
      var url = Urls.UPDATE_GANTT_STATUS;
      var param = {
        status: statusValue[newSatusIndex],
        color: statusColor[newSatusIndex],
        start: selectedChatItem["start"],
        end: selectedChatItem["end"],
        customer_id: customer_id,
        machine_id: selectedChatItem["machine_id"]
      };
      postRequest(url, param, (res) => {
        if (res.status == true) {
          message.success(lang(langIndex, "msg_update_success"));
        }
      });
    }
  }

  // GetTimeWithStyle

  return (
    <div>
      {isVisibleModal == false ? null : (
        <Modal
          destroyOnClose={true}
          visible={isVisibleModal}
          onOk={onUpdateComment}
          onCancel={onCancelComment}
          okButtonProps={{
            style: {
              display: security_level == 0 ? "none" : "",
            },
          }}
          cancelButtonProps={{
            style: {
              display: security_level == 0 ? "none" : "",
            },
          }}
          className="comment-dialog-style"
        >
          <p style={{ fontSize: 20 }}>{selectedChatItem["machine_id"]}</p>
          <p>
            {lang(langIndex, "cnc_jobid")}: {job_id}
          </p>
          <p>
            {lang(langIndex, "cnc_mainprogram")}: {mainProgram}
          </p>
          <p>
            {lang(langIndex, "cnc_currentprogram")}: {currentProgram}
          </p>

          <p style={{ color: selectedChatItem["color"], fontSize: 20 }}>
            {lang(langIndex, "cnc_status")}: {status}
          </p>

          <p className={statusSelectClass}>

            {security_level != 0 && (
              <Row align="middle">
                <Col span={6} style={{ textAlign: "right", marginRight: 10 }}>
                  {lang(langIndex, "change_to")}
                </Col>
                <Col span={12}>{selectUI}</Col>
                <Col span={4} style={{ paddingLeft: 10 }}>
                  <Button type="primary" onClick={onChangeStatus}>
                    {lang(langIndex, "jobentry_save")}
                  </Button>
                </Col>
              </Row>
            )}
          </p>

          <p>
            {lang(langIndex, "cnc_time")}: {GetTimeWithStyle(startTime)} -{" "}
            {GetTimeWithStyle(endTime)}
          </p>
          <p>
            {lang(langIndex, "cnc_duration")}: {duration}
          </p>
          <p>
            {lang(langIndex, "plant_operator")}: {operator}
          </p>

          <span>{lang(langIndex, "cnc_comment")}</span>
          <TextArea
            style={{ marginTop: 5 }}
            value={comment}
            onChange={(e) => setComment(e.target.value)}
          />
        </Modal>
      )}
    </div>
  );
}

export default CommentMgrDlg;
