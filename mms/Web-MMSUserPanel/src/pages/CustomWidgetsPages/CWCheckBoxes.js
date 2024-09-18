import { Checkbox, Col, Row, Select } from "antd";
import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";

import { cwDataPoints } from "services/common/constants";
import "./CWCheckBoxes.css";

const { Option } = Select;

const CWCheckBoxes = (props) => {
  const appData = useSelector((state) => state.app);
  const { langIndex } = appData;
  const { screenSize } = props.app;

  const { data, setData, type, widgetType, currentStep } = props;
  const checkBoxes =
    data === null
      ? null
      : data.map((item) => {
          return (
            <Row align="center" key={`cw-${item.key}`}>
              <Col>
                <Checkbox
                  style={{
                    display:
                      (currentStep == 3 &&
                        (((item.key < 2 || item.key == 6) && widgetType == 2) ||
                          (item.key > 1 && widgetType == 3))) ||
                      (currentStep == 2 && widgetType > 3 && item.key == 0)
                        ? "none"
                        : "",
                  }}
                  className="cd-add-form-checkbox"
                  checked={
                    widgetType > 3 && item.key == 1 && currentStep == 2
                      ? true
                      : item.value
                  }
                  onChange={(e) => {
                    const newData = [...data];
                    if (type == "Single") {
                      let i = 0;
                      for (i = 0; i < newData.length; i++) {
                        newData[i]["value"] = i === item.key ? true : false;
                      }
                    } else {
                      newData[item.key]["value"] = e.target.checked;
                    }
                    setData(newData);
                  }}
                >
                  {item.name}
                </Checkbox>
              </Col>
            </Row>
          );
        });

  return <div>{checkBoxes}</div>;
};

const mapStateToProps = (state, props) => ({
  cwService: state.cwService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(CWCheckBoxes);
