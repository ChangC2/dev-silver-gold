import { Button, Spin } from "antd";
import { useEffect, useState } from "react";
import { connect, useDispatch, useSelector } from "react-redux";
import {
  getCustomDashboardTableData,
  getCustomDashboardStage,
  GetCustomerCurrentTime,
  GetMachineListData,
  getPartId,
  getTackReport,
  readOperatorList,
} from "../../services/common/cnc_apis";

import moment from "moment";
import { CSSTransition } from "react-transition-group";
import { UpdateAppConfig } from "../../services/redux/reducers/app";
import MachineDetailPage from "../CNCPages/machineDetailPage/MachineDetailPage";
import MachineItem from "../CNCPages/MainPage/MachineItem_Grid/MachineItem";
import "./CustomCNCPage.css";
import MachineHstGraphWidget from "./MachineHstGraphWidget/MachineHstGraphWidget";
import { showCustomCNCPDPreviewModal } from "./PreviewWidgets/showCustomCNCPDFPreviewModal";
import AssemblyStation1TableWidget from "./TableWidget/AssemblyStation1TableWidget";
import AssemblyStation3TableWidget from "./TableWidget/AssemblyStation3TableWidget";
import BlastStationTableWidget from "./TableWidget/BlastStationTableWidget";
import CleaningStaionTableWidget from "./TableWidget/CleaningStationTableWidget";
import PaintStationTableWidget from "./TableWidget/PaintStationTableWidget";
import TankTimesTableWidget from "./TableWidget/TankTimesTableWidget";
import StagesTableWidget from "./TableWidget/StagesTableWidget";
import PanelsTableWidget from "./TableWidget/PanelsTableWidget";
import BlastTrendChartWidget from "./TrendChartWidget/BlastTrendChartWidget";
import PaintTrendChartWidget from "./TrendChartWidget/PaintTrendChartWidget";
import SensorTrendChartWidget from "./TrendChartWidget/SensorTrendChartWidget";
import Blu136_assemblyTableWidget from "./TableWidget/Blu136_assemblyTableWidget";
import QaulityStationTableWidget from "./TableWidget/QaulityStationTableWidget";

