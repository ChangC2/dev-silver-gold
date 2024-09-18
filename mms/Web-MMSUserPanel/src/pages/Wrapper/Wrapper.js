import { Spin } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Switch, useHistory } from "react-router-dom";
import MyLayout from "../../components/Layout/MyLayout";
import { GetAllCustomerInfo } from "../../services/common/cnc_apis";
import { PAGETYPE } from "../../services/common/constants";
import { UpdateAppConfig } from "../../services/redux/reducers/app";
import CNCPages from "../CNCPages/CNCPages";
import SensorPage from "../sensorPage/sensor_page";
import "./Wrapper.css";

import CustomDashboard from "pages/CustomWidgetsPages/CustomDashboard";
import JobEntryPage from "pages/jobEntryPage/jobEntryPage";
import ReportPage from "pages/reportPage/ReportPage";
import Alarms from "pages/TMSPage/Alarms";
import { getCustomDashboards } from "services/common/cw_apis";
import CustomCNCPage from "../CustomCNCPage/CustomCNCPage";
import MaintenancePages from "../MaintenancePages/MaintenancePages";
import TMSPage from "../TMSPage/TMSPage";
import HennigPage from "pages/HennigPage/HennigPage";
import UtilizationDashboardPage from "../UtilizationDashboardPages/UtilizationDashboardPage";
import { USER_SITE } from "services/common/urls";

function Wrapper(props) {
  const dispatch = useDispatch();
  const history = useHistory();
  const authData = useSelector((x) => x.authService);
  const cncService = useSelector((x) => x.cncService);
  const appData = useSelector((x) => x.app);
  const { langIndex } = useSelector((x) => x.app);
  const [isLoading, setIsLoading] = useState(false);
  const screenSize = useWindowSize();

  if (authData.isAuth == false || authData.customer_id == undefined) {
    history.push(`${USER_SITE}/login`);
  }

  useEffect(() => {
    UpdateAppConfig(
      { screenSize: { width: screenSize.width, height: screenSize.height } },
      dispatch
    );
  }, [screenSize]);

  useEffect(() => {
    setIsLoading(true);
    GetAllCustomerInfo(authData.customer_id, dispatch, (res) => {
      if (res == false) {
        history.push(`${USER_SITE}/login`);
        return;
      }
      const customerIdList = Object.keys(res);
      let selCustomerId = appData.customer_id;
      if (
        selCustomerId == "" ||
        !customerIdList.includes(selCustomerId) ||
        isLoading
      ) {
        selCustomerId = customerIdList[0];
        UpdateAppConfig(
          { customer_id: selCustomerId, page_type: PAGETYPE.cnc },
          dispatch
        );
        return <div></div>;
      }
    });
  }, [authData.customer_id]);

  useEffect(() => {
    getCustomDashboards(dispatch, (res) => {
      setIsLoading(false);
    });
  }, [appData.customer_id]);

  const customerInfoList = cncService.customerInfoList;
  const customerIdList = Object.keys(customerInfoList);
  if (customerIdList.length == 0) {
    return <div></div>;
  }

  let selCustomerId = appData.customer_id;
  if (selCustomerId == "" || !customerIdList.includes(selCustomerId)) {
    return <div></div>;
  }
  return (
    <div>
      {isLoading && (
        <div style={{ paddingTop: 100, textAlign: "center" }}>
          <Spin tip="Loading ..." size="large" />
        </div>
      )}

      {!isLoading && (
        <MyLayout screenSize={screenSize}>
          <Switch>
            <Route
              exact
              path={`${USER_SITE}/pages/machine`}
              component={CNCPages}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/job_entry`}
              component={JobEntryPage}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/report`}
              component={ReportPage}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/machine/:customer_id/:machine_id`}
              component={CustomCNCPage}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/cw/:customer_id/:dashboard_id`}
              component={CustomDashboard}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/ud`}
              component={UtilizationDashboardPage}
            />
            <Route exact path={`${USER_SITE}/pages/tms`} component={TMSPage} />
            <Route
              exact
              path={`${USER_SITE}/pages/tms_alarm`}
              component={Alarms}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/iiot`}
              component={SensorPage}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/maintenance`}
              component={MaintenancePages}
            />

            <Route
              exact
              path={`${USER_SITE}/pages/hennig_alarm`}
              component={Alarms}
            />
            <Route
              exact
              path={`${USER_SITE}/pages/hennig_iot`}
              component={HennigPage}
            />
            {customerIdList.length === 1 && customerIdList[0] === "hennig" && (
              <Route exact path={`${USER_SITE}/pages`} component={HennigPage} />
            )}
            {!(
              customerIdList.length === 1 && customerIdList[0] === "hennig"
            ) && (
              <Route exact path={`${USER_SITE}/pages`} component={CNCPages} />
            )}
          </Switch>
        </MyLayout>
      )}
    </div>
  );
}

export default Wrapper;

// Hook
function useWindowSize() {
  // Initialize state with undefined width/height so server and client renders match
  // Learn more here: https://joshwcomeau.com/react/the-perils-of-rehydration/
  const [windowSize, setWindowSize] = useState({
    width: undefined,
    height: undefined,
  });

  useEffect(() => {
    // Handler to call on window resize
    function handleResize() {
      // Set window width/height to state
      setWindowSize({
        width: window.innerWidth,
        height: window.innerHeight,
      });
    }

    // Add event listener
    window.addEventListener("resize", handleResize);

    // Call handler right away so state gets updated with initial window size
    handleResize();

    // Remove event listener on cleanup
    return () => window.removeEventListener("resize", handleResize);
  }, []); // Empty array ensures that effect is only run on mount

  return windowSize;
}
