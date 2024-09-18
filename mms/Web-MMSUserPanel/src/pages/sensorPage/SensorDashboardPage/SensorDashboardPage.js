import { Button, Col, Row } from "antd";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import { getSensorInfo } from "../../../services/common/sensor_apis";
import OneItem from "../one_item/one_item";
import "./SensorDashboardPage.css";
function SensorDashboardPage(props) {
  const { customerId, setIsShowAddModal, sensorList } = props;
  const { setSelectedSensorId, timezone } = props;

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const intervalTime = 60000;

  useEffect(() => {
    try {
      clearInterval(intervalId);
    } catch (e) {}
    readSensorInfo();
    setIntervalID(setInterval(timer, intervalTime));
    return () => {
      clearInterval(intervalId);
    };
  }, [customerId, sensorList]);

  const timer = () => {
    setTick((t) => !t);
  };

  useEffect(() => {
    readSensorInfo();
  }, [tick]);

  const readSensorInfo = () => {
    if (sensorList !== undefined && sensorList.length > 0) {
      var sensorIdList = sensorList.map((info) => info.sensor_id);
      getSensorInfo(customerId, sensorIdList, props.dispatch);
    }
  };

  const onSelectSensor = (sensorName) => {};

  const sensorInfoList = props.sensorService.sensorInfo[customerId];
  const sensorUIList =
    sensorList === undefined || sensorList.length === 0
      ? null
      : sensorList.map((sensor) => {
          const sensorInfo =
            sensorInfoList === undefined
              ? undefined
              : sensorInfoList[sensor.sensor_id];

          return (
            <Col
              xs={24}
              sm={12}
              md={12}
              lg={8}
              key={`sensor_list_key_${sensor.sensor_id}`}
            >
              <OneItem
                sensor={sensor}
                sensorInfo={sensorInfo}
                onSelectSensor={onSelectSensor}
                timezone={timezone}
                setSelectedSensorId={setSelectedSensorId}
              />
            </Col>
          );
        });

  return (
    <div>
      <div className="add-sensor-button">
        <Button
          ghost
          style={{ textAlign: "right" }}
          onClick={() => setIsShowAddModal(true)}
        >
          Add Sensor +{" "}
        </Button>
      </div>
      <Row>{sensorUIList}</Row>
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  app: state.app,
  sensorService: state.sensorService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SensorDashboardPage);
