// @flow strict

import { useSelector } from "react-redux";

import AssemblyStation136Layout from "layouts/AssemblyStation136Layout/AssemblyStation136Layout";
import AssemblyStation137Layout from "layouts/AssemblyStation137Layout/AssemblyStation137Layout";
import AssemblyStation3Layout from "layouts/AssemblyStation3Layout/AssemblyStation3Layout";
import BlastStationLayout from "layouts/BlastStationLayout/BlastStationLayout";
import CleaningStationLayout from "layouts/CleaningStationLayout/CleaningStationLayout";
import PaintStationLayout from "layouts/PaintStationLayout/PaintStationLayout";
import QualityLayout from "layouts/QualityLayout/QualityLayout";
import TimeLoggerLayout from "layouts/TimeLoggerLayout/TimeLoggerLayout";
import "./ProcessMonitor.css";

function ProcessMonitor(props) {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { process, pages } = appDataStore;

  return (
    <div
      className={
        pages[pages.length - 1] === 1 ? "process-monitor-page" : "display-none"
      }
    >
      {process === 0 && (
        <div className="process-monitor-none">
          {"Please choose Process Monitor Type in the Settings menu."}
        </div>
      )}
      {process === 1 && <TimeLoggerLayout />}
      {process === 2 && <CleaningStationLayout />}
      {process === 3 && <BlastStationLayout />}
      {process === 4 && <PaintStationLayout />}
      {process === 5 && <AssemblyStation137Layout />}
      {process === 6 && <AssemblyStation136Layout />}
      {process === 7 && <AssemblyStation3Layout />}
      {process === 8 && <QualityLayout />}
    </div>
  );
}

export default ProcessMonitor;
