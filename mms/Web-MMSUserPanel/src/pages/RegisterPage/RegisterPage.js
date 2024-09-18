import React, { useState, useEffect } from "react";
import { Link, withRouter } from "react-router-dom";
import { Button, Col, Input, message, Row, Select, Spin } from "antd";
import blankFactory from "../../assets/images/blank_factory.png";

import "./RegisterPage.css";
import Urls, { postRequest, USER_SITE } from "../../services/common/urls";
import logo from "../../assets/images/logo.png";
import { useDispatch, useSelector } from "react-redux";
import { SignOut } from "../../services/common/auth_apis";
import lang from "../../services/lang";
const { Option } = Select;
const ct = require("countries-and-timezones");
const timezones = ct.getAllTimezones();
const timezoneList = Object.keys(timezones).map((x) => timezones[x]);
const currentTimezoneName = Intl.DateTimeFormat().resolvedOptions().timeZone;
function hasWhiteSpace(s) {
  return s.indexOf(" ") >= 0;
}
function RegisterPage(props) {
  const dispatch = useDispatch();
  const appData = useSelector((state) => state.app);
  const { langIndex } = appData;
  const [machineInfo, setMachineInfo] = useState({});
  const [isUploading, setIsUploading] = useState(false);
  const [isWrong, setIsWrong] = useState(false);
  const [isBusy, setIsBusy] = useState(false);
  const [wrongMessage, setWrongMessage] = useState("");

  useEffect(() => {
    SignOut(dispatch);
    setMachineInfo({ timezone: currentTimezoneName });
  }, []);

  const onDropImage = (e) => {
    const files = Array.from(e.target.files);
    if (files.length == 0) return;
    var formData = new FormData();
    setIsUploading(true);
    formData.append("file", files[0]);
    postRequest(Urls.UPLOAD_FACTORY_IMAGE, formData, (response) => {
      setIsUploading(false);
      if (response.status == true) {
        setMachineInfo({ ...machineInfo, logo: response.url });
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  const checkValue = (field, name) => {
    if (machineInfo[field] == undefined) {
      message.error(lang(langIndex, "register_specify") + name);
      return false;
    }
    return true;
  };
  
  const onClickRegisterButton = async () => {
    const {
      logo,
      serialKey,
      factoryId,
      factoryName,
      userName,
      password,
      confirmPassword,
      timezone,
      userFullName,
    } = machineInfo;
    if (!checkValue("logo", lang(langIndex, "register_logo"))) return;
    if (!checkValue("serialKey", lang(langIndex, "register_serial_key")))
      return;
    if (!checkValue("factoryId", lang(langIndex, "register_factory_id")))
      return;
    if (!checkValue("factoryName", lang(langIndex, "register_factory_name")))
      return;
    if (!checkValue("userName", lang(langIndex, "register_username"))) return;
    if (!checkValue("userFullName", lang(langIndex, "register_fullname")))
      return;
    if (!checkValue("password", lang(langIndex, "register_password"))) return;
    if (!checkValue("timezone", lang(langIndex, "register_timezone"))) return;

    if (hasWhiteSpace(factoryId)) {
      message.error(lang(langIndex, "register_err1"));
      return;
    }
    if (hasWhiteSpace(userName)) {
      message.error(lang(langIndex, "register_err2"));
      return;
    }
    if (password != confirmPassword) {
      message.error(lang(langIndex, "register_err3"));
      return;
    }
    if (password.length < 6) {
      message.error(lang(langIndex, "register_err4"));
      return;
    }
    var selTimezone = timezoneList.find((x) => x.name == timezone);

    const param = {
      logo: logo,
      serialKey: serialKey,
      factoryId: factoryId.toLowerCase(),
      factoryName: factoryName,
      userName: userName,
      userFullName: userFullName,
      password: password,
      timezone: selTimezone.utcOffset / 60,
      timezoneName: selTimezone.name,
    };
    const url = Urls.CREATE_NEW_FACTORY;
    setIsBusy(true);
    postRequest(url, param, (res) => {
      setIsBusy(false);
      if (res.status == false) {
        message.error(res.message);
      } else {
        message.success(lang(langIndex, "register_err5"));
        props.history.push(`${USER_SITE}/login`);
      }
    });
  };
  var d = new Date();
  var currentTimezone = d.getTimezoneOffset();

  const timezoneUIList = timezoneList.map((timezoneInfo, index) => {
    return (
      <Option key={index} value={timezoneInfo.name}>
        {timezoneInfo.name + "(" + timezoneInfo.utcOffsetStr + ")"}
      </Option>
    );
  });

  return (
    <div className="register-page-background">
      <div className="RegisterDialog">
        <Spin spinning={isBusy}>
          <div className="register-page-logo-container">
            <img className="register-page-logo-image" alt="logo" src={logo} />
            <h4 className="register-page-title">
              {lang(langIndex, "register_desc")}
            </h4>
          </div>
          <div style={{ border: "1px dashed white", margin: "10px 0px" }}>
            <Spin spinning={isUploading}>
              <label
                htmlFor={"factory-logo"}
                className="register-page-factory-logo-container"
              >
                <img
                  className="register-page-factory-logo"
                  src={
                    machineInfo.logo == undefined
                      ? blankFactory
                      : machineInfo.logo
                  }
                />
                <input
                  type="file"
                  style={{ display: "none" }}
                  id="factory-logo"
                  name="factory-logo"
                  accept="image/x-png,image/gif,image/jpeg"
                  onChange={onDropImage}
                />
              </label>
            </Spin>
          </div>
          <Row align={"middle"}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_serial_key")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(langIndex, "register_serial_key_placeholder")}
                style={{ background: "transparent", color: "#cccccc" }}
                onChange={(e) => {
                  setMachineInfo({ ...machineInfo, serialKey: e.target.value });
                }}
              />
            </Col>
          </Row>
          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_factory_id")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(langIndex, "register_factory_id_placeholder")}
                style={{ background: "transparent", color: "#cccccc" }}
                onChange={(e) => {
                  setMachineInfo({ ...machineInfo, factoryId: e.target.value });
                }}
              />
            </Col>
          </Row>

          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_factory_name")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(
                  langIndex,
                  "register_factory_name_placeholder"
                )}
                style={{ background: "transparent", color: "#cccccc" }}
                onChange={(e) => {
                  setMachineInfo({
                    ...machineInfo,
                    factoryName: e.target.value,
                  });
                }}
              />
            </Col>
          </Row>
          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_username")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(langIndex, "register_username_placeholder")}
                style={{ background: "transparent", color: "#cccccc" }}
                onChange={(e) => {
                  setMachineInfo({ ...machineInfo, userName: e.target.value });
                }}
              />
            </Col>
          </Row>
          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_fullname")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(langIndex, "register_fullname_placeholder")}
                style={{ background: "transparent", color: "#cccccc" }}
                onChange={(e) => {
                  setMachineInfo({
                    ...machineInfo,
                    userFullName: e.target.value,
                  });
                }}
              />
            </Col>
          </Row>

          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_password")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(langIndex, "register_password_placeholder")}
                style={{ background: "transparent", color: "#cccccc" }}
                type={"password"}
                onChange={(e) => {
                  setMachineInfo({ ...machineInfo, password: e.target.value });
                }}
              />
            </Col>
          </Row>
          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_confirm_password")}
            </Col>
            <Col xs={24} sm={16}>
              <Input
                placeholder={lang(
                  langIndex,
                  "register_confirm_password_placeholder"
                )}
                style={{ background: "transparent", color: "#cccccc" }}
                type={"password"}
                onChange={(e) => {
                  setMachineInfo({
                    ...machineInfo,
                    confirmPassword: e.target.value,
                  });
                }}
              />
            </Col>
          </Row>
          <Row align={"middle"} style={{ marginTop: 15 }}>
            <Col xs={24} sm={8} style={{ textAlign: "left", color: "#cccccc" }}>
              {lang(langIndex, "register_timezone")}
            </Col>
            <Col xs={24} sm={16}>
              <Select
                className={"register-page-timezone"}
                dropdownClassName={"register-page-timezone-list"}
                showSearch
                style={{ width: "100%" }}
                defaultValue={currentTimezoneName}
                filterOption={(input, option) =>
                  option.children.toLowerCase().indexOf(input.toLowerCase()) >=
                  0
                }
                onChange={(v) =>
                  setMachineInfo({ ...machineInfo, timezone: v })
                }
              >
                {timezoneUIList}
              </Select>
            </Col>
          </Row>

          <div style={{ marginTop: 20, textAlign: "center" }}>
            <Button
              ghost={true}
              onClick={onClickRegisterButton}
              className="register-dlg-button"
            >
              {lang(langIndex, "register_register")}
            </Button>
          </div>
          <div className="register-page-link-text" style={{ marginTop: 20 }}>
            {lang(langIndex, "register_has_account")}{" "}
            <Link to={`${USER_SITE}/login`}>
              {lang(langIndex, "register_login_here")}
            </Link>
          </div>
        </Spin>
      </div>
    </div>
  );
}

export default withRouter(RegisterPage);
