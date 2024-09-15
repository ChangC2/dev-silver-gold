import { Button, Col, Row } from "antd";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import { getHennigInfo } from "../../../services/common/hennig_apis";
import OneItem from "../OneItem/OneItem";
import "./HennigDashboardPage.css";
function HennigDashboardPage(props) {
  const { customerId, setIsShowAddModal, hennigList } = props;
  const { setSelectedSensorId, timezone } = props;

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const intervalTime = 60000;

  useEffect(() => {
    try {
      clearInterval(intervalId);
    } catch (e) {}
    readHennigInfo();
    setIntervalID(setInterval(timer, intervalTime));
    return () => {
      clearInterval(intervalId);
    };
  }, [customerId, hennigList]);

  const timer = () => {
    setTick((t) => !t);
  };

  useEffect(() => {
    readHennigInfo();
  }, [tick]);

  const readHennigInfo = () => {
    if (hennigList !== undefined && hennigList.length > 0) {
      var sensorIdList = hennigList.map((info) => info.sensor_id);
      getHennigInfo(customerId, sensorIdList, props.dispatch);
    }
  };

  const onSelectHennig = (hennigName) => {};

  const hennigInfoList = props.hennigService.hennigInfo[customerId];
  const hennigUIList =
    hennigList === undefined || hennigList.length === 0
      ? null
      : hennigList.map((hennig) => {
          const hennigInfo =
            hennigInfoList === undefined
              ? undefined
              : hennigInfoList[hennig.sensor_id];

          return (
            <Col
              xs={24}
              sm={12}
              md={12}
              lg={8}
              key={`hennig_list_key_${hennig.sensor_id}`}
            >
              <OneItem
                hennig={hennig}
                hennigInfo={hennigInfo}
                onSelectHennig={onSelectHennig}
                timezone={timezone}
                setSelectedSensorId={setSelectedSensorId}
              />
            </Col>
          );
        });

  return (
    <div>
      <div className="add-hennig-button">
        <Button
          ghost
          style={{ textAlign: "right" }}
          onClick={() => setIsShowAddModal(true)}
        >
          Add Sensor +{" "}
        </Button>
      </div>
      <Row>{hennigUIList}</Row>
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  app: state.app,
  hennigService: state.hennigService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HennigDashboardPage);