const CustomCNCPage = (props) => {
  const dispatch = useDispatch();
  const appData = useSelector((state) => state.app);
  const authData = useSelector((x) => x.authService);
  const { customer_id, machine_id } = props.match.params;
  const { security_level } = authData;
  const [operatorList, setOperatorList] = useState([]);
  const cncService = useSelector((x) => x.cncService);
  const customerInfo = cncService.customerInfoList[customer_id];
  const { screenSize } = appData;
  const [machineId, setMachineId] = useState("");
  const [menuId, setMenuId] = useState("");
  const [partId, setPartId] = useState();
  const [partIds, setPartIds] = useState([]);

  const [tanktimes, setTanktimes] = useState([]);
  const [stages, setStages] = useState([]);
  const [panels, setPanels] = useState([]);
  const [dataSource, setDataSource] = useState([]);
  const [columns, setColumns] = useState([]);

  const [blastStation, setBlastStation] = useState([]);
  const [cleaningStation, setCleaningStation] = useState([]);
  const [cleaningStation2, setCleaningStation2] = useState([]);
  const [paintStation, setPaintStation] = useState([]);
  const [assemblyStation1, setAssemblyStation1] = useState([]);
  const [assemblyStation12, setAssemblyStation12] = useState([]);
  const [assemblyStation3, setAssemblyStation3] = useState([]);
  const [blu136_assembly, setBlu136_assembly] = useState([]);
  const [blu136_assembly2, setBlu136_assembly2] = useState([]);
  const [blu136_assembly3, setBlu136_assembly3] = useState([]);
  const [blu136_assembly4, setBlu136_assembly4] = useState([]);
  const [qualityStation, setQualityStation] = useState([]);

  const [daysoftimes, setDaysOfTimes] = useState(7);
  const [daysofstages, setDaysOfStages] = useState(7);
  const [daysofpanels, setDaysOfPanels] = useState(7);
  const [daysofhst, setDaysOfHst] = useState(30);
  const [daysoftemp, setDaysOfTemp] = useState(7);
  const [selectedMachine, setSelectedmachine] = useState("");
  const [chartData, setChartData] = useState();
  const [chartHstData, setChartHstData] = useState();
  const [step, setStep] = useState(0);
  const [hstData, setHstData] = useState();
  const [period, setPeriod] = useState();
  const [showSpin, setShowSpin] = useState(false);

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const intervalTime = 60000;

  const formattedNumber = (myNumber) => ("0" + myNumber).slice(-2);
  const getTimeFromSecond = (second) => {
    second = parseInt(second);
    if (isNaN(second)) return "00:00:00";

    var hr = Math.floor(second / 1000 / 3600);
    var min = Math.floor((second % 3600) / 60);
    var sec = second % 60;

    return `${formattedNumber(hr)}:${formattedNumber(min)}:${formattedNumber(
      sec
    )}`;
  };

  const onSetAutoScroll = (val) => {
    UpdateAppConfig({ isAutoScroll: val }, props.dispatch);
  };

  const onClickMachine = (machine) => {
    onSetAutoScroll(false);
    setSelectedmachine(machine);
  };

  useEffect(() => {
    if (customerInfo == undefined) return;

    try {
      clearInterval(intervalId);
    } catch (_) {}

    readOperatorList((res) => {
      setOperatorList(res);
      //readMachineGanttData();
      setIntervalID(setInterval(timer, intervalTime));
    });

    return () => {
      clearInterval(intervalId);
    };
  }, [customerInfo]);

  useEffect(() => {
    readMachineGanttData();
  }, [tick]);

  const timer = () => {
    setTick((t) => !t);
  };

  useEffect(() => {
    getPartId(customer_id, (res) => {
      const { data, status, message } = res;
      if (status == false) {
        message.error(message);
        return;
      }
      setPartIds(data);
    });
  }, []);

  useEffect(() => {
    setSelectedmachine("");
    setPartId(partIds[machineId]);
    readMachineGanttData();
  }, [partIds, machineId]);

  useEffect(() => {
    getCustomDashboardTableData(customer_id, daysoftimes, (res) => {
      if (res == null) {
        return;
      }
      const { data, status, message } = res;

      setTanktimes(data["tank_times"]);
      setBlastStation(data["blast_station"]);
      setPaintStation(data["paint_station"]);

      setCleaningStation(data["cleaning_station"]);

      let cStation1 = [];
      let cStation2 = [];

      data["cleaning_station"].forEach((item) => {
        let row = { ...item };
        if (item["machine_id"] === "Cleaning Station") {
          cStation1.push(row);
        } else if (item["machine_id"] === "Cleaning Station 2") {
          cStation2.push(row);
        }
      });
      setCleaningStation(cStation1);
      setCleaningStation2(cStation2);

      let assembly_station1 = [];
      let assembly_station12 = [];

      data["assembly_station1"].forEach((item) => {
        let row = { ...item };
        row["processing_time"] = getTimeFromSecond(item["processing_time"]);
        if (item["machine_id"] === "Assembly Blu/137")
          assembly_station1.push(row);
        else if (item["machine_id"] === "Assembly Blu/137-2")
          assembly_station12.push(row);
      });
      setAssemblyStation1(assembly_station1);
      setAssemblyStation12(assembly_station12);

      let assembly_station3 = data["assembly_station3"].map((x) => {
        let row = { ...x };
        row["processing_time"] = getTimeFromSecond(x["processing_time"]);
        return { ...row };
      });
      setAssemblyStation3(assembly_station3);

      let blu136_assembly = [];
      let blu136_assembly2 = [];
      let blu136_assembly3 = [];
      let blu136_assembly4 = [];

      data["blu136_assembly"].forEach((item) => {
        let row = { ...item };
        row["processing_time"] = getTimeFromSecond(item["processing_time"]);
        if (item["machine_id"] === "Assembly Blu/136")
          blu136_assembly.push(row);
        else if (item["machine_id"] === "Assembly Blu/136-2")
          blu136_assembly2.push(row);
        else if (item["machine_id"] === "Assembly Blu/136-3")
          blu136_assembly3.push(row);
        else if (item["machine_id"] === "Assembly Blu/136-4")
          blu136_assembly4.push(row);
      });
      setBlu136_assembly(blu136_assembly);
      setBlu136_assembly2(blu136_assembly2);
      setBlu136_assembly3(blu136_assembly3);
      setBlu136_assembly4(blu136_assembly4);

      let quality_station = data["quality_station"].map((x) => {
        let row = { ...x };
        row["processing_time"] = getTimeFromSecond(x["processing_time"]);
        return { ...row };
      });
      setQualityStation(quality_station);
    });
  }, [daysoftimes]);

  useEffect(() => {
    if (
      machine_id === "Phosphate Line 1" ||
      machine_id === "Phosphate Line 2"
    ) {
      getCustomDashboardStage(customer_id, machine_id, daysofstages, (res) => {
        if (res == null) {
          return;
        }
        setStages(res["data"]);
      });
    }
  }, [daysofstages, machine_id]);

  useEffect(() => {
    if (
      machine_id === "Phosphate Line 1" ||
      machine_id === "Phosphate Line 2"
    ) {
      getCustomDashboardStage(customer_id, machine_id, daysofpanels, (res) => {
        if (res == null) {
          return;
        }
        setPanels(res["data"]);
      });
    }
  }, [daysofpanels, machine_id]);

  useEffect(() => {
    console.log(machine_id);
    setMenuId(machine_id);
    if (machine_id === "Assembly Blu_137") {
      setMachineId("Assembly Blu/137");
    } else if (machine_id === "Assembly Blu_137_2") {
      setMachineId("Assembly Blu/137-2");
    } else if (machine_id == "Assembly Blu_136") {
      setMachineId("Assembly Blu/136");
    } else if (machine_id == "Assembly Blu_136_2") {
      setMachineId("Assembly Blu/136-2");
    } else if (machine_id == "Assembly Blu_136_3") {
      setMachineId("Assembly Blu/136-3");
    } else if (machine_id == "Assembly Blu_136_4") {
      setMachineId("Assembly Blu/136-4");
    } else {
      setMachineId(machine_id);
    }
  }, [machine_id]);

  const readMachineGanttData = () => {
    const timeZone = customerInfo["timezone"];
    const customerCurrentTime = GetCustomerCurrentTime(timeZone);

    const startDate = moment(customerCurrentTime).format("MM/DD/YYYY");
    const endDate = moment(customerCurrentTime)
      .add(1, "days")
      .format("MM/DD/YYYY");

    GetMachineListData(
      customer_id,
      timeZone,
      startDate,
      endDate,
      machineId,
      dispatch
    );
  };

  const machineList = cncService.machineDetailList[customer_id];
  const machineInfo =
    machineList == null || machineList == undefined
      ? undefined
      : machineList.find((x) => x["machine_id"] == machineId);

  const onClickCreatePDF = async () => {
    const dlgResult = await showCustomCNCPDPreviewModal({
      daysoftimes: daysoftimes,
      daysofhst: daysofhst,
      daysoftemp: daysoftemp,
      logo: customerInfo.logo,
      customer_id: customer_id,
      machine_id: machineId,
      customerInfo: customerInfo,
      chartData: chartData,
      chartHstData: chartHstData,
      columns: columns,
      dataSource: dataSource,
      step: step,
    });

    if (dlgResult != false) {
      setShowSpin(true);
      getTackReport(
        dlgResult["logoUrl"],
        dlgResult["hstUrl"],
        daysofhst,
        dlgResult["tempUrl"],
        daysoftemp,
        tanktimes,
        daysoftimes,
        (res) => {
          setShowSpin(false);
          if (res == null) {
            return;
          }
          window.open(res);
        }
      );
    }
  };

  if ((customerInfo == undefined || machineInfo) == undefined) {
    return (
      <div style={{ textAlign: "center", paddingTop: 100 }}>
        {" "}
        <Spin tip="Loading ..." size="large" />
      </div>
    );
  }

  return (
    <div>
      {selectedMachine == "" && (
        <div>
          <MachineItem
            machineInfo={machineInfo == undefined ? {} : machineInfo}
            customer_id={customer_id}
            customerInfo={customerInfo}
            onClickMachine={onClickMachine}
            operatorList={operatorList}
            screenSize={screenSize}
            partId={partId}
          />

          <div className="create-pdf-container">
            <Button
              type={"primary"}
              onClick={() => onClickCreatePDF()}
              className="create-pdf-button"
            >
              Create PDF
              {showSpin && <Spin style={{ marginLeft: 10, marginRight: 10 }} />}
            </Button>
          </div>

          {machineId === "Phosphate Line 1" && (
            <TankTimesTableWidget
              tanktimes={tanktimes}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Phosphate Line 2" && (
            <TankTimesTableWidget
              tanktimes={tanktimes}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Cleaning Station" && (
            <CleaningStaionTableWidget
              cleaningStation={cleaningStation}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Cleaning Station 2" && (
            <CleaningStaionTableWidget
              cleaningStation={cleaningStation2}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Blast Booth" && (
            <BlastStationTableWidget
              blastStation={blastStation}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Paint Booth" && (
            <PaintStationTableWidget
              paintStation={paintStation}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_137" && (
            <AssemblyStation1TableWidget
              assemblyStation1={assemblyStation1}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_137_2" && (
            <AssemblyStation1TableWidget
              assemblyStation1={assemblyStation12}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_136" && (
            <Blu136_assemblyTableWidget
              blu136_assembly={blu136_assembly}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_136_2" && (
            <Blu136_assemblyTableWidget
              blu136_assembly={blu136_assembly2}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_136_3" && (
            <Blu136_assemblyTableWidget
              blu136_assembly={blu136_assembly3}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {menuId === "Assembly Blu_136_4" && (
            <Blu136_assemblyTableWidget
              blu136_assembly={blu136_assembly4}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Assembly Sub" && (
            <AssemblyStation3TableWidget
              assemblyStation3={assemblyStation3}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          {machineId === "Quality Control" && (
            <QaulityStationTableWidget
              qualityStation={qualityStation}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysoftimes={daysoftimes}
              setDaysOfTimes={setDaysOfTimes}
            />
          )}

          <MachineHstGraphWidget
            machine_id={machineId}
            customer_id={customer_id}
            customerInfo={customerInfo}
            daysofhst={daysofhst}
            setDaysOfHst={setDaysOfHst}
            hstData={hstData}
            setHstData={setHstData}
            period={period}
            setPeriod={setPeriod}
            chartData={chartHstData}
            setChartData={setChartHstData}
          />

          {machineId === "Blast Booth" && (
            <BlastTrendChartWidget
              customer_id={customer_id}
              customerInfo={customerInfo}
              daysoftemp={daysoftemp}
              setDaysOfTemp={setDaysOfTemp}
              chartData={chartData}
              setChartData={setChartData}
              step={step}
              setStep={setStep}
            />
          )}

          {machineId === "Paint Booth" && (
            <PaintTrendChartWidget
              customer_id={customer_id}
              customerInfo={customerInfo}
              daysoftemp={daysoftemp}
              setDaysOfTemp={setDaysOfTemp}
              chartData={chartData}
              setChartData={setChartData}
              step={step}
              setStep={setStep}
            />
          )}

          {machineId === "Phosphate Line 1" && (
            <SensorTrendChartWidget
              customer_id={customer_id}
              customerInfo={customerInfo}
              daysoftemp={daysoftemp}
              setDaysOfTemp={setDaysOfTemp}
              chartData={chartData}
              setChartData={setChartData}
              step={step}
              setStep={setStep}
            />
          )}

          {machineId === "Phosphate Line 2" && (
            <SensorTrendChartWidget
              customer_id={customer_id}
              customerInfo={customerInfo}
              daysoftemp={daysoftemp}
              setDaysOfTemp={setDaysOfTemp}
              chartData={chartData}
              setChartData={setChartData}
              step={step}
              setStep={setStep}
            />
          )}

          {machineId === "Phosphate Line 1" && (
            <StagesTableWidget
              stages={stages}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysofstages={daysofstages}
              setDaysOfStages={setDaysOfStages}
            />
          )}

          {machineId === "Phosphate Line 2" && (
            <StagesTableWidget
              stages={stages}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysofstages={daysofstages}
              setDaysOfStages={setDaysOfStages}
            />
          )}

          {machineId === "Phosphate Line 1" && (
            <PanelsTableWidget
              panels={panels}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysofpanels={daysofpanels}
              setDaysOfPanels={setDaysOfPanels}
            />
          )}

          {machineId === "Phosphate Line 2" && (
            <PanelsTableWidget
              panels={panels}
              setPreviewDataSource={setDataSource}
              setPreviewColumns={setColumns}
              daysofpanels={daysofpanels}
              setDaysOfPanels={setDaysOfPanels}
            />
          )}
        </div>
      )}

      <CSSTransition
        in={selectedMachine !== ""}
        timeout={1000}
        classNames="pageSliderLeft"
        unmountOnExit={true}
      >
        <MachineDetailPage
          customer_id={customer_id}
          customerInfo={customerInfo}
          machineInfo={selectedMachine}
          security_level={security_level}
          onCloseMachine={setSelectedmachine}
          operatorList={operatorList}
          screenSize={screenSize}
          dismissible
        />
      </CSSTransition>
    </div>
  );
};

const mapStateToProps = (state, props) => ({
  cncService: state.cncService,
  app: state.app,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(CustomCNCPage);
