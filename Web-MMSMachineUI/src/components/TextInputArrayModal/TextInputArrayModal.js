import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./TextInputArrayModal.css";

function TextInputArrayModal(props) {
  const authData = useSelector((x) => x.authService);
  const {
    showModal,
    setShowModal,
    values,
    setValues,
    titles,
    title,
    subTitle,
    span,
  } = props;

  const [mValues, setMValues] = useState([]);

  useEffect(() => {
    setMValues(values);
  }, [values]);

  const setTemperature = (index, value) => {
    let tmpValues = [...mValues];
    tmpValues[index] = value; //parseFloat(value);
    setMValues(tmpValues);
  };

  const inputUI = mValues.map((temp, index) => {
    return (
      <Col span={span} key={"key-" + index}>
        <div className="text-input-array-text-input-container">
          <div className="text-input-array-text-input-title">
            {titles[index]}
          </div>
          <input
            className="text-input-array-text-input-value"
            value={temp}
            onChange={(e) => setTemperature(index, e.target.value)}
            type="text"
            style={{ outlineStyle: "none" }}
          />
        </div>
      </Col>
    );
  });

  useEffect(() => {
    setMValues(values);
  }, [values]);

  const onCancel = () => {
    setShowModal(false);
  };

  const onOK = () => {
    setValues(mValues);
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="text-input-array-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="text-input-array-dialog-top">
            <Col>
              <span className="text-input-array-dialog-title">{title}</span>
            </Col>
          </Row>
          <Row className="text-input-array-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="text-input-array-dialog-input-desc">
                {subTitle}
              </span>
            </Col>
            <Col span={24}>
              <Row>{inputUI}</Row>
            </Col>

            <Col span={12}>
              <Button
                className="text-input-array-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="text-input-array-dialog-button"
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

export default TextInputArrayModal;
