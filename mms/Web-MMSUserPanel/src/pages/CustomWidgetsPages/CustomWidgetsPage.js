import { PrinterOutlined } from "@ant-design/icons";
import { Button, Col, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { connect, useDispatch, useSelector } from "react-redux";
import { sizeMobile } from "services/common/constants";
import {
  getCustomDashboardReport, GetCustomeWidget
} from "services/common/cw_apis";

import { GetStatus } from "../../services/common/cnc_apis";

import AddCustomWidget from "./AddCustomWidget";
import "./CustomWidgetsPage.css";
import CWBarChart from "./CWBarChart";
import CWGaugeChart from "./CWGaugeChart";
import CWMachineInfo from "./CWMachineInfo";
import CWPieChart from "./CWPieChart";
import CWTextInfo from "./CWTextInfo";
import CWTrendChart from "./CWTrendChart";
import EditCustomWidget from "./EditCustomWidget";
import { ShowPDFPreviewModal } from "./ShowPDFPreviewModal";

const { Option } = Select;

const CustomWidgetsPage = (props) => {
  const appData = useSelector((state) => state.app);

  const dispatch = useDispatch();
  const cwService = useSelector((x) => x.cwService);
  const cncService = useSelector((x) => x.cncService);

  const { langIndex } = appData;
  const { screenSize } = props.app;
  const { customer_id, dashboard_id } = props;

  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditForm, setShowEditForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [selWidget, setSelWidget] = useState();
  const [showSpin, setShowSpin] = useState(false);

  const customerInfo = cncService.customerInfoList[customer_id];
  
  const customWidgets = cwService.cwWidgets;

  const onClickCreatePDF = async () => {
    if (customWidgets.length == 0 || customerInfo == null) return;
    const result = await ShowPDFPreviewModal({
      customWidgets: customWidgets,
      screenSize: screenSize,
      customerInfo: customerInfo,
    });

    if (result["widgetDatas"] != null && result["widgetDatas"] != undefined) {
      setShowSpin(true);
      getCustomDashboardReport(
        result["widgetDatas"],
        result["logoData"],
        customWidgets,
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

  useEffect(() => {
    setShowAddForm(false);
    GetStatus(customer_id, props.dispatch);
  }, [customer_id]);

  useEffect(() => {
    setShowAddForm(false);
    setIsEditing(false);
    GetCustomeWidget(customer_id, dashboard_id, dispatch, (res) => {});
  }, [dashboard_id]);

  const mainUI = customWidgets.map((widget) => {
    {
      switch (parseInt(widget.widget_type)) {
        case 0:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWTrendChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
        case 1:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWBarChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
        case 2:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWGaugeChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
        case 3:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWPieChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                  />
                </div>
              </div>
            </Col>
          );
        case 4:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWTextInfo
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
        case 5:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWMachineInfo
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
        default:
          return (
            <Col
              onClick={() => {
                if (isEditing) {
                  setShowEditForm(true);
                  setSelWidget(widget);
                }
              }}
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div className={isEditing ? "cw-widget-editing" : ""}>
                <div className="cw-widget-container">
                  <CWTrendChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={false}
                  />
                </div>
              </div>
            </Col>
          );
      }
    }
  });

  return (
    <div style={{ margin: 15 }}>
      {customWidgets === undefined && (
        <div style={{ paddingTop: 100, textAlign: "center" }}>
          <Spin tip="Loading ..." size="large" />
        </div>
      )}
      {customWidgets !== undefined && customWidgets !== null && (
        <div>
          {!showAddForm && !showEditForm && (
            <div className="add-cw-button">
              <Button
                ghost
                style={{ width: 100 }}
                onClick={() => {
                  onClickCreatePDF();
                }}
              >
                <PrinterOutlined />
                Print{" "}
                {showSpin && (
                  <Spin style={{ marginLeft: 10, marginRight: 10 }} />
                )}
              </Button>
              <span className="backspace" />
              <Button
                ghost
                onClick={() => {
                  setShowAddForm(true);
                }}
              >
                Add New Widget +{" "}
              </Button>
              <span className="backspace" />
              <Button
                ghost
                onClick={() => {
                  setIsEditing(!isEditing);
                  if (isEditing) {
                    setShowEditForm(false);
                  }
                }}
              >
                {isEditing ? "Cancel Edit" : "Edit Widget"}
              </Button>
            </div>
          )}

          {showAddForm && (
            <AddCustomWidget
              customer_id={customer_id}
              dashboard_id={dashboard_id}
              setShowAddForm={setShowAddForm}
            />
          )}

          {showEditForm && (
            <EditCustomWidget
              widget_info={selWidget}
              setShowEditForm={setShowEditForm}
            />
          )}

          {!showAddForm && !showEditForm && customWidgets.length === 0 && (
            <div className="cd-no-widget">No Widget</div>
          )}

          {!showAddForm && !showEditForm && customWidgets.length > 0 && (
            <div>
              <Row>{mainUI}</Row>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

const mapStateToProps = (state, props) => ({
  cwService: state.cwService,
  cncService: state.cncService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(CustomWidgetsPage);
