import { Spin } from "antd";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import { CSSTransition } from "react-transition-group";
import { deleteHennig, getHennigList } from "../../services/common/hennig_apis";
import AddHennigModal from "./AddHennigModal/AddHennigModal";
import DetailPage from "./DetailPage/DetailPage";
import HennigDashboardPage from "./HennigDashboardPage/HennigDashboardPage";
import "./HennigPage.css";

function HennigPage(props) {

  const [isShowAddModal, setIsShowAddModal] = useState(false);
  const [selectedSensorId, setSelectedSensorId] = useState("");
  const [customerInfo, setCustomerInfo] = useState({});
  const customerId = props.app.customer_id;
  const [isLoading, setIsLoading] = useState(true);

  const { screenSize } = props.app;

  const onDeleteHennig = (sensorId) => {
    deleteHennig(customerId, sensorId, props.dispatch);
  };

  useEffect(() => {
    setSelectedSensorId("");
    setIsLoading(true);
    getHennigList(customerId, props.dispatch, (res) => {
      setIsLoading(false);
    });

    if (customerId !== undefined || customerId !== "") {
      var customer = props.cncService.customerInfoList[customerId];
      if (customer !== undefined) {
        setCustomerInfo(customer);
      }
    }
  }, [customerId]);


  const hennigList = props.hennigService.hennigList[customerId];

  const selectedHennigInfo =
    selectedSensorId === "" || hennigList === undefined
      ? ""
      : hennigList.filter((x) => x.sensor_id === selectedSensorId)[0];

  useEffect(() => {
    if (selectedHennigInfo === undefined) {
      setSelectedSensorId("");
    }
  }, [selectedHennigInfo]);

  return (
    <div className="hennig-page-background">
      {isShowAddModal && (
        <AddHennigModal
          isShowAddModal={isShowAddModal}
          setIsShowAddModal={setIsShowAddModal}
        />
      )}

      {isLoading && (
        <div style={{ paddingTop: 100, textAlign: "center" }}>
          <Spin tip="Loading ..." size="large" />
        </div>
      )}

      {!isLoading && (
        <div style={{ margin: 15 }}>
          {(selectedSensorId === "" || selectedHennigInfo === undefined) && (
            <HennigDashboardPage
              customerId={customerId}
              setIsShowAddModal={setIsShowAddModal}
              hennigList={hennigList}
              selectedSensorId={selectedSensorId}
              setSelectedSensorId={setSelectedSensorId}
              timezone={customerInfo.timezone}
            />
          )}

          <CSSTransition
            in={selectedSensorId !== "" && selectedHennigInfo !== undefined}
            timeout={1000}
            classNames="pageSliderLeft"
            unmountOnExit={true}
          >
            <div>
              {selectedSensorId !== "" && selectedHennigInfo !== undefined && (
                <DetailPage
                  screenSize={screenSize}
                  selectedSensorId={selectedSensorId}
                  setSelectedSensorId={setSelectedSensorId}
                  hennigInfo={selectedHennigInfo}
                  onDeleteHennig={onDeleteHennig}
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
  hennigService: state.hennigService,
  cncService: state.cncService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(HennigPage);
