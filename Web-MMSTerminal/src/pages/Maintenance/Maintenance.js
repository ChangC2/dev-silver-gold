// @flow strict

import { useSelector } from "react-redux";
import "./Maintenance.css";

import MaintenanceTopLayout from "layouts/MaintenanceTopLayout/MaintenanceTopLayout";

function Maintenance(props) {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;
  return (
    <div
      className={
        pages[pages.length - 1] === 4 ? "maintenance-page" : "display-none"
      }
    >
      <MaintenanceTopLayout />
    </div>
  );
}

export default Maintenance;
