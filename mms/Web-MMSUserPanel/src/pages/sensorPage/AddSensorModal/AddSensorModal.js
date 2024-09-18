import { Col, message, Modal, Row } from "antd";
import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";
import TextInput from "../../../components/cncPageComponents/TextInput/TextInput";
import SensorTypeSelector from "../../../components/sensorPageWidgets/sensorTypeSelector/sensorTypeSelector";
import {
  SENSOR_TITLE_BY_ID,
  SENSOR_TYPE,
  SENSOR_UNIT_BY_ID,
} from "../../../services/common/constants";
import { getEnumKeyByValue } from "../../../services/common/functions";
import lang from "../../../services/lang";
import { addSensor } from "../../../services/common/sensor_apis";
import "./AddSensorModal.css";
import SensorImageUploader from "./SensorImageUploader/SensorImageUploader";

function AddSensorModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { initSetting, isShowAddModal, setIsShowAddModal } = props;

  const [tempSetting, setTempSetting] = useState({
    sensor_id: "",
    sensor_name: "",
    location: "",
    for_value1: "",
    for_value2: "",
    for_value3: "",
    image: "",
    type: SENSOR_TYPE.other,
    unit: "",
  });

  useEffect(() => {
    if (initSetting != undefined) {
      setTempSetting({ ...initSetting });
    }
  }, [initSetting]);

  const validateValues = (setting) => {
    if (setting.sensor_id === "") {
      message.error(lang(langIndex, "iiot_iderror"));
      return false;
    }
    if (setting.sensor_name === "") {
      message.error(lang(langIndex, "iiot_nameerror"));
      return false;
    }
    return true;
  };

  const onClickAdd = () => {
    var res = validateValues(tempSetting);
    if (res === false) {
      return;
    }

    addSensor(props.app.customer_id, tempSetting, props.dispatch, (res) => {
      if (res.status === true) {
        message.success(res.message);
        setIsShowAddModal(false);
      } else {
        message.error(res.message);
      }
    });
  };
  const onClickCancel = () => {
    setIsShowAddModal(false);
  };
  const onUpdateImage = (val) => {
    setTempSetting({ ...tempSetting, image: val });
  };
  
  const onUpdateValue = (field, value) => {
    var newSetting = { ...tempSetting };
    newSetting[field] = value;
    if (field === "type") {
      newSetting.unit = "";
    }
    setTempSetting({ ...newSetting });
  };

  return (
    <div>
      <Modal
        title={lang(langIndex, "iiot_addsensormodal")}
        visible={isShowAddModal}
        onOk={onClickAdd}
        onCancel={onClickCancel}
        destroyOnClose={true}
        className="add-sensor-modal"
      >
        <div className="add-sensor-modal-body-container">
          <Row>
            <Col span={12}>
              <div className={"add-sensor-modal-image-upload-container"}>
                <SensorImageUploader
                  onUpdateImage={onUpdateImage}
                  initImage={tempSetting.image}
                />
              </div>
            </Col>
            <Col span={12}>
              <div>
                <TextInput
                  initValue={tempSetting}
                  field="sensor_id"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_sensorid")} *`}
                />
              </div>
              <div>
                <TextInput
                  initValue={tempSetting}
                  field="sensor_name"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_sensorname")} *`}
                />
              </div>
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={18}>
              <SensorTypeSelector
                initValue={tempSetting}
                field="type"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_sensortype")}
                optionList={[
                  {
                    value: SENSOR_TYPE.temperature,
                    title: SENSOR_TITLE_BY_ID.temperature,
                  },
                  {
                    value: SENSOR_TYPE.current,
                    title: SENSOR_TITLE_BY_ID.current,
                  },
                  {
                    value: SENSOR_TYPE.vibration,
                    title: SENSOR_TITLE_BY_ID.vibration,
                  },
                  {
                    value: SENSOR_TYPE.moisture,
                    title: SENSOR_TITLE_BY_ID.moisture,
                  },
                  {
                    value: SENSOR_TYPE.weight,
                    title: SENSOR_TITLE_BY_ID.weight,
                  },
                  {
                    value: SENSOR_TYPE.tempAndHum,
                    title: SENSOR_TITLE_BY_ID.tempAndHum,
                  },
                  { value: SENSOR_TYPE.other, title: SENSOR_TITLE_BY_ID.other },
                ]}
              />
            </Col>
            <Col span={6}>
              <SensorTypeSelector
                initValue={tempSetting}
                field="unit"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_unit")}
                optionList={SENSOR_UNIT_BY_ID[
                  getEnumKeyByValue(SENSOR_TYPE, tempSetting.type)
                ].map((e) => {
                  return { value: e, title: e };
                })}
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={24}>
              <TextInput
                initValue={tempSetting}
                field="location"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_location")}
              />
            </Col>
          </Row>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              Value Descriptions (optional)
            </div>
            <Row style={{ marginTop: 10 }}>
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value1"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}1`}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value2"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}2`}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value3"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}3`}
                />
              </Col>
            </Row>
          </div>
        </div>
      </Modal>
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
export default connect(mapStateToProps, mapDispatchToProps)(AddSensorModal);
