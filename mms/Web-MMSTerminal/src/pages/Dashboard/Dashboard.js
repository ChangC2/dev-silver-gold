// @flow strict

import { Col, Row, message } from "antd";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./Dashboard.css";

import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import IndicatorDots from "components/IndicatorDots";
import DashboardChartLayout from "layouts/DashboardChartLayout/DashboardChartLayout";
import DashboardMachineInfoLayout from "layouts/DashboardMachineInfoLayout/DashboardMachineInfoLayout";
import DashboardOEELayout from "layouts/DashboardOEELayout/DashboardOEELayout";
import DashboardUtilizationLayout from "layouts/DashboardUtilizationLayout/DashboardUtilizationLayout";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import Carousel from "re-carousel";
import { useEffect } from "react";
import { appData } from "services/global";
import { setAppDataStore } from "redux/actions/appActions";

function Dashboard(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;

  const [goodParts, setGoodParts] = useState("0");
  const [badParts, setBadParts] = useState("0");
  const [showGoodBadModal, setShowGoodBadModal] = useState(false);

  useEffect(() => {
    setGoodParts(appData.shiftGoodParts);
    setBadParts(appData.shiftBadParts);
  }, [appDataStore.shiftGoodParts, appDataStore.shiftBadParts]);

  const updateGoodParts = (value) =>{
    appData.shiftGoodParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  }

  const updateBadParts = (value) => {
    appData.shiftBadParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  return (
    <div
      className={
        pages[pages.length - 1] === 0 ? "dashboard-page" : "display-none"
      }
    >
      <GoodBadModal
        goodParts={goodParts}
        badParts={badParts}
        setGoodParts={updateGoodParts}
        setBadParts={updateBadParts}
        showModal={showGoodBadModal}
        setShowModal={setShowGoodBadModal}
      />
      <div className="dashboard-content-left">
        <UserInfoLayout />
        <DashboardMachineInfoLayout />
        <div className="dashboard-content-left-good-bad">
          <Row>
            <Col span={12}>
              <GoodBadWidget
                type={0}
                value={goodParts}
                setValue={updateGoodParts}
                setShowModal={setShowGoodBadModal}
              />
            </Col>
            <Col span={12}>
              <GoodBadWidget
                type={1}
                value={badParts}
                setValue={updateBadParts}
                setShowModal={setShowGoodBadModal}
              />
            </Col>
          </Row>
        </div>
      </div>
      <div className="dashboard-content-right">
        <Carousel auto widgets={[IndicatorDots]}>
          <div style={{ height: "100%" }}>
            <DashboardOEELayout />
          </div>
          <div style={{ height: "100%" }}>
            <DashboardUtilizationLayout />
          </div>
        </Carousel>
      </div>
      <DashboardChartLayout />
    </div>
  );
}

export default Dashboard;
