import { FullscreenExitOutlined } from "@ant-design/icons";
import { Layout } from "antd";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { withRouter } from "react-router";
import { Fab } from "react-tiny-fab";
import "react-tiny-fab/dist/styles.css";
import { sizePad } from "../../services/common/constants";
import { UpdateAppConfig } from "../../services/redux/reducers/app";
import MyHeader from "./MyHeader";
import "./MyLayout.css";
import MySidebar from "./MySidebar";

import MyFooter from "./MyFooter";

function MyLayout(props) {
  const dispatch = useDispatch();
  const authData = useSelector((x) => x.authService);
  const appData = useSelector((x) => x.app);
  const cncService = useSelector((x) => x.cncService);
  const cwService = useSelector((x) => x.cwService);
  const { screenSize } = appData;

  const { username, security_level, fullname, avatar } = authData;
  const [autoScrollSpeed, setAutoScrollSpeed] = useState(50);

  const [isFullScreen, setIsFullScreen] = useState(false);

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const [dy, setDy] = useState(10);
  const [isWaitingToTop, setIsWaitingToTop] = useState(false);
  const fixedInterval = 50;

  // sync with redux
  const customerInfoList = cncService.customerInfoList;
  const customerIdList = Object.keys(customerInfoList);
  const customDashboardList = cwService.cwDashboards;


  const isMenuCollapsed = appData.is_menu_collapsed;
  const selCustomerId = appData.customer_id;
  const selMenuType = appData.y;
  const customerInfo = customerInfoList[selCustomerId];

  const viewMode = appData.viewMode;
  const autoScroll = appData.isAutoScroll;

  const setIsMenuCollapsed = (val) => {
    UpdateAppConfig({ is_menu_collapsed: val }, dispatch);
  };

  const setSelMenuType = (val) => {
    UpdateAppConfig({ page_type: val }, dispatch);
  };

  const setViewMode = (val) => {
    UpdateAppConfig({ viewMode: val }, dispatch);
  };

  const setAutoScroll = (val) => {
    UpdateAppConfig({ isAutoScroll: val }, dispatch);
  };

  // section for scrolling
  useEffect(() => {
    return () => {
      if (intervalId != null) clearInterval(intervalId);
    };
  }, []);

  // totally 30 seconds
  const myRef = useRef(null);
  useEffect(() => {
    myRef.current.scrollTop = 0;

    if (intervalId != null) clearInterval(intervalId);
    if (autoScroll == true) {
      var height =
        myRef.current.scrollHeight - myRef.current.offsetHeight + 0.0;
      var duration = 31 - autoScrollSpeed * 0.3 + 0.0; // seconds
      let _dy = height / (20 * duration);
      setDy(_dy);
      setIntervalID(setInterval(timer, fixedInterval));
    }
  }, [autoScroll, autoScrollSpeed]);

  const timer = () => {
    setTick((t) => !t);
  };

  useEffect(() => {
    if (
      myRef.current.scrollTop >
      myRef.current.scrollHeight - myRef.current.offsetHeight - dy &&
      autoScroll == true
    ) {
      myRef.current.scrollTop = myRef.current.scrollHeight;
      setIsWaitingToTop(true);
      clearInterval(intervalId);
      setTimeout(function () {
        myRef.current.scrollTo({ behavior: "smooth", top: 0 });
        setTimeout(function () {
          setIntervalID(setInterval(timer, fixedInterval));
          setIsWaitingToTop(false);
        }, 1000);
      }, 500);
    } else {
      myRef.current.scrollTop = myRef.current.scrollTop + dy;
    }
  }, [tick]);

  return (
    <Layout>
      {!isFullScreen && (
        <MySidebar
          isMenuCollapsed={isMenuCollapsed}
          setIsMenuCollapsed={setIsMenuCollapsed}
          selCustomerId={selCustomerId}
          customerInfo={customerInfo}
          customerIdList={customerIdList}
          customDashboardList={customDashboardList}
          customerInfoList={customerInfoList}
          setAutoScroll={setAutoScroll}
          screenSize={screenSize}
          security_level={security_level}
        />
      )}

      <Layout>
        {!isFullScreen && (
          <MyHeader
            customerInfo={customerInfo}
            viewMode={viewMode}
            setViewMode={setViewMode}
            setIsFullScreen={setIsFullScreen}
            isMenuCollapsed={isMenuCollapsed}
            setIsMenuCollapsed={setIsMenuCollapsed}
            selCustomerId={selCustomerId}
            setAutoScroll={setAutoScroll}
            screenSize={screenSize}
            security_level={security_level}
            avatar={avatar}
            fullname={fullname}
            setSelMenuType={setSelMenuType}
            username={username}
            selMenuType={selMenuType}
          />
        )}
        {screenSize.width >= sizePad ? (
          <div
            className="content-scrollbar-style"
            ref={myRef}
            style={{
              height: !isFullScreen
                ? window.innerHeight - 140
                : window.innerHeight - 50,
              overflow: "auto",
              overflowX: "hidden",
              margin: "10px 16px",
            }}
          >
            {props.children}
          </div>
        ) : (
          <div
            className="content-scrollbar-style"
            ref={myRef}
            style={{
              height: !isFullScreen
                ? window.innerHeight - 180
                : window.innerHeight - 50,
              overflow: "auto",
              overflowX: "hidden",
              margin: "10px 16px",
            }}
          >
            {props.children}
          </div>
        )}

        <MyFooter
          screenSize={screenSize}
          customerInfo={customerInfo}
          isMenuCollapsed={isMenuCollapsed}
          isFullScreen={isFullScreen}
          autoScroll={autoScroll}
          setAutoScroll={setAutoScroll}
          autoScrollSpeed={autoScrollSpeed}
          setAutoScrollSpeed={setAutoScrollSpeed}
          isWaitingToTop={isWaitingToTop}
        />
      </Layout>

      {isFullScreen && (
        <Fab
          position={{ bottom: 24, right: 24 }}
          icon={<FullscreenExitOutlined />}
          onClick={() => setIsFullScreen(false)}
        ></Fab>
      )}
    </Layout>
  );
}

export default withRouter(MyLayout);
