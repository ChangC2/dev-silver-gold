import { Col, Row } from "antd";
import editIcon from "../../assets/icons/ic_jobid_edit.png";
import "./TankTimeWidget.css";

const TankTimeWidget = (props) => {
  const {
    times,
    setTimes,
    index,
    selectedTankIndex,
    setSelectedTankIndex,
    setShowTemperatureModal,
    temps,
    title,
  } = props;

  return (
    <Row align={"middle"} className="time-logger-tank-time-layout">
      <Col flex="50px">{title}</Col>
      <Col
        flex="auto"
        style={{
          textAlign: "center",
          color: index === selectedTankIndex ? "red" : "white",
          cursor: "default",
        }}
        onClick={() => {
          setSelectedTankIndex(index);
        }}
      >
        {times[index]}
      </Col>
      <Col
        flex="50px"
        style={{ color: "red", cursor:"default" }}
        onClick={() => {
          setShowTemperatureModal(true);
        }}
      >{`${temps[index]}°F`}</Col>
      <Col flex="50px">
        <img
          src={editIcon}
          style={{ width: "30px", height: "30px" }}
          onClick={() => {
            setShowTemperatureModal(true);
          }}
        />
      </Col>
    </Row>
  );
};

export default TankTimeWidget;
