import { Col, Row, message, Modal } from "antd";
import React, { useState, useEffect } from "react";
import { connect, useSelector } from "react-redux";
import TextInput from "../../../components/cncPageComponents/TextInput/TextInput";
import SensorTypeSelector from "../../../components/sensorPageWidgets/sensorTypeSelector/sensorTypeSelector";
import {
  SENSOR_ALERT_FREQUENCY,
  SENSOR_ALERT_FREQUENCY_TITLE,
  SENSOR_TITLE_BY_ID,
  SENSOR_TYPE,
  SENSOR_UNIT_BY_ID,
} from "../../../services/common/constants";
import { getEnumKeyByValue } from "../../../services/common/functions";
import lang from "../../../services/lang";
import { updateSensorInfo } from "../../../services/common/sensor_apis";
import SensorImageUploader from "../AddSensorModal/SensorImageUploader/SensorImageUploader";
import "./sensorUpdateModal.css";

function SensorUpdateModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { sensorInfo, setIsShowUpdateModal, isShowUpdateModal } = props;
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
    min_limit: 0,
    max_limit: 0,
    frequency: 0,
    alert_emails: "",
  });

  useEffect(() => {
    if (sensorInfo !== undefined) {
      setTempSetting({ ...sensorInfo });
    }
  }, [sensorInfo]);

 

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

  const onClickUpdateSensorInfo = () => {
    updateSensorInfo(
      props.app.customer_id,
      tempSetting,
      props.dispatch,
      (res) => {
        if (res === true) {
          message.success(lang(langIndex, "msg_update_success"));
          setIsShowUpdateModal(false);
        }
      }
    );
  };

  return (
    <div>
      <Modal
        title={lang(langIndex, "iiot_updatesensormodal")}
        visible={isShowUpdateModal}
        onOk={onClickUpdateSensorInfo}
        onCancel={() => setIsShowUpdateModal(false)}
        destroyOnClose={true}
        className="update-sensor-modal"
      >
        <div className="update-sensor-modal-body-container">
          <Row>
            <Col span={12}>
              <div className={"update-sensor-modal-image-upload-container"}>
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
                valueType="number"
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
                  getEnumKeyByValue(SENSOR_TYPE, parseInt(tempSetting.type))
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

          <Row style={{ marginTop: 10 }}>
            <Col span={8}>
              <TextInput
                initValue={tempSetting}
                field="min_limit"
                updateValue={onUpdateValue}
                input_type="number"
                title={lang(langIndex, "iiot_minlimit")}
              />
            </Col>
            <Col span={8}>
              <TextInput
                initValue={tempSetting}
                field="max_limit"
                updateValue={onUpdateValue}
                input_type="number"
                title={lang(langIndex, "iiot_maxlimit")}
              />
            </Col>
            <Col span={8}>
              <SensorTypeSelector
                initValue={tempSetting}
                field="frequency"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_frequency")}
                valueType="number"
                optionList={[
                  {
                    value: SENSOR_ALERT_FREQUENCY.min5,
                    title: SENSOR_ALERT_FREQUENCY_TITLE.min5,
                  },
                  {
                    value: SENSOR_ALERT_FREQUENCY.min10,
                    title: SENSOR_ALERT_FREQUENCY_TITLE.min10,
                  },
                  {
                    value: SENSOR_ALERT_FREQUENCY.min20,
                    title: SENSOR_ALERT_FREQUENCY_TITLE.min20,
                  },
                  {
                    value: SENSOR_ALERT_FREQUENCY.hr1,
                    title: SENSOR_ALERT_FREQUENCY_TITLE.hr1,
                  },
                  {
                    value: SENSOR_ALERT_FREQUENCY.noAlert,
                    title: SENSOR_ALERT_FREQUENCY_TITLE.noAlert,
                  },
                ]}
              />
            </Col>
          </Row>

          <Row style={{ marginTop: 10 }}>
            <Col span={24}>
              <TextInput
                initValue={tempSetting}
                field="alert_emails"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_alertemails")}
              />
            </Col>
          </Row>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "iiot_valuedescription_optional")}
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
export default connect(mapStateToProps, mapDispatchToProps)(SensorUpdateModal);
