// @flow strict

import { Col, Row } from "antd";
import "./TextWithIcon.css";

function TextWithIcon(props) {
  const { text, icon, marginLeft, iconSize, fontSize, iconBottom, iconClick } =
    props;
  return (
    <Row align={"middle"}>
      <Col span={24}>
        <span
          className="text-with-icon-text"
          style={{ fontSize: fontSize === undefined ? "15px" : fontSize }}
        >
          {text}
        </span>
        <img
          src={icon}
          style={{
            marginLeft: marginLeft === undefined ? "0px" : marginLeft,
            marginBottom: iconBottom === undefined ? "0px" : iconBottom,
            width: iconSize === undefined ? "21px" : iconSize,
            height: iconSize === undefined ? "21px" : iconSize,
          }}
          onClick={iconClick === undefined ? ()=>{} : iconClick}
          alt="icon"
        />
      </Col>
    </Row>
  );
}

export default TextWithIcon;
