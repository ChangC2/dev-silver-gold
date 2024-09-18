// @flow strict

import { Col, Row } from "antd";
import greenDownIcon from "../../assets/icons/ic_down_green.png";
import redDownIcon from "../../assets/icons/ic_down_red.png";
import greenUpIcon from "../../assets/icons/ic_up_green.png";
import redUpIcon from "../../assets/icons/ic_up_red.png";
import "./GoodBadWidget.css";

function GoodBadWidget(props) {
  const { type, value, setValue, setShowModal } = props;

  const onClick = () => {
    setShowModal(true);
  };

  const onUp = () => {
    let v = value;
    v++;
    setValue(v);
  };

  const onDown = () => {
    let v = value;
    if (v > 0) v--;
    setValue(v);
  };

  return (
    <div className="good-bad-widget">
      <span
        className="good-bad-widget-title"
        style={{
          color: type === 0 ? "rgb(48, 191, 120)" : "rgb(244, 102, 74)",
        }}
      >
        {type === 0 ? "Good Parts" : "Bad Parts"}
      </span>

      <Row align="middle" className="good-bad-widget-up-down">
        <Col span={8} style={{ textAlign: "left" }}>
          <img
            className="good-bad-widget-down"
            style={{
              borderColor:
                type === 0 ? "rgb(48, 191, 120)" : "rgb(244, 102, 74)",
            }}
            src={type === 0 ? greenDownIcon : redDownIcon}
            onClick={() => onDown()}
          />
        </Col>
        <Col span={8} style={{ textAlign: "center" }} onClick={() => onClick()}>
          <span
            style={{
              color: type === 0 ? "rgb(48, 191, 120)" : "rgb(244, 102, 74)",
            }}
            className="good-bad-widget-value"
          >
            {value}
          </span>
        </Col>
        <Col span={8} style={{ textAlign: "right" }}>
          <img
            className="good-bad-widget-up"
            style={{
              borderColor:
                type === 0 ? "rgb(48, 191, 120)" : "rgb(244, 102, 74)",
            }}
            src={type === 0 ? greenUpIcon : redUpIcon}
            onClick={() => onUp()}
          />
        </Col>
      </Row>
    </div>
  );
}

export default GoodBadWidget;
