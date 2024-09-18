import { Col, Row, message } from "antd";
import logoutIcon from "../../assets/icons/ic_logout_app.png";
import qrCodeIcon from "../../assets/icons/ic_qrcode_scan.png";
import "./PartIDInputLayout.css";

const PartIDInputLayout = (props) => {
  const { setShowInputMode, partID, setPartID } = props;

  const onLogout = () => {
    setPartID("");
  };

  return (
    <Row align={"middle"} className="part-id-input-layout">
      <Col flex={"85px"}>{"Part ID : "}</Col>
      <Col flex={"auto"} style={{ cursor: "default" }}>
        {partID}
      </Col>
      <Col
        flex={"50px"}
        style={{ textAlign: "right" }}
        onClick={() => {
          setShowInputMode(true);
        }}
      >
        <img
          src={qrCodeIcon}
          style={{ width: "30px", height: "30px" }}
          alt="qrcode"
        />
      </Col>
      <Col
        flex={"50px"}
        style={{ textAlign: "right" }}
        onClick={() => {
          onLogout();
        }}
      >
        <img
          src={logoutIcon}
          style={{ width: "30px", height: "30px" }}
          alt="logout"
        />
      </Col>
    </Row>
  );
};

export default PartIDInputLayout;
