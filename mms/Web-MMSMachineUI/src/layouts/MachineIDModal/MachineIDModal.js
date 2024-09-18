import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData, isValid } from "services/global";
import "./MachineIDModal.css";

function MachineIDModal(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { machine_id } = appDataStore;
  const { showModal, setShowModal } = props;
  const [machineID, setMachineID] = useState("");

  useEffect(() => {
    setMachineID(machine_id);
  }, [machine_id]);

  const onCancel = () => {
    setMachineID("");
    setShowModal(false);
  };

  const onOK = () => {
    if (!isValid(machineID)) return;
    appData.machine_id = machineID;
    dispatch(setAppDataStore(appData));
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="machine-id-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="machine-id-dialog-top">
            <Col>
              <span className="machine-id-dialog-title">Machine ID Input</span>
            </Col>
          </Row>
          <Row className="machine-id-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="machine-id-dialog-input-desc">
                Please Input Machine ID
              </span>
            </Col>
            <Col span={24}>
              <input
                className="machine-id-dialog-input"
                value={machineID}
                onChange={(e) => setMachineID(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="machine-id-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="machine-id-dialog-button"
                style={{ marginLeft: "5px" }}
                onClick={onOK}
                type="primary"
              >
                {"Ok"}
              </Button>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default MachineIDModal;
