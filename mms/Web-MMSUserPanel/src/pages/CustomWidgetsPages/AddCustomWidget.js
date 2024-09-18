import { Button, Col, InputNumber, message, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { connect, useDispatch, useSelector } from "react-redux";

import {
  GetCustomeWidgetFilters,
  AddCustomeWidgetFilters,
} from "../../services/common/cw_apis";

import { CloseOutlined } from "@ant-design/icons";

import { cwDataPoints, cwSizes, cwTypes } from "services/common/constants";
import "./AddCustomWidget.css";
import CWCheckBoxes from "./CWCheckBoxes";

const { Option } = Select;

const AddCustomWidget = (props) => {
  const dispatch = useDispatch();
  const appData = useSelector((state) => state.app);
  const { langIndex } = appData;
  const { screenSize } = props.app;
  const { customer_id, dashboard_id, setShowAddForm } = props;

  const totalSteps = 4;
  const [isLoading, setIsLoading] = useState(false);

  const [stepOfAdd, setStepOfAdd] = useState(1);

  const [widgetTypes, setWidgetTypes] = useState([]);
  const [widgetType, setWidgetType] = useState(0);
  const [widgetSizes, setwidgetSizes] = useState([]);
  const [dataPoints, setDataPoints] = useState([]);

  const [machineList, setMachineList] = useState([]);
  const [machine, setMachine] = useState("");
  const [operatorList, setOperatorList] = useState([]);
  const [operator, setOperator] = useState("");
  const [jobIDList, setJobIDList] = useState([]);
  const [jobID, setJobID] = useState("");
  const [days, setDays] = useState(30);

  const titleOfAdd = [
    "Widget Type",
    "Widget Size",
    "Data Point(s)",
    "Filter(s)",
  ];

  const setCheckBoxData = (step) => {
    let data = [];
    if (step === 1) {
      data = [...cwTypes];
    } else if (step === 2) {
      data = [...cwSizes];
    } else if (step === 3) {
      data = [...cwDataPoints];
    }

    let i = 0;
    let tmp = [];
    for (i = 0; i < data.length; i++) {
      const newData = {
        key: i,
        name: data[i],
        value: step < 3 && i === 0 ? true : false,
      };
      tmp.push(newData);
    }

    if (step === 1) {
      setWidgetTypes(tmp);
    } else if (step === 2) {
      setwidgetSizes(tmp);
    } else if (step === 3) {
      setDataPoints(tmp);
    }
  };

  const initStates = () => {
    setStepOfAdd(1);
    setCheckBoxData(1);
    setCheckBoxData(2);
    setCheckBoxData(3);
    setMachine("");
    setOperator("");
    setJobID("");
    setDays(30);
  };

  useEffect(() => {
    initStates();
    //setIsLoading(true);
    GetCustomeWidgetFilters(customer_id, (res) => {
      //setIsLoading(false);
      if (res != undefined && res != null) {
        setMachineList(res["machineList"]);
        if (res["machineList"] != undefined && res["machineList"].length > 0) {
          setMachine(res["machineList"][0].machine);
        }
        setOperatorList(res["operatorList"]);
        setJobIDList(res["jobIDList"]);
      } else {
        setMachineList([]);
        setOperatorList([]);
        setJobIDList([]);
      }
    });
  }, []);

  useEffect(() => {
    setWType();
  }, [widgetTypes]);

  const machineOptionUI =
    machineList == undefined || machineList.length == 0
      ? null
      : machineList.map((info, index) => {
          return (
            <Option
              key={`machine_option_key${index}`}
              value={info.machine}
              className="cd-machine-selector-value"
            >
              {info.machine}
            </Option>
          );
        });

  const operatorOptionUI =
    operatorList == undefined || operatorList.length == 0
      ? null
      : operatorList.map((info, index) => {
          return (
            <Option
              key={`machine_option_key${index}`}
              value={info.operator}
              className="cd-machine-selector-value"
            >
              {info.operator}
            </Option>
          );
        });

  const jobIDOptionUI =
    jobIDList == undefined || jobIDList.length == 0
      ? null
      : jobIDList.map((info, index) => {
          return (
            <Option
              key={`machine_option_key${index}`}
              value={info.jobID}
              className="cd-machine-selector-value"
            >
              {info.jobID}
            </Option>
          );
        });

  const setWType = () => {
    let i = 0;
    let wType = 0;
    for (i = 0; i < widgetTypes.length; i++) {
      if (widgetTypes[i].value) {
        wType = i;
        break;
      }
    }
    setWidgetType(wType);
  };

  const AddCustomWidget = () => {
    let i = 0;
    let wType = 0;
    for (i = 0; i < widgetTypes.length; i++) {
      if (widgetTypes[i].value) {
        wType = i;
        break;
      }
    }

    let wSize = 0;
    for (i = 0; i < widgetSizes.length; i++) {
      if (widgetSizes[i].value) {
        wSize = i;
        break;
      }
    }

    if (machine == "") {
      message.info("Please select machine");
      return;
    }

    let dPointStr = "";
    let checkedCount = 0;
    for (i = 0; i < dataPoints.length; i++) {
      if (i == 0) {
        dPointStr = dPointStr.concat(dataPoints[i].value ? "1" : "0");
      } else {
        dPointStr = dPointStr.concat(dataPoints[i].value ? ",1" : ",0");
      }
      if (dataPoints[i].value) {
        checkedCount++;
      }
    }

    if (checkedCount === 0 && widgetType != 5) {
      message.info("Please select data points");
      return;
    }

    if (wType === 5) {
      wSize = 1;
      dPointStr = "1,1,0,0,0,0,1";

      if (operator === "") {
        message.info("Please select operator");
        return;
      }
      if (jobID === "") {
        message.info("Please select jobID");
        return;
      }
    }

    if (wType == 4){
      wSize = 1;
    }

    let trendDay = wType == 5 ? 1 : days;

    const params = {
      customer_id: customer_id,
      dashboard_id: dashboard_id,
      widget_type: wType,
      widget_size: wSize,
      data_points: dPointStr,
      machine: machine,
      operator: operator,
      jobID: jobID,
      days: trendDay,
    };

    setIsLoading(true);
    AddCustomeWidgetFilters(params, dispatch, (res) => {
      setIsLoading(false);
      setShowAddForm(false);
      if (res) {
        message.success("Success to add");
      } else {
        message.error("Fail to add");
      }
    });
  };

  return (
    <Spin spinning={isLoading}>
      <div>
        <div className="cd-add-form-back">
          <Row>
            <Col span={8}>
              <h3 className="cd-add-form-step">
                Step {stepOfAdd} Of {totalSteps}
              </h3>
            </Col>
            <Col span={16} className="cd-add-form-close">
              <CloseOutlined
                onClick={() => {
                  setShowAddForm(false);
                }}
              />
            </Col>
          </Row>
          <Row align="center">
            <Col>
              <h3 className="cd-add-form-title">{titleOfAdd[stepOfAdd - 1]}</h3>
            </Col>
          </Row>

          {stepOfAdd == 1 && (
            <CWCheckBoxes
              data={widgetTypes}
              currentStep={stepOfAdd}
              setData={setWidgetTypes}
              type="Single"
            />
          )}

          {stepOfAdd == 2 && (
            <CWCheckBoxes
              data={widgetSizes}
              widgetType={widgetType}
              currentStep={stepOfAdd}
              setData={setwidgetSizes}
              type="Single"
            />
          )}

          {stepOfAdd == 3 && (
            <CWCheckBoxes
              data={dataPoints}
              currentStep={stepOfAdd}
              widgetType={widgetType}
              setData={setDataPoints}
              type="Multiple"
            />
          )}

          {stepOfAdd == 4 && (
            <div>
              <Row align="middle" justify="center" style={{ marginTop: 15 }}>
                <Col style={{ width: 100 }}>
                  <span className="cd-selector-label">Machine :</span>
                </Col>
                <Col className="cd-add-form-step3">
                  <Select
                    onChange={(value) => {
                      setMachine(value);
                    }}
                    defaultValue={machine}
                    value={machine}
                    className="cd-machine-selector"
                    dropdownClassName="cd-machine-selector-dropdown"
                  >
                    {machineOptionUI}
                  </Select>
                </Col>
              </Row>

              <Row align="middle" justify="center" style={{ marginTop: 15 }}>
                <Col style={{ width: 100 }}>
                  <span className="cd-selector-label">Operator :</span>
                </Col>
                <Col className="cd-add-form-step3">
                  <Select
                    onChange={(value) => {
                      setOperator(value);
                    }}
                    defaultValue={operator}
                    value={operator}
                    className="cd-machine-selector"
                    dropdownClassName="cd-machine-selector-dropdown"
                  >
                    <Option
                      key={`operator_option_key${0}`}
                      value={""}
                      className="cd-machine-selector-value"
                    >
                      {""}
                    </Option>
                    {operatorOptionUI}
                  </Select>
                </Col>
              </Row>

              <Row align="middle" justify="center" style={{ marginTop: 15 }}>
                <Col style={{ width: 100 }}>
                  <span className="cd-selector-label">Job ID :</span>
                </Col>
                <Col className="cd-add-form-step3">
                  <Select
                    onChange={(value) => {
                      setJobID(value);
                    }}
                    defaultValue={jobID}
                    value={jobID}
                    className="cd-machine-selector"
                    dropdownClassName="cd-machine-selector-dropdown"
                  >
                    <Option
                      key={`jobID_option_key${0}`}
                      value={""}
                      className="cd-machine-selector-value"
                    >
                      {""}
                    </Option>
                    {jobIDOptionUI}
                  </Select>
                </Col>
              </Row>

              {widgetType != 5 && (
                <Row align="middle" justify="center" style={{ marginTop: 15 }}>
                  <Col style={{ width: 350 }}>
                    <span className="cd-selector-label">Trailing </span>
                    <span className="backspace" />
                    <span className="backspace" />
                    <InputNumber
                      className="cd-number-input"
                      min={1}
                      max={365}
                      defaultValue={days}
                      onChange={(value) => {
                        setDays(value);
                      }}
                    />
                    <span className="backspace" />
                    <span className="backspace" />
                    <span className="cd-selector-label">Days Throughput</span>
                  </Col>
                </Row>
              )}
            </div>
          )}

          <Row align="center" style={{ marginTop: 30 }}>
            <Col>
              <Button
                type="ghost"
                className="cd-add-form-button"
                onClick={() => {
                  if (stepOfAdd == 1) {
                    return;
                  }
                  const currentStep = stepOfAdd - 1;
                  if (widgetType == 5 && currentStep == 3) {
                    setStepOfAdd(currentStep - 1);
                  } else {
                    setStepOfAdd(currentStep);
                  }
                }}
              >
                Back
              </Button>
            </Col>
            <Col style={{ marginLeft: 20 }}>
              <Button
                type="ghost"
                className="cd-add-form-button"
                onClick={() => {
                  if (stepOfAdd < totalSteps) {
                    const currentStep = stepOfAdd + 1;
                    if (widgetType == 5 && currentStep == 3) {
                      setStepOfAdd(currentStep + 1);
                    } else {
                      setStepOfAdd(currentStep);
                    }
                  } else {
                    AddCustomWidget();
                  }
                }}
              >
                {stepOfAdd < totalSteps ? "Next" : "Save"}
              </Button>
            </Col>
          </Row>
        </div>
      </div>
    </Spin>
  );
};

const mapStateToProps = (state, props) => ({
  cwService: state.cwService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(AddCustomWidget);
