import { Col, Input, InputNumber, message, Modal, Row, Select, Spin, Switch } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { humanitizeDuration } from "../../../../../src/services/common/cnc_apis";
import Urls, {
  MACHINE_IMAGE_BASE_URL, postRequest
} from "../../../../../src/services/common/urls";
import lang from "../../../../../src/services/lang";
import MaintenanceImageUploader from "./maintenanceImageUploader/maintenanceImageUploader";
import "./maintenanceModal.css";


const { TextArea } = Input;
const { Option } = Select;
function MaintenanceModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const [isBusy, setBusy] = useState(false);

  const {
    addEntry,
    updateEntry,
    isShowModal,
    setIsShowModal,
    machineList,
    initEntryInfo,
  } = props;

  const [inputData, setInputData] = useState({
    completed_hours: 0,
    frequency: 0,
    picture: "blank machine.png",
    interlock: 0,
  });

  const [files, setFiles] = useState([]);
  const fileSelectedHandler = (e) => {
    setFiles(Array.from(e.target.files));
  };


  useEffect(() => {
    if (initEntryInfo === undefined) {
      if (machineList !== undefined && machineList.length > 0) {
        setInputData({ ...inputData, machine_id: machineList[0].machine_id });
      }
    } else {
      setInputData({ ...initEntryInfo });
    }
  }, [machineList, initEntryInfo]);

  const onClickAdd = () => {

    if (inputData.task_name === undefined || inputData.task_name === "") {
      message.error(lang(langIndex, "missing_param"));
      return
    }

    if (initEntryInfo === undefined) {

      var formData = new FormData();
      for (var i = 0; i < files.length; i++) {
        formData.append("files[]", files[i]);
      }

      postRequest(
        Urls.UPLOAD_JOB_FILES,
        formData,
        (response) => {
          setBusy(false);
          setIsShowModal(false);
          if (response.status === true) {
            addEntry({ ...inputData, urls: response.urls });
          } else {
            addEntry({ ...inputData });
          }
        }
      );
    } else {
      updateEntry(inputData);
    }
  };

  const onClickCancel = () => {
    setIsShowModal(false);
  };
  const updateImage = (url) => {
    setInputData({ ...inputData, picture: url });
  };

  const machineUIList = machineList.map((machineInfo) => {
    return (
      <Option
        className="page-changer-item"
        value={machineInfo.machine_id}
        key={machineInfo.machine_id}
      >
        <Row>
          <Col span={8}>
            <img
              src={
                machineInfo["machine_picture_url"].includes("http")
                  ? machineInfo["machine_picture_url"]
                  : MACHINE_IMAGE_BASE_URL + machineInfo["machine_picture_url"]
              }
              className="maintenance-machine-image-style"
            />
          </Col>
          <Col span={16}>
            <h3 style={{ color: "white" }}>{machineInfo.machine_id}</h3>
          </Col>
        </Row>
      </Option>
    );
  });

  return (
    <Spin spinning={isBusy}>
      <div>
        <Modal
          title={
            initEntryInfo === null
              ? "Maintenance Entry Add Dialog"
              : "Maintenance Entry Edit Dialog"
          }
          visible={isShowModal}
          onOk={onClickAdd}
          onCancel={onClickCancel}
          destroyOnClose={true}
          className="maintenance-entry-add-modal"
        >
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_machine")}</span>
            </Col>
            <Col span={14}>
              <Select
                defaultValue="0"
                style={{ width: "100%" }}
                value={inputData.machine_id}
                onChange={(e) => setInputData({ ...inputData, machine_id: e })}
                // defaultValue={machineList[0].machine_id}
                className="page-changer-style"
                dropdownClassName="page-changer-style-dropdown"
              >
                {machineUIList}
              </Select>
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_taskname")}</span>
            </Col>
            <Col span={14}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                placeholder={lang(langIndex, "maintenance_taskname")}
                value={
                  inputData.task_name === undefined ? "" : inputData.task_name
                }
                onChange={(e) =>
                  setInputData({ ...inputData, task_name: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_taskcategory")}</span>
            </Col>
            <Col span={14}>
              <Input
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                placeholder={lang(langIndex, "maintenance_taskcategory")}
                value={
                  inputData.task_category === undefined
                    ? ""
                    : inputData.task_category
                }
                onChange={(e) =>
                  setInputData({ ...inputData, task_category: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_taskpicture")}</span>
            </Col>
            <Col span={14}>
              <MaintenanceImageUploader
                machine_picture={inputData.picture}
                updateImage={updateImage}
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={24}>
              <span>{lang(langIndex, "maintenance_taskinstruction")}</span>
            </Col>
            <Col span={24}>
              <TextArea
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                placeholder={lang(langIndex, "maintenance_taskinstruction")}
                value={
                  inputData.task_instruction === undefined
                    ? ""
                    : inputData.task_instruction
                }
                onChange={(e) =>
                  setInputData({ ...inputData, task_instruction: e.target.value })
                }
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_frequency")}</span>
            </Col>
            <Col span={14}>
              <InputNumber
                style={{ background: "#1e1e1e", color: "#eeeeee" }}
                placeholder={lang(langIndex, "maintenance_frequency")}
                step={1}
                precision={0}
                min={0}
                value={inputData.frequency === undefined ? 0 : inputData.frequency}
                onChange={(e) => setInputData({ ...inputData, frequency: e })}
              />
            </Col>
          </Row>
          <Row style={{ marginTop: 10 }}>
            <Col span={10}>
              <span>{lang(langIndex, "maintenance_cycle")}</span>
            </Col>
            <Col span={14} className="maintenance-interlock-style">
              <Switch
                checked={inputData.interlock === 1}
                onChange={(e) =>
                  setInputData({ ...inputData, interlock: e === true ? 1 : 0 })
                }
              />
            </Col>
          </Row>
          {initEntryInfo === undefined ? null : (
            <Row style={{ marginTop: 10 }}>
              <Col span={10}>
                <span>{lang(langIndex, "maintenance_timetonext")}</span>
              </Col>
              <Col span={14}>
                <h4 style={{ color: "#eeeeee" }}>
                  {inputData.is_finished === 1
                    ? "finished"
                    : inputData.completed_hours === undefined ||
                      inputData.completed_hours === 0
                      ? ""
                      : inputData.completed_hours > 0
                        ? humanitizeDuration(inputData.completed_hours, false)
                        : "finished"}
                </h4>
              </Col>
            </Row>
          )}

          {initEntryInfo === null && (
            <Row style={{ marginTop: 15 }}>
              <Col span={8}>
                <span>{lang(langIndex, "files")}</span>
              </Col>
              <Col span={16}>
                <form>
                  <input type="file" accept=".png, .jpg, .jpeg, .pdf" multiple onChange={fileSelectedHandler} />
                </form>
              </Col>
            </Row>
          )
          }
        </Modal>
      </div>
    </Spin>
  );
}

export default MaintenanceModal;
