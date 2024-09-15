import React, { useState, useEffect } from "react";
import "./TimeSettings.css";
import { Input, Modal, Select } from "antd";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import { SettingOutlined } from "@ant-design/icons";
const { Option } = Select;

const ct = require("countries-and-timezones");
const timezones = ct.getAllTimezones();
const timezoneList = Object.keys(timezones).map((x) => timezones[x]);

function TimeSettings(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal, customerInfo, updateCustomerInfo } = props;
  const [customerTimezone, setCustomerTimezone] = useState(0);
  const [customerTimezoneName, setCustomerTimezoneName] = useState("");
  const [isCollapsed, setIsCollapsed] = useState(true);
  const [selectUI, setSelectUI] = useState("");

  useEffect(() => {
    setCustomerTimezone(customerInfo.timezone);
    setCustomerTimezoneName(customerInfo.timezone_name);
  }, [customerInfo]);

  useEffect(() => {
    let timezoneUIList = timezoneList.map((timezoneInfo, index) => {
      return (
        <Option key={index} value={timezoneInfo.name}>
          {timezoneInfo.name + "(" + timezoneInfo.utcOffsetStr + ")"}
        </Option>
      );
    });
    setSelectUI(
      <Select
        className={"timezone-page-timezone"}
        dropdownClassName={"timezone-page-timezone-list"}
        showSearch
        style={{ width: "100%" }}
        defaultValue={customerTimezoneName}
        filterOption={(input, option) =>
          option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
        }
        onChange={(v) => onChangeTimezone(v)}
      >
        {timezoneUIList}
      </Select>
    );
  }, [customerTimezoneName]);

  const onUpdateTimezone = () => {
    if (customerInfo.timezone == customerTimezone) return;
    let newInfo = customerInfo;
    newInfo.timezone = customerTimezone;
    newInfo.timezone_name = customerTimezoneName;
    updateCustomerInfo(newInfo);
  };

  const onChangeTimezone = (v) => {
    let timezoneInfo = timezoneList.find((x) => x.name == v);
    if (timezoneInfo != null) {
      setCustomerTimezone(timezoneInfo.utcOffset / 60);
      setCustomerTimezoneName(timezoneInfo.name);
    }
  };

  const onCancel = () => {
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        visible={showModal}
        title={null}
        onCancel={() => onCancel()}
        onOk={() => onUpdateTimezone()}
        okText={lang(langIndex, "plant_update")}
        closable={true}
        className="customer-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div className="customer-setting-page-title">
          <SettingOutlined />
          <span style={{ marginLeft: 10 }} />
          {lang(langIndex, "plant_timesetting")}
        </div>
        <div className="customer-setting-container-style">
          <div className="timezone-title-style">
            {" "}
            {lang(langIndex, "plant_timezone")}
          </div>
          {selectUI}
        </div>
      </Modal>
    </div>
  );
}

export default TimeSettings;
