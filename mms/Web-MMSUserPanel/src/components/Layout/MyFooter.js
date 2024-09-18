import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Button, Switch, Slider, Row, Col } from "antd";

import { MailOutlined } from "@ant-design/icons";

import {
  pad,
  GetCustomerCurrentTime,
} from "../../services/common/cnc_apis";
import { sizePad } from "../../services/common/constants";

import lang from "../../services/lang";
import "./MyFooter.css";

import FeatureModal from "./FeatureModal/FeatureModal";

import logo from "../../assets/images/logo.png";

function MyFooter(props) {
  const { langIndex } = useSelector((x) => x.app);

  const {
    screenSize,
    autoScroll,
    setAutoScroll,
    autoScrollSpeed,
    setAutoScrollSpeed,
    isWaitingToTop,
    isMenuCollapsed,
    isFullScreen,
  } = props;

  const { customerInfo } = props;

  const [currentTime, setCurrentTime] = useState("");
  const [customerTime, setCustomerTime] = useState("");
  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);

  useEffect(() => {
    setIntervalID(setInterval(timer, 1000));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const timer = () => {
    setTick((t) => !t);
  };

  useEffect(() => {
    const timezone = customerInfo["timezone"];
    let _cusTime = GetCustomerCurrentTime(timezone);
    let _curTime = new Date();
    setCustomerTime(_cusTime.toLocaleString());
    setCurrentTime(_curTime.toLocaleString());
  }, [tick]);

  return screenSize.width >= sizePad ? (
    <div className="footer-background">
      <div style={{ flex: "1 1 0%" }}>
        {(isMenuCollapsed || isFullScreen) && (
          <img
            className="bottom-customer-logo"
            alt="logo"
            src={logo}
            style={{ height: 30, marginTop: 10 }}
          />
        )}
      </div>
      <div className="div-autoscroll">
        <span className="txt-bottom-autoscroll-label">
          {lang(langIndex, "sidebar_auto_scroll")}
        </span>
        <Switch
          className="switch-style"
          checkedChildren="On"
          unCheckedChildren="Off"
          onChange={setAutoScroll}
          checked={autoScroll}
          disabled={isWaitingToTop}
        />
        <span className="txt-bottom-autoscroll-label">
          {lang(langIndex, "sidebar_scroll_speed")}
        </span>
      </div>
      <div className="div-slider">
        <Slider
          className="slider-bottom"
          defaultValue={autoScrollSpeed}
          disabled={!autoScroll || isWaitingToTop}
          onAfterChange={setAutoScrollSpeed}
        />
      </div>
      <div className="bottom-clock-bg">
        <p className="txt-clock">
          {lang(langIndex, "breadcrumb_current_time")}: {currentTime}
        </p>
        <p className="txt-clock">
          {lang(langIndex, "breadcrumb_factory_time")}: {customerTime}
        </p>
      </div>
    </div>
  ) : (
    <Row align="center">
      <Col>
        <div className="footer-background">
          <div className="div-autoscroll">
            <span className="txt-bottom-autoscroll-label">
              {lang(langIndex, "sidebar_auto_scroll")}
            </span>
            <Switch
              className="switch-style"
              checkedChildren="On"
              unCheckedChildren="Off"
              onChange={setAutoScroll}
              checked={autoScroll}
              disabled={isWaitingToTop}
            />
            <span className="txt-bottom-autoscroll-label">
              {lang(langIndex, "sidebar_scroll_speed")}
            </span>
          </div>
          <div className="div-slider">
            <Slider
              className="slider-bottom"
              defaultValue={autoScrollSpeed}
              disabled={!autoScroll || isWaitingToTop}
              onAfterChange={setAutoScrollSpeed}
            />
          </div>
        </div>
      </Col>
    </Row>
  );
}

export default MyFooter;
