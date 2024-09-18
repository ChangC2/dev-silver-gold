import { Col, message, Modal, Row } from "antd";
import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";
import TextInput from "../../../components/cncPageComponents/TextInput/TextInput";
import HennigTypeSelector from "../../../components/HennigPageWidgets/HennigTypeSelector/HennigTypeSelector";
import {
  HENNIG_ALERT_FREQUENCY,
  HENNIG_ALERT_FREQUENCY_TITLE,
  HENNIG_TYPE,
} from "../../../services/common/constants";
import { addHennig } from "../../../services/common/hennig_apis";
import lang from "../../../services/lang";
import "./AddHennigModal.css";
import HennigImageUploader from "./HennigImageUploader/HennigImageUploader";

function AddHennigModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { initSetting, isShowAddModal, setIsShowAddModal } = props;

  const [tempSetting, setTempSetting] = useState({
    sensor_id: "",
    sensor_name: "",
    location: "",
    for_value1: "Value1",
    for_value2: "Value2",
    for_value3: "Value3",
    for_value4: "Value4",
    for_value5: "Value5",
    for_value6: "Value6",
    image: "",
    type: HENNIG_TYPE.other,
    unit: "",
    min_limit: "0",
    max_limit: "0",
    alert_emails: "",
    modbus_ip: "",
    modbus_hreg: "0,1,2,3",
  });

  const [hregs, setHregs] = useState({
    address1: "0",
    address2: "1",
    address3: "2",
    address4: "3",
  });

  useEffect(() => {
    if (initSetting != undefined) {
      setTempSetting({ ...initSetting });
    }
  }, [initSetting]);

  useEffect(() => {
    let hregStr = tempSetting["modbus_hreg"];
    let hregVals = hregStr.split(",");
    if (hregVals.length === 4) {
      setHregs({
        address1: hregVals[0],
        address2: hregVals[1],
        address3: hregVals[2],
        address4: hregVals[3],
      });
    }
  }, [tempSetting]);

  const validateValues = (setting) => {
    if (isNaN(+setting.sensor_id)) {
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

    addHennig(props.app.customer_id, tempSetting, props.dispatch, (res) => {
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

    if (field === "address1") {
      let hregStr = newSetting["modbus_hreg"];
      let hregVals = hregStr.split(",");
      if (hregVals.length === 4) {
        let newHregStr =
          value + "," + hregVals[1] + "," + hregVals[2] + "," + hregVals[3];
        newSetting.modbus_hreg = newHregStr;
      }
    }
    if (field === "address2") {
      let hregStr = newSetting["modbus_hreg"];
      let hregVals = hregStr.split(",");
      if (hregVals.length === 4) {
        let newHregStr =
          hregVals[0] + "," + value + "," + hregVals[2] + "," + hregVals[3];
        newSetting.modbus_hreg = newHregStr;
      }
    }
    if (field === "address3") {
      let hregStr = newSetting["modbus_hreg"];
      let hregVals = hregStr.split(",");
      if (hregVals.length === 4) {
        let newHregStr =
          hregVals[0] + "," + hregVals[1] + "," + value + "," + hregVals[3];
        newSetting.modbus_hreg = newHregStr;
      }
    }
    if (field === "address4") {
      let hregStr = newSetting["modbus_hreg"];
      let hregVals = hregStr.split(",");
      if (hregVals.length === 4) {
        let newHregStr =
          hregVals[0] + "," + hregVals[1] + "," + hregVals[2] + "," + value;
        newSetting.modbus_hreg = newHregStr;
      }
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
        className="add-hennig-modal"
      >
        <div className="add-hennig-modal-body-container">
          <Row>
            <Col span={12}>
              <div className={"add-hennig-modal-image-upload-container"}>
                <HennigImageUploader
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
          {/* <Row style={{ marginTop: 10 }}>
            <Col span={18}>
              <HennigTypeSelector
                initValue={tempSetting}
                field="type"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_sensortype")}
                optionList={[
                  {
                    value: HENNIG_TYPE.temperature,
                    title: HENNIG_TITLE_BY_ID.temperature,
                  },
                  {
                    value: HENNIG_TYPE.current,
                    title: HENNIG_TITLE_BY_ID.current,
                  },
                  {
                    value: HENNIG_TYPE.vibration,
                    title: HENNIG_TITLE_BY_ID.vibration,
                  },
                  {
                    value: HENNIG_TYPE.moisture,
                    title: HENNIG_TITLE_BY_ID.moisture,
                  },
                  {
                    value: HENNIG_TYPE.weight,
                    title: HENNIG_TITLE_BY_ID.weight,
                  },
                  {
                    value: HENNIG_TYPE.tempAndHum,
                    title: HENNIG_TITLE_BY_ID.tempAndHum,
                  },
                  { value: HENNIG_TYPE.other, title: HENNIG_TITLE_BY_ID.other },
                ]}
              />
            </Col>
            <Col span={6}>
              <HennigTypeSelector
                initValue={tempSetting}
                field="unit"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_unit")}
                optionList={HENNIG_UNIT_BY_ID[
                  getEnumKeyByValue(HENNIG_TYPE, tempSetting.type)
                ].map((e) => {
                  return { value: e, title: e };
                })}
              />
            </Col>
          </Row> */}
          {/* <Row style={{ marginTop: 10 }}>
            <Col span={24}>
              <TextInput
                initValue={tempSetting}
                field="location"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_location")}
              />
            </Col>
          </Row> */}
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
              <HennigTypeSelector
                initValue={tempSetting}
                field="frequency"
                updateValue={onUpdateValue}
                title={lang(langIndex, "iiot_frequency")}
                valueType="number"
                optionList={[
                  {
                    value: HENNIG_ALERT_FREQUENCY.min5,
                    title: HENNIG_ALERT_FREQUENCY_TITLE.min5,
                  },
                  {
                    value: HENNIG_ALERT_FREQUENCY.min10,
                    title: HENNIG_ALERT_FREQUENCY_TITLE.min10,
                  },
                  {
                    value: HENNIG_ALERT_FREQUENCY.min20,
                    title: HENNIG_ALERT_FREQUENCY_TITLE.min20,
                  },
                  {
                    value: HENNIG_ALERT_FREQUENCY.hr1,
                    title: HENNIG_ALERT_FREQUENCY_TITLE.hr1,
                  },
                  {
                    value: HENNIG_ALERT_FREQUENCY.noAlert,
                    title: HENNIG_ALERT_FREQUENCY_TITLE.noAlert,
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
            <div className="add-hennig-group-title">
              Value Descriptions (optional)
            </div>
            <Row>
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
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value4"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}4`}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value5"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}5`}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  initValue={tempSetting}
                  field="for_value6"
                  updateValue={onUpdateValue}
                  title={`${lang(langIndex, "iiot_valuedescription")}6`}
                />
              </Col>
            </Row>
          </div>

          <div className="app-setting-group-container">
            <div className="add-hennig-group-title">Modbus Settings</div>
            <Row>
              <Col span={24}>
                <TextInput
                  initValue={tempSetting}
                  field="modbus_ip"
                  updateValue={onUpdateValue}
                  title={"Modbus IP"}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  input_type="number"
                  initValue={hregs}
                  field="address1"
                  updateValue={onUpdateValue}
                  title={"HReg Address1"}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  input_type="number"
                  initValue={hregs}
                  field="address2"
                  updateValue={onUpdateValue}
                  title={"HReg Address2"}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  input_type="number"
                  initValue={hregs}
                  field="address3"
                  updateValue={onUpdateValue}
                  title={"HReg Address3"}
                />
              </Col>
              <Col span={12}>
                <TextInput
                  input_type="number"
                  initValue={hregs}
                  field="address4"
                  updateValue={onUpdateValue}
                  title={"HReg Address4"}
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
  hennigService: state.hennigService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(AddHennigModal);
