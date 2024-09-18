import BottomLayout from "layouts/BottomLayout/BottomLayout";
import ContentLayout from "layouts/ContentLayout/ContentLayout";
import TopLayout from "layouts/TopLayout/TopLayout";
import Dashboard from "pages/Dashboard/Dashboard";
import InstallConfig from "pages/InstallConfig/InstallConfig";
import Maintenance from "pages/Maintenance/Maintenance";
import ProcessMonitor from "pages/ProcessMonitor/ProcessMonitor";
import Settings from "pages/Settings/Settings";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { setUserDataStore } from "redux/actions/userActions";
import { setFactoryDataStore } from "redux/actions/factoryActions";
import { LS_ITEMS } from "services/CONSTANTS";
import {
  appData,
  getLSJson,
  setAppData,
  factoryData,
  setFactoryData,
  setUserData,
  userData,
} from "services/global";
import "./MainLayout.css";
import MachineStatus from "pages/MachineStatus/MachineStatus";

const MainLayout = (props) => {
  const dispatch = useDispatch();

  const { appDataStore } = useSelector((x) => x.appDataStore);
  //const { userDataStore } = useSelector((x) => x.userDataStore);

  //console.log("appDataStore=>", appDataStore);
  //console.log("appData=>", appData);

  //console.log("userDataStore=>", userDataStore);
  //console.log("userData=>", userData);

  useEffect(() => {
    loadLSUserData();
    loadLSFactoryData();
    loadLSAppData();
  }, []);

  // Load User Data
  const loadLSUserData = () => {
    setUserData(getLSJson(LS_ITEMS.userData));
    dispatch(setUserDataStore(userData));
  };

  // Load Factory Data
  const loadLSFactoryData = () => {
    setFactoryData(getLSJson(LS_ITEMS.factoryData));
    dispatch(setFactoryDataStore(factoryData));
  };

  // Load App Data
  const loadLSAppData = () => {
    setAppData(getLSJson(LS_ITEMS.appData));
    dispatch(setAppDataStore(appData));
  };

  return (
    <div className="main-layout">
      <TopLayout />
      <ContentLayout>
        <Dashboard />
        <ProcessMonitor />
        {appDataStore.pages[appDataStore.pages.length - 1] === 6 && (
          <MachineStatus />
        )}
        <Settings />
        <InstallConfig />
        <Maintenance />
      </ContentLayout>
      <BottomLayout />
    </div>
  );
};

export default MainLayout;
