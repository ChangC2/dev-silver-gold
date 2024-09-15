import { RightOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import lang from "../../services/lang";
import "./PlantSettingsPopover.css";
import { message } from "antd";
import { connect, useSelector } from "react-redux";
import {
  ExecuteQuery,
  GetMachineList,
  GetStatus,
  updateCustomerInfo,
  updateMachineListInfo,
} from "services/common/cnc_apis";
import CustomerSettings from "./CustomerSettings/CustomerSettings";
import GroupSettings from "./GroupSettings/GroupSettings";
import FirmwareSettings from "./FirmwareSettings/FirmwareSettings";
import MachineSettings from "./MachineSettings/MachineSettings";
import "./PlantSettingsPopover.css";
import ReportSettings from "./ReportSettings/ReportSettings";
import ShiftSettings from "./ShiftSettings/ShiftSettings";
import TimeSettings from "./TimeSettings/TimeSettings";
import UserSettings from "./UserSettings/UserSettings";
import LaborSettings from "./LaborSettings/LaborSettings";
import TrelloSettings from "./TrelloSettings/TrelloSettings";

function PlantSettingsPopover(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { setOpenSettings } = props;
  const [openCustomerSettings, setOpenCustomerSettings] = useState(false);
  const [openUserSettings, setOpenUserSettings] = useState(false);
  const [openMachineSettings, setOpenMachineSettings] = useState(false);
  const [openTimeSettings, setOpenTimeSettings] = useState(false);
  const [openShiftSettings, setOpenShiftSettings] = useState(false);
  const [openReportSettings, setOpenReportSettings] = useState(false);
  const [openGroupSettings, setOpenGroupSettings] = useState(false);
  const [openFirmwareSettings, setOpenFirmwareSettings] = useState(false);
  const [openLaborSettings, setOpenLaborSettings] = useState(false);
  const [openTrelloSettings, setOpenTrelloSettings] = useState(false);

  useEffect(() => {}, []);

  const onClickMenu = (key) => {
    setOpenSettings(false);
    switch (key) {
      case 0:
        setOpenCustomerSettings(true);
        break;
      case 1:
        setOpenUserSettings(true);
        break;
      case 2:
        setOpenMachineSettings(true);
        break;
      case 3:
        setOpenTimeSettings(true);
        break;
      case 4:
        setOpenShiftSettings(true);
        break;
      case 5:
        setOpenReportSettings(true);
        break;
      case 6:
        setOpenGroupSettings(true);
        break;
      case 7:
        setOpenFirmwareSettings(true);
        break;
      case 8:
        setOpenLaborSettings(true);
        break;
      case 9:
        setOpenTrelloSettings(true);
        break;
      default:
        break;
    }
  };

  const selCustomerId = props.app.customer_id;
  const [machineList, setMachineList] = useState([]);

  const customerInfo = props.cncService.customerInfoList[selCustomerId];
  // change customertimezone
  const onUpdateCustomerInfo = (newItem) => {
    updateCustomerInfo(selCustomerId, { ...newItem }, props.dispatch);
    let query =
      "UPDATE " +
      "`" +
      selCustomerId +
      "_info`" +
      "SET " +
      "`logo`='" +
      newItem.logo +
      "', " +
      "`name`='" +
      newItem.name +
      "', " +
      "`timezone`=" +
      newItem.timezone +
      ", " +
      "`timezone_name`='" +
      newItem.timezone_name +
      "', " +
      "`report_days`='" +
      newItem.report_days +
      "', " +
      "`emails`='" +
      newItem.emails +
      "', " +
      "`shift1_start`='" +
      newItem.shift1_start +
      "', " +
      "`shift1_end`='" +
      newItem.shift1_end +
      "', " +
      "`shift2_start`='" +
      newItem.shift2_start +
      "', " +
      "`shift2_end`='" +
      newItem.shift2_end +
      "', " +
      "`shift3_start`='" +
      newItem.shift3_start +
      "', " +
      "`shift3_end`='" +
      newItem.shift3_end +
      "'";
    ExecuteQuery(query, (res) => {
      message.success(lang(langIndex, "msg_update_success"));
    });
  };

  const onUpdateMachineList = (machineInfo) => {
    updateMachineListInfo(selCustomerId, [...machineInfo], props.dispatch);
  };

  useEffect(() => {
    GetStatus(selCustomerId, props.dispatch);
    GetMachineList(selCustomerId, (mList) => {
      if (mList != null || mList.length > 0) {
        mList.forEach(function (entry) {
          entry.key = entry.id;
        });
        mList.sort((a, b) => a.order - b.order);
        setMachineList(mList);
      }
    });
  }, [selCustomerId, openMachineSettings]);

  return (
    <div>
      <CustomerSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        updateCustomerInfo={onUpdateCustomerInfo}
        showModal={openCustomerSettings}
        setShowModal={setOpenCustomerSettings}
      />

      <UserSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        showModal={openUserSettings}
        setShowModal={setOpenUserSettings}
      />

      <MachineSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        setMachineInfoList={onUpdateMachineList}
        machineList={machineList}
        setMachineList={setMachineList}
        showModal={openMachineSettings}
        setShowModal={setOpenMachineSettings}
      />
      <TimeSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        updateCustomerInfo={onUpdateCustomerInfo}
        showModal={openTimeSettings}
        setShowModal={setOpenTimeSettings}
      />

      <ShiftSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        updateCustomerInfo={onUpdateCustomerInfo}
        showModal={openShiftSettings}
        setShowModal={setOpenShiftSettings}
      />

      <ReportSettings
        selCustomerId={selCustomerId}
        customerInfo={customerInfo}
        updateCustomerInfo={onUpdateCustomerInfo}
        showModal={openReportSettings}
        setShowModal={setOpenReportSettings}
      />

      <GroupSettings
        selCustomerId={selCustomerId}
        machineList={machineList}
        showModal={openGroupSettings}
        setShowModal={setOpenGroupSettings}
      />

      <FirmwareSettings
        showModal={openFirmwareSettings}
        setShowModal={setOpenFirmwareSettings}
      />

      <LaborSettings
        showModal={openLaborSettings}
        setShowModal={setOpenLaborSettings}
      />

      <TrelloSettings
        showModal={openTrelloSettings}
        setShowModal={setOpenTrelloSettings}
      />

      <div className="plant-settings-modal-body-container">
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(0);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_customersetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(1);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_usersettings")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(2);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_machinesetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(3);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_timesetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(4);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "shift_settings")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(5);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_reportsetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(6);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_groupsetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(7);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_firmwaresetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(8);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {lang(langIndex, "plant_laborsetting")}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
        <div
          className="plant-settings-menu-container"
          onClick={() => {
            onClickMenu(9);
          }}
        >
          <a>
            <span className="plant-settings-menu-label">
              {"Trello Settings"}
            </span>
            <RightOutlined className="plant-settings-menu-icon" />
          </a>
        </div>
      </div>
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PlantSettingsPopover);
