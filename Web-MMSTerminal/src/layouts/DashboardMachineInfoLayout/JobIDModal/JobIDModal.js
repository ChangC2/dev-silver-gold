import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { LS_ITEMS } from "services/CONSTANTS";
import { appData } from "services/global";
import "./JobIDModal.css";

function JobIDModal(props) {
  const dispatch = useDispatch();
  const { showModal, setShowModal } = props;
  const [jobID, setJobID] = useState("");

  const onCancel = () => {
    setJobID("");
    setShowModal(false);
  };

  const onOK = () => {
    if (jobID === "" || jobID === undefined) return;
    appData.jobId = jobID;
    dispatch(setAppDataStore(appData));
    setJobID("");
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="job-id-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="job-id-dialog-top">
            <Col>
              <span className="job-id-dialog-title">Job ID Input</span>
            </Col>
          </Row>
          <Row className="job-id-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="job-id-dialog-input-desc">
                Please Input Job ID
              </span>
            </Col>
            <Col span={24}>
              <input
                className="job-id-dialog-input"
                value={jobID}
                onChange={(e) => setJobID(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="job-id-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="job-id-dialog-button"
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

export default JobIDModal;
