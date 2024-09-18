import React, { useEffect, useState } from "react";
import { Col, Row, Checkbox } from "antd";
import "./MachineContainer.css";
import {
  BASE_URL,
  MACHINE_IMAGE_BASE_URL,
} from "../../../services/common/urls";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import OptionInput from "../../../components/cncPageComponents/OptionInput/OptionInput";
function MachineContainer(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { selectedMachine, setSelectedMachine } = props;
  const { selectedOperator, setSelectedOperator } = props;
  const { machineList } = props;
  const { operatorList } = props;
  const [selectOption, setSelectOption] = useState({
    machine: "0",
  });

  const setOptionValue = (field, value) => {
    var newOption = { ...selectOption };
    newOption[field] = value;
    setSelectOption({ ...newOption });
    setSelectedMachine([]);
    setSelectedOperator([]);
  };

  const machineListUI =
    machineList == undefined
      ? null
      : machineList.map((info) => {
          return (
            <Row
              className="one-machine-container none"
              key={info["machine_id"]}
            >
              <Col span={8}>
                <img
                  src={
                    info["machine_picture_url"].includes("http")
                      ? info["machine_picture_url"]
                      : MACHINE_IMAGE_BASE_URL + info["machine_picture_url"]
                  }
                  alt="one-machine-image"
                  className="one-machine-image"
                />
              </Col>
              <Col span={12}>
                <span>{info["machine_id"]}</span>
              </Col>
              <Col span={2}>
                <Checkbox
                  id={info["machine_id"]}
                  onChange={onMachineSelect}
                ></Checkbox>
              </Col>
            </Row>
          );
        });

  const operatorListUI =
    operatorList == undefined
      ? null
      : operatorList.map((info) => {
          return (
            <Row className="one-machine-container none" key={info["name"]}>
              <Col span={8}>
                <img
                  src={
                    info["image"] != null && info["image"].includes("http")
                      ? info["image"]
                      : BASE_URL + "images/photo/blank.jpg"
                  }
                  alt="one-machine-image"
                  className="one-machine-image"
                />
              </Col>
              <Col span={12}>
                <span>{info["name"]}</span>
              </Col>
              <Col span={2}>
                <Checkbox
                  id={info["name"]}
                  onChange={onOperatorSelect}
                ></Checkbox>
              </Col>
            </Row>
          );
        });

  function onMachineSelect(e) {
    if (e.target.checked) {
      setSelectedMachine([...selectedMachine, e.target.id]);
    } else {
      const index = selectedMachine.findIndex((item) => item === e.target.id);
      const newMahcine = [
        ...selectedMachine.slice(0, index),
        ...selectedMachine.slice(index + 1),
      ];
      setSelectedMachine(newMahcine);
    }
  }

  function onOperatorSelect(e) {
    if (e.target.checked) {
      setSelectedOperator([...selectedOperator, e.target.id]);
    } else {
      const index = selectedOperator.findIndex((item) => item === e.target.id);
      const newOperator = [
        ...selectedOperator.slice(0, index),
        ...selectedOperator.slice(index + 1),
      ];
      setSelectedOperator(newOperator);
    }
  }

  return (
    <div style={{ maxHeight: "80vh", overflowY: "auto", paddingRight: 30 }}>
      <div>
        <div class="option-container">
          <OptionInput
            span={12}
            initValue={selectOption}
            field="machine"
            updateValue={setOptionValue}
            title={[
              lang(langIndex, "report_machine"),
              lang(langIndex, "report_operator")
            ]}
          />
        </div>
      </div>
      {selectOption.machine == 0 && machineListUI}
      {selectOption.machine == 1 && operatorListUI}
    </div>
  );
}

export default MachineContainer;
