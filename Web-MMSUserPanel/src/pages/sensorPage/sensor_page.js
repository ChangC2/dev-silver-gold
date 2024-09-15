import { Spin } from "antd";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import { CSSTransition } from "react-transition-group";
import { deleteSensor, getSensorList } from "../../services/common/sensor_apis";
import AddSensorModal from "./AddSensorModal/AddSensorModal";
import DetailPage from "./detail_page/detail_page";
import SensorDashboardPage from "./SensorDashboardPage/SensorDashboardPage";
import "./sensor_page.css";

function SensorPage(props) {

  const [isShowAddModal, setIsShowAddModal] = useState(false);
  const [selectedSensorId, setSelectedSensorId] = useState("");
  const [customerInfo, setCustomerInfo] = useState({});
  const customerId = props.app.customer_id;
  const [isLoading, setIsLoading] = useState(true);

  const { screenSize } = props.app;

  const onDeleteSensor = (sensorId) => {
    deleteSensor(customerId, sensorId, props.dispatch);
  };

  useEffect(() => {
    setSelectedSensorId("");
    setIsLoading(true);
    getSensorList(customerId, props.dispatch, (res) => {
      setIsLoading(false);
    });

    if (customerId !== undefined || customerId !== "") {
      var customer = props.cncService.customerInfoList[customerId];
      if (customer !== undefined) {
        setCustomerInfo(customer);
      }
    }
  }, [customerId]);

  const sensorList = props.sensorService.sensorList[customerId];

  const selectedSensorInfo =
    selectedSensorId === "" || sensorList === undefined
      ? ""
      : sensorList.filter((x) => x.sensor_id === selectedSensorId)[0];

  useEffect(() => {
    if (selectedSensorInfo === undefined) {
      setSelectedSensorId("");
    }
  }, [selectedSensorInfo]);
  return (
    <div className="sensor-page-background">
      {isShowAddModal && (
        <AddSensorModal
          isShowAddModal={isShowAddModal}
          setIsShowAddModal={setIsShowAddModal}
        />
      )}

      {isLoading && (
        <div
          style={{ paddingTop: 100, textAlign: "center" }}
        >
          <Spin tip="Loading ..." size="large" />
        </div>
      )}

      {!isLoading && (
        <div style={{ margin: 15 }}>
          {(selectedSensorId === "" || selectedSensorInfo === undefined) && (
            <SensorDashboardPage
              customerId={customerId}
              setIsShowAddModal={setIsShowAddModal}
              sensorList={sensorList}
              selectedSensorId={selectedSensorId}
              setSelectedSensorId={setSelectedSensorId}
              timezone={customerInfo.timezone}
            />
          )}

          <CSSTransition
            in={selectedSensorId !== "" && selectedSensorInfo !== undefined}
            timeout={1000}
            classNames="pageSliderLeft"
            unmountOnExit={true}
          >
            <div>
              {selectedSensorId !== "" && selectedSensorInfo !== undefined && (
                <DetailPage
                  screenSize={screenSize}
                  setSelectedSensorId={setSelectedSensorId}
                  sensorInfo={selectedSensorInfo}
                  onDeleteSensor={onDeleteSensor}
                  timezone={customerInfo.timezone}
                />
              )}
            </div>
          </CSSTransition>
        </div>
      )}
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  app: state.app,
  sensorService: state.sensorService,
  cncService: state.cncService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(SensorPage);
